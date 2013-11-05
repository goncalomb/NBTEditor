package com.goncalomb.bukkit.customitems.api;

import org.bukkit.inventory.ItemStack;

public abstract class ItemDetails {
	
	protected ItemStack _item;
	
	ItemDetails(ItemStack item) {
		_item = item;
	}
	
	public ItemStack getItem() {
		return _item;
	}
	
}
