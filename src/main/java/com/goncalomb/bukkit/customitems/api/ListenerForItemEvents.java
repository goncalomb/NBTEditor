package com.goncalomb.bukkit.customitems.api;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

final class ListenerForItemEvents extends CustomItemListener<CustomItem> {
	
	@Override
	public boolean put(CustomItem customItem) {
		try {
			if (isOverriden(customItem, "onPickup", PlayerPickupItemEvent.class)
					|| isOverriden(customItem, "onDrop", PlayerDropItemEvent.class)
					|| isOverriden(customItem, "onDespawn", ItemDespawnEvent.class)
					|| isOverriden(customItem, "onDropperPickup", InventoryPickupItemEvent.class)) {
				return super.put(customItem);
			}
		} catch (NoSuchMethodException e) {
			throw new Error(e);
		}
		return false;
	}
	
	@EventHandler
	private void playerPickupItem(PlayerPickupItemEvent event) {
		CustomItem customItem = get(event.getItem().getItemStack());
		if (verifyCustomItem(customItem, event.getPlayer(), true)) {
			customItem.onPickup(event);
		}
	}
	
	@EventHandler
	private void playerDropItem(PlayerDropItemEvent event) {
		CustomItem customItem = get(event.getItemDrop().getItemStack());
		if (verifyCustomItem(customItem, event.getPlayer(), true)) {
			customItem.onDrop(event);
		}
	}
	
	@EventHandler
	private void itemDespawnItem(ItemDespawnEvent event) {
		CustomItem customItem = get(event.getEntity().getItemStack());
		if (verifyCustomItem(customItem, event.getEntity().getWorld())) {
			customItem.onDespawn(event);
		}
	}
	
	@EventHandler
	private void inventoryPickupItemItem(InventoryPickupItemEvent event) {
		CustomItem customItem = get(event.getItem().getItemStack());
		if (verifyCustomItem(customItem, event.getItem().getWorld())) {
			customItem.onDropperPickup(event);
		}
	}
	
}
