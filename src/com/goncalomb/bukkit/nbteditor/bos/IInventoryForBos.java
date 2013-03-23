package com.goncalomb.bukkit.nbteditor.bos;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.CustomInventory;
import com.goncalomb.bukkit.UtilsMc;
import com.goncalomb.bukkit.betterplugin.Lang;

abstract class IInventoryForBos extends CustomInventory {
	
	protected final static ItemStack _itemFiller = UtilsMc.newItem(Material.TRIPWIRE, Lang._("nbt.bos.inv.nothing"));
	
	private HashMap<Integer, ItemStack> _placeholders;
	
	protected final static ItemStack createPlaceholder(Material material, String name) {
		return UtilsMc.newItem(material, name, Lang._("nbt.bos.inv.pholder"));
	}
	
	protected final static ItemStack createPlaceholder(Material material, String name, String lore) {
		return UtilsMc.newItem(material, name, lore, Lang._("nbt.bos.inv.pholder"));
	}
	
	public IInventoryForBos(Player owner, int size, String title, HashMap<Integer, ItemStack> placeholders) {
		super(owner, size, title);
		_placeholders = placeholders;
		for (Entry<Integer, ItemStack> entry : _placeholders.entrySet()) {
			_inventory.setItem(entry.getKey(), entry.getValue());
		}
	}
	
	private boolean isPlaceholder(int slot) {
		ItemStack item = _inventory.getItem(slot);
		return (item != null && item.equals(_placeholders.get(slot)));
	}
	
	protected final ItemStack[] getContents() {
		ItemStack[] items = _inventory.getContents();
		for (Entry<Integer, ItemStack> entry : _placeholders.entrySet()) {
			ItemStack item = items[entry.getKey()];
			if (item != null && item.equals(entry.getValue())) {
				items[entry.getKey()] = null;
			}
		}
		return items;
	}
	
	@Override
	protected void inventoryClick(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		if (slot > 0 && slot < getInventory().getSize() && isPlaceholder(slot)) {
			event.setCurrentItem(new ItemStack(Material.AIR));
		}
		if (BookOfSouls.isValidBook(event.getCurrentItem())) {
			event.setCancelled(true);
		}
	}

}
