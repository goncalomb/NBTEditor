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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

final class CustomItemContainer {
	
	private HashMap<MaterialData, HashMap<String, CustomItem>> _customItems = new HashMap<MaterialData, HashMap<String, CustomItem>>();
	private HashMap<Plugin, ArrayList<CustomItem>> _customItemsByPlugin = new HashMap<Plugin, ArrayList<CustomItem>>();
	private HashMap<String, CustomItem> _customItemsBySlug = new HashMap<String, CustomItem>();
	
	public boolean contains(CustomItem customItem) {
		return _customItemsBySlug.containsKey(customItem.getSlug());
	}
	
	public boolean put(CustomItem customItem, Plugin owner) {
		if (customItem._owner != null && !contains(customItem)) {
			// Insert into the HashMap by item type.
			HashMap<String, CustomItem> itemMap = _customItems.get(customItem.getMaterial());
			if (itemMap == null) {
				itemMap = new HashMap<String, CustomItem>();
				_customItems.put(customItem.getMaterial(), itemMap);
			}
			itemMap.put(customItem.getName(), customItem);
			// Insert into the HashMap by plugin.
			ArrayList<CustomItem> itemList = _customItemsByPlugin.get(owner);
			if (itemList == null) {
				itemList = new ArrayList<CustomItem>();
				_customItemsByPlugin.put(owner, itemList);
			}
			itemList.add(customItem);
			// Insert into the HashMap by slug.
			_customItemsBySlug.put(customItem.getSlug(), customItem);
			// Set the owner.
			customItem._owner = owner;
		}
		return true;
	}
	
	public CustomItem get(ItemStack item) {
		String name = CustomItem.getItemName(item);
		if (name != null) {
			MaterialData data = item.getData();
			if (data.getItemType().getMaxDurability() > 0) {
				data.setData((byte) 0);
			}
			HashMap<String, CustomItem> itemMap = _customItems.get(data);
			if (itemMap != null) {
				CustomItem customItem = itemMap.get(name);
				if (customItem != null) {
					return customItem;
				}
			}
		}
		return null;
	}
	
	public CustomItem get(String slug) {
		return _customItemsBySlug.get(slug);
	}
	
	public Collection<CustomItem> get(Plugin plugin) {
		ArrayList<CustomItem> list = _customItemsByPlugin.get(plugin);
		return Collections.unmodifiableCollection(list == null ? new ArrayList<CustomItem>() : list);
	}
	
	public Collection<CustomItem> getAll() {
		return Collections.unmodifiableCollection(_customItemsBySlug.values());
	}
	
	public Collection<Plugin> getOwners() {
		return Collections.unmodifiableCollection(_customItemsByPlugin.keySet());
	}
	
	private static void remove(Collection<CustomItem> col, Plugin plugin) {
		for (Iterator<CustomItem> it = col.iterator(); it.hasNext(); ) {
			if (it.next()._owner == plugin) {
				it.remove();
			}
		}
	}
	
	public void remove(Plugin plugin) {
		for (HashMap<String, CustomItem> map : _customItems.values()) {
			remove(map.values(), plugin);
		}
		remove(_customItemsBySlug.values(), plugin);
		_customItemsByPlugin.remove(plugin);
	}
	
	public void clear() {
		_customItems.clear();
		_customItemsByPlugin.clear();
		_customItemsBySlug.clear();
	}
	
}
