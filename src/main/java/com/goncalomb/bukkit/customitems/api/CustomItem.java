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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

public abstract class CustomItem {

	Plugin _owner;
	String _group;
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
		if (_material.getItemType().getMaxDurability() > 0) {
			_material.setData((byte) 0);
		}
		setDefaultConfig("enabled", true);
		setDefaultConfig("name", _name);
		setDefaultConfig("lore", new ArrayList<String>());
		setDefaultConfig("allowed-worlds", new ArrayList<String>());
		setDefaultConfig("blocked-worlds", new ArrayList<String>());
	}
	
	protected final void setLore(List<String> lore) {
		setDefaultConfig("lore", lore);
	}
	
	protected final void setLore(String... lore) {
		setDefaultConfig("lore", Arrays.asList(lore));
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
	
	public final String getGroup() {
		return _group;
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
		_name = section.getString("name");
		
		ItemMeta meta = _item.getItemMeta();
		if (meta instanceof BookMeta) {
			((BookMeta) meta).setTitle(_name);
		} else {
			meta.setDisplayName(_name);
		}
		meta.setLore(section.getStringList("lore"));
		_item.setItemMeta(meta);
		
		List<String> allowedWorlds = section.getStringList("allowed-worlds");
		if (allowedWorlds == null || allowedWorlds.size() == 0) {
			List<String> blockedWorlds = section.getStringList("blocked-worlds");
			_blockedWorlds = (blockedWorlds.size() > 0 ? new HashSet<String>(blockedWorlds) : null);
			_allowedWorlds = null;
		} else {
			_allowedWorlds = new HashSet<String>(allowedWorlds);
		}
	};
	
	public void onLeftClick(PlayerInteractEvent event, PlayerDetails details) { };
	
	public void onRightClick(PlayerInteractEvent event, PlayerDetails details) { };
	
	public void onBlockBreak(BlockBreakEvent event, PlayerDetails details) { };
	
	public void onAttack(EntityDamageByEntityEvent event, PlayerDetails details) { }
	
	public void onInteractEntity(PlayerInteractEntityEvent event, PlayerDetails details) { };
	
	public void onInteractAtEntity(PlayerInteractAtEntityEvent event, PlayerDetails details) { };
	
	public void onPickup(PlayerPickupItemEvent event) { };
	
	public void onDrop(PlayerDropItemEvent event) { };
	
	public void onDespawn(ItemDespawnEvent event) { };
	
	public void onDropperPickup(InventoryPickupItemEvent event) { };
	
	public void onDispense(BlockDispenseEvent event, DispenserDetails details) { };
	
	public void onPlayerDeath(PlayerDeathEvent event, PlayerDetails details) { };
	
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
