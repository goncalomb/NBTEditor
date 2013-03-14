package com.goncalomb.bukkit.customitems.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

import com.goncalomb.bukkit.Utils;

public abstract class CustomItem {
	
	Plugin _owner;
	private String _slug;
	private String _name;
	private MaterialData _material;
	private ItemStack _item;
	
	LinkedHashMap<String, Object> _defaultConfig = new LinkedHashMap<String, Object>();
	
	private boolean _enabled;
	private HashSet<String> _allowedWorlds = new HashSet<String>();
	private HashSet<String> _blockedWorlds = new HashSet<String>();
	
	protected CustomItem(String slug, String name, MaterialData material) {
		_slug = slug;
		_name = name;
		_material = material;
		_item = _material.toItemStack(1);
		setItemName(_item, _name);
		if (_material.getItemType().getMaxDurability() > 0) {
			_material.setData((byte) 0);
		}
		setDefaultConfig("enabled", true);
		setDefaultConfig("allowed-worlds", "");
		setDefaultConfig("blocked-worlds", "");
	}
	
	protected final void setLore(List<String> lore) {
		if (_owner == null) {
			ItemMeta meta = _item.getItemMeta();
			meta.setLore(lore);
			_item.setItemMeta(meta);
		}
	}
	
	protected final void setLore(String... lore) {
		if (_owner == null) {
			setLore(Arrays.asList(lore));
		}
	}
	
	protected final void addEnchantment(Enchantment enchantment, int level) {
		if (_owner == null) {
			ItemMeta meta = _item.getItemMeta();
			meta.addEnchant(enchantment, level, true);
			_item.setItemMeta(meta);
		}
	}
	
	protected final void setDefaultConfig(String name, Object value) {
		if (_owner == null) {
			_defaultConfig.put(name, value);
		}
	}
	
	public final Plugin getPlugin() {
		return _owner;
	}
	
	public final String getSlug() {
		return _slug;
	}
	
	public final String getName() {
		return _name;
	}
	
	public final MaterialData getMaterial() {
		return _material;
	}
	
	public ItemStack getItem() {
		return _item.clone();
	}
	
	protected void applyConfig(ConfigurationSection section) {
		_enabled = section.getBoolean("enabled");
		String[] allowedWorlds = Utils.split(section.getString("allowed-worlds"), Utils.SplitType.COMMAS);
		if (allowedWorlds.length == 0) {
			String[] blockedWorlds = Utils.split(section.getString("blocked-worlds"), Utils.SplitType.COMMAS);
			_blockedWorlds = (blockedWorlds.length > 0 ? new HashSet<String>(Arrays.asList(blockedWorlds)) : null);
			_allowedWorlds = null;
		} else {
			_allowedWorlds = new HashSet<String>(Arrays.asList(allowedWorlds));
		}
	};
	
	public void onLeftClick(PlayerInteractEvent event, PlayerDetails details) { };
	
	public void onRightClick(PlayerInteractEvent event, PlayerDetails details) { };
	
	public void onAttack(EntityDamageByEntityEvent event, PlayerDetails details) { }
	
	public void onInteractEntity(PlayerInteractEntityEvent event, PlayerDetails details) { };
	
	public void onPickup(PlayerPickupItemEvent event) { };
	
	public void onDrop(PlayerDropItemEvent event) { };
	
	public void onDespawn(ItemDespawnEvent event) { };
	
	public void onDispense(BlockDispenseEvent event, DispenserDetails details) { };
	
	public void onPlayerDeath(PlayerDeathEvent event, PlayerInventoryDetails details) { };
	
	public final boolean isEnabled() {
		return _enabled;
	}
	
	public final boolean isValidWorld(World world) {
		String wName = world.getName();
		if (_allowedWorlds == null) {
			return _blockedWorlds == null || !_blockedWorlds.contains(wName);
		} else {
			return _allowedWorlds.contains(wName);
		}
	}
	
	static ItemStack setItemName(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		if (meta instanceof BookMeta) {
			((BookMeta) meta).setTitle(name);
		} else {
			meta.setDisplayName(name);
		}
		item.setItemMeta(meta);
		return item;
	}
	
	static String getItemName(ItemStack item) {
		if (item != null) {
			ItemMeta meta = item.getItemMeta();
			if (meta != null) {
				if (meta instanceof BookMeta) {
					return ((BookMeta) meta).getTitle();
				} else {
					return meta.getDisplayName();
				}
			}
		}
		return null;
	}
}
