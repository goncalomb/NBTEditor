package com.goncalomb.bukkit.nbteditor.bos;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class InventoryForSingleItem extends IInventoryForBos {
	
	public InventoryForSingleItem(String title, HashMap<Integer, ItemStack> placeholders, ItemStack item, BookOfSouls bos, Player owner) {
		super(owner, 9, title, placeholders);
		Inventory inv = getInventory();
		for (int i = 0; i < 9; ++i) {
			if (i != 4) {
				inv.setItem(i, _itemFiller);
			}
		}
		if (item != null) {
			inv.setItem(4, item);
		}
	}
	
	@Override
	protected void inventoryClick(InventoryClickEvent event) {
		super.inventoryClick(event);
		if (event.getRawSlot() != 4 && event.getRawSlot() < 9) {
			event.setCancelled(true);
		}
	}
	
}
