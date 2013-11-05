package com.goncalomb.bukkit.customitems.api;

import org.bukkit.inventory.ItemStack;

public interface IConsumableDetails {
	
	public void consumeItem();
	
	public ItemStack getItem();
	
}
