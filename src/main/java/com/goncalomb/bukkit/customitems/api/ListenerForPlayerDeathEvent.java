package com.goncalomb.bukkit.customitems.api;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

final class ListenerForPlayerDeathEvent extends CustomItemListener<CustomItem> {
	
	@Override
	public boolean put(CustomItem customItem) {
		try {
			if (isOverriden(customItem, "onPlayerDeath", PlayerDeathEvent.class, PlayerInventoryDetails.class)) {
				return super.put(customItem);
			}
		} catch (NoSuchMethodException e) {
			throw new Error(e);
		}
		return false;
	}
	
	@EventHandler
	private void playerDeath(PlayerDeathEvent event) {
		if (size() > 0) {
			Player player = event.getEntity();
			PlayerInventory inv = player.getInventory();
			for (int i = 0, l = inv.getSize() + 4; i < l; ++i) {
				ItemStack item = inv.getItem(i);
				CustomItem customItem = get(item);
				
				if (verifyCustomItem(customItem, player, true)) {
					customItem.onPlayerDeath(event, new PlayerInventoryDetails(item, player, i));
					List<ItemStack> drops = event.getDrops();
					drops.clear();
					for (ItemStack drop : inv.getContents()) {
						drops.add(drop);
					}
					for (ItemStack drop : inv.getArmorContents()) {
						drops.add(drop);
					}
					return;
				}
			}
		}
	}
	
}
