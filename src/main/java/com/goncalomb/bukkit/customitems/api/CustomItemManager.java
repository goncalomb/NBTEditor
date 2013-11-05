package com.goncalomb.bukkit.customitems.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.goncalomb.bukkit.bkglib.betterplugin.BetterPlugin;

public final class CustomItemManager {
	
	private static List<String> _allowedPlugins = Arrays.asList(new String[] { "NBTEditor" });
	private static boolean _isBondToCustomItems = false;
	private static CustomItemManager _instance;

	Plugin _plugin;
	private Listener _mainListener;
	private Logger _logger;
	private Permission _usePermission;
	private Permission _worldOverridePermission;
	
	private CustomItemContainer<CustomItem> _generalContainer = new CustomItemContainer<CustomItem>();
	
	private ListenerForInteractionEvents _interactionEventsListener = new ListenerForInteractionEvents();
	private ListenerForItemEvents _itemEventsListener = new ListenerForItemEvents();
	private ListenerForDispenseEvent _dispenseEventListener = new ListenerForDispenseEvent();
	private ListenerForPlayerDeathEvent _playerDeathEventListener = new ListenerForPlayerDeathEvent();
	private ListenerForBowEvents _bowEventsListener = new ListenerForBowEvents(this);
	
	private HashMap<String, CustomItem> _customItemsBySlug = new HashMap<String, CustomItem>();
	private HashMap<Plugin, ArrayList<CustomItem>> _customItemsByPlugin = new HashMap<Plugin, ArrayList<CustomItem>>();
	
	private HashMap<Plugin, CustomItemConfig> _configsByPlugin = new HashMap<Plugin, CustomItemConfig>();
	
	public static CustomItemManager getInstance(Plugin plugin) {
		if (plugin == null) {
			return null;
			//return (_instance != null && _isBondToCustomItems ? _instance : null); // API is private, for now.
		} else if (_instance == null) {
			_isBondToCustomItems = plugin.getName().equals("CustomItemsAPI");
			if (_isBondToCustomItems || _allowedPlugins.contains(plugin.getName())) {
				_instance = new CustomItemManager(plugin);
			}
			return _instance;
		} else if (_isBondToCustomItems || _allowedPlugins.contains(plugin.getName())) {
			return _instance;
		}
		return null;
	}
	
	private CustomItemManager(Plugin plugin) {
		if (!_isBondToCustomItems) {
			_logger = new Logger(null, null) {
				
				@Override
				public void log(LogRecord logRecord) {
					logRecord.setMessage("[CustomItemsAPI] " + logRecord.getMessage());
					super.log(logRecord);
				}
				
			};
			_logger.setLevel(Level.ALL);
			_logger.setParent(Bukkit.getLogger());
			_usePermission = new Permission("customitemsapi.use.*");
			_usePermission.addParent("customitemsapi.*", true);
			Bukkit.getPluginManager().addPermission(_usePermission);
			_worldOverridePermission = new Permission("customitemsapi.world-override.*");
			_worldOverridePermission.addParent("customitemsapi.*", true);
			Bukkit.getPluginManager().addPermission(_worldOverridePermission);
		} else {
			_logger = plugin.getLogger();
		}
		reboundListeners(plugin);
	}
	
	private void reboundListeners(Plugin plugin) {
		_plugin = plugin;
		if (_mainListener == null) {
			_mainListener = new Listener() {
				
				@EventHandler
				private void inventoryClick(InventoryClickEvent event) {
					if (event.getInventory().getType() == InventoryType.ANVIL && getCustomItem(event.getCurrentItem()) != null) {
						event.setCancelled(true);
					}
				}
				
				@EventHandler
				private void pluginDisable(PluginDisableEvent event) {
					remove(event.getPlugin());
					if (event.getPlugin() == _plugin) {
						if (_isBondToCustomItems) {
							_isBondToCustomItems = false;
						}
						boolean rebounded = false;
						for (Plugin plugin : _customItemsByPlugin.keySet()) {
							if (_allowedPlugins.contains(plugin.getName())) {
								if (!rebounded) {
									reboundListeners(plugin);
								}
							} else {
								remove(plugin);
							}
						}
						if (!rebounded) {
							destroy(true);
							_logger.info("CustomItemManager disposed!");
						}
					}
				}
				
			};
		}
		
		destroy(false);

		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(_interactionEventsListener, _plugin);
		pluginManager.registerEvents(_itemEventsListener, _plugin);
		pluginManager.registerEvents(_dispenseEventListener, _plugin);
		pluginManager.registerEvents(_playerDeathEventListener, _plugin);
		pluginManager.registerEvents(_bowEventsListener, _plugin);
		
		_logger.info("CustomItemManager bound to " + _plugin.getName() + ".");
	}
	
	private void destroy(boolean hard) {
		HandlerList.unregisterAll(_interactionEventsListener);
		HandlerList.unregisterAll(_itemEventsListener);
		HandlerList.unregisterAll(_dispenseEventListener);
		HandlerList.unregisterAll(_playerDeathEventListener);
		HandlerList.unregisterAll(_bowEventsListener);
		
		if (hard) {
			_mainListener = null;
			
			_generalContainer.clear();
			
			_interactionEventsListener.clear();
			_itemEventsListener.clear();
			_dispenseEventListener.clear();
			_playerDeathEventListener.clear();
			_bowEventsListener.clear();
			
			_customItemsBySlug.clear();
			_customItemsByPlugin.clear();
			_configsByPlugin.clear();
			
			_instance = null;
		}
	}
	
	private void remove(Plugin plugin) {
		if (_customItemsByPlugin.remove(plugin) != null) {
			_generalContainer.remove(plugin);
			
			_interactionEventsListener.remove(plugin);
			_itemEventsListener.remove(plugin);
			_dispenseEventListener.remove(plugin);
			_playerDeathEventListener.remove(plugin);
			_bowEventsListener.remove(plugin);
			
			CustomItemContainer.removeFromHashMap(_customItemsBySlug, plugin);
			_customItemsByPlugin.remove(plugin);
			_configsByPlugin.remove(plugin);
		}
	}
	
	File getDataFolder() {
		return new File(BetterPlugin.getGmbConfigFolder(), "CustomItemsAPI");
	}
	
	Logger getLogger() {
		return _logger;
	}
	
	public boolean registerNew(CustomItem customItem, Plugin plugin) {
		if (customItem._owner != null || _customItemsBySlug.containsKey(customItem.getSlug())) {
			_logger.warning(plugin.getName() + " tried to register an already registed CustomItem, " + customItem.getSlug() + "!");
			return false;
		}
		
		CustomItemConfig config = _configsByPlugin.get(plugin);
		if (config == null) {
			config = new CustomItemConfig(this, plugin);
		}
		config.configureItem(customItem);
		
		boolean yep = false;
		yep |= _interactionEventsListener.put(customItem);
		yep |= _itemEventsListener.put(customItem);
		yep |= _dispenseEventListener.put(customItem);
		yep |= _playerDeathEventListener.put(customItem);
		if (customItem instanceof CustomBow) {
			yep |= _bowEventsListener.put((CustomBow) customItem);
		}
		if (!yep) {
			_logger.warning(customItem.getSlug() + " does not override any event methods!");
			config.removeItem(customItem);
			return false;
		}
		
		config.saveToFile();
		_configsByPlugin.put(plugin, config);
		
		customItem._owner = plugin;
		
		(new Permission("customitemsapi.use." + customItem.getSlug())).addParent("customitemsapi.use.*", true);
		(new Permission("customitemsapi.world-override." + customItem.getSlug())).addParent("customitemsapi.world-override.*", true);
		
		_generalContainer.put(customItem);
		_customItemsBySlug.put(customItem.getSlug(), customItem);
		
		ArrayList<CustomItem> set = _customItemsByPlugin.get(plugin);
		if (set == null) {
			set = new ArrayList<CustomItem>();
			_customItemsByPlugin.put(plugin, set);
		}
		set.add(customItem);
		
		return true;
	}
	
	public Plugin[] getOwningPlugins() {
		return _customItemsByPlugin.keySet().toArray(new Plugin[0]);
	}
	
	public CustomItem[] getCustomItems(Plugin plugin) {
		ArrayList<CustomItem> set = _customItemsByPlugin.get(plugin);
		return (set == null ? new CustomItem[] { } : set.toArray(new CustomItem[0]));
	}
	
	public CustomItem getCustomItem(String slug) {
		return _customItemsBySlug.get(slug);
	}
	
	public CustomItem getCustomItem(ItemStack item) {
		return _generalContainer.get(item);
	}
	
}
