package com.goncalomb.bukkit.customitems.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

class CustomItemContainer<T extends CustomItem> {
	private HashMap<MaterialData, HashMap<String, T>> _customItems = new HashMap<MaterialData, HashMap<String, T>>();
	
	public static <T extends CustomItem> void removeFromHashMap(HashMap<String, T> map, Plugin plugin) {
		for (Iterator<Entry<String, T>> it = map.entrySet().iterator(); it.hasNext(); ) {
			if (it.next().getValue()._owner == plugin) {
				it.remove();
			}
		}
	}
	
	public boolean put(T customItem) {
		HashMap<String, T> itemMap = _customItems.get(customItem.getMaterial());
		if (itemMap == null) {
			itemMap = new HashMap<String, T>();
			_customItems.put(customItem.getMaterial(), itemMap);
		}
		itemMap.put(customItem.getName(), customItem);
		return true;
	}
	
	public final T get(ItemStack item) {
		String name = CustomItem.getItemName(item);
		if (name != null) {
			MaterialData data = item.getData();
			if (data.getItemType().getMaxDurability() > 0) {
				data.setData((byte) 0);
			}
			HashMap<String, T> itemMap = _customItems.get(data);
			if (itemMap != null) {
				T customItem = itemMap.get(name);
				if (customItem != null) {
					return customItem;
				}
			}
		}
		return null;
	}
	
	public final int size() {
		return _customItems.size();
	}
	
	public final void remove(Plugin plugin) {
		for (HashMap<String, T> map : _customItems.values()) {
			removeFromHashMap(map, plugin);
		}
	}
	
	public final void clear() {
		_customItems.clear();
	}
	
}
