/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
 *
 * This file is part of NBTEditor.
 *
 * NBTEditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NBTEditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NBTEditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.customitems.api;

import java.util.Collection;
import java.util.HashMap;

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

import com.goncalomb.bukkit.mylib.utils.UtilsMc;

public final class CustomItemManager {

	static Plugin _plugin;
	private static Listener _mainListener;
	private static Permission _usePermission;
	private static Permission _worldOverridePermission;
	private static CustomItemContainer _container = new CustomItemContainer();
	private static CustomItemListener _listener = new CustomItemListener();
	private static HashMap<String, CustomItemConfig> _configsByGroup = new HashMap<String, CustomItemConfig>();
	
	private CustomItemManager() { }
	
	public static void initialize() {
		// Find NBTEditor plugin...
		if (_plugin != null || (_plugin = Bukkit.getPluginManager().getPlugin("NBTEditor")) == null) {
			return;
		}
		
		Permission mainPermission = new Permission("nbteditor.customitems.*");
		mainPermission.addParent(UtilsMc.getRootPermission(_plugin), true);
		
		_usePermission = new Permission("nbteditor.customitems.use.*");
		_usePermission.addParent(mainPermission, true);
		Bukkit.getPluginManager().addPermission(_usePermission);
		
		_worldOverridePermission = new Permission("nbteditor.customitems.world-override.*");
		_worldOverridePermission.addParent(mainPermission, true);
		Bukkit.getPluginManager().addPermission(_worldOverridePermission);
		
		_mainListener = new Listener() {
			@EventHandler
			private void onInventoryClick(InventoryClickEvent event) {
				if (event.getInventory().getType() == InventoryType.ANVIL && getCustomItem(event.getCurrentItem()) != null) {
					event.setCancelled(true);
				}
			}
			@EventHandler
			private void onPluginDisable(PluginDisableEvent event) {
				Plugin plugin = event.getPlugin();
				if (plugin == _plugin) {
					HandlerList.unregisterAll(_mainListener);
					HandlerList.unregisterAll(_listener);
					_plugin = null;
					_mainListener = null;
					_container.clear();
					_configsByGroup.clear();
				} else {
					_container.remove(plugin);
				}
			}
		};
		
		Bukkit.getPluginManager().registerEvents(_mainListener, _plugin);
		Bukkit.getPluginManager().registerEvents(_listener, _plugin);
	}
	
	public static boolean register(CustomItem customItem, Plugin plugin, String group) {
		initialize();
		if (_plugin == null) {
			return false;
		} else if (customItem._owner != null || _container.contains(customItem)) {
			_plugin.getLogger().warning(plugin.getName() + " tried to register an already registered CustomItem, " + customItem.getSlug());
			return false;
		}
		
		CustomItemConfig config = _configsByGroup.get(group);
		if (config == null) {
			config = new CustomItemConfig(group);
		}
		config.configureItem(customItem);
		
		config.saveToFile();
		_configsByGroup.put(group, config);

		customItem._owner = plugin;
		customItem._group = group;
		
		(new Permission("nbteditor.customitems.use." + customItem.getSlug())).addParent(_usePermission, true);
		(new Permission("nbteditor.customitems.world-override." + customItem.getSlug())).addParent(_worldOverridePermission, true);
		
		_container.put(customItem, plugin, group);
		return true;
	}
	
	public static CustomItem getCustomItem(ItemStack item) {
		return _container.get(item);
	}
	
	public static CustomItem getCustomItem(String slug) {
		return _container.get(slug);
	}
	
	public static Collection<CustomItem> getCustomItems(Plugin plugin) {
		return _container.getByOwner(plugin);
	}
	
	public static Collection<CustomItem> getCustomItems(String group) {
		return _container.getByGroup(group);
	}
	
	public static Collection<CustomItem> getCustomItems() {
		return _container.getAll();
	}
	
	public static Collection<Plugin> getOwningPlugins() {
		return _container.getOwners();
	}
	
	public static Collection<String> getGroups() {
		return _container.getGroups();
	}
	
}
