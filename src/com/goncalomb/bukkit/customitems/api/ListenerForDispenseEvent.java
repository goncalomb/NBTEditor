package com.goncalomb.bukkit.customitems.api;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDispenseEvent;

final class ListenerForDispenseEvent extends CustomItemListener<CustomItem> {
	
	@Override
	public boolean put(CustomItem customItem) {
		try {
			if (isOverriden(customItem, "onDispense", BlockDispenseEvent.class, DispenserDetails.class)) {
				return super.put(customItem);
			}
		} catch (NoSuchMethodException e) {
			throw new Error(e);
		}
		return false;
	}
	
	@EventHandler
	private void blockDispense(BlockDispenseEvent event) {
		CustomItem customItem = get(event.getItem());
		if (customItem != null) {
			if (verifyCustomItem(customItem, event.getBlock().getWorld())) {
				customItem.onDispense(event, new DispenserDetails(event, customItem._owner));
			} else {
				event.setCancelled(true);
			}
		}
	}
	
}
