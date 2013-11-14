package com.goncalomb.bukkit.nbteditor.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.bkglib.utils.CustomInventory;
import com.goncalomb.bukkit.nbteditor.bos.BookOfSouls;
import com.goncalomb.bukkit.nbteditor.nbt.SpawnerEntityNBT;
import com.goncalomb.bukkit.nbteditor.nbt.SpawnerNBTWrapper;

final class InventoryForSpawnerEntities extends CustomInventory {
	
	public InventoryForSpawnerEntities(Player owner, SpawnerNBTWrapper spawner) {
		super(owner, 54, "Books of Souls (to grab a copy)");
		int i = 0;
		for (SpawnerEntityNBT entity : spawner.getEntities()) {
			if (i >= 54) {
				break;
			}
			_inventory.addItem((new BookOfSouls(entity.getEntityNBT())).getBook());
		}
	}

	@Override
	protected void inventoryClick(final InventoryClickEvent event) {
		final int slot = event.getRawSlot();
		if (slot >= 0 && slot < 54) {
			final ItemStack item = event.getCurrentItem().clone();
			if (event.isShiftClick()) {
				Bukkit.getScheduler().runTask(getPlugin(), new Runnable() {
					@Override
					public void run() {
						event.setCurrentItem(item);
					}
				});
			} else {
				if (event.getCursor().getType() == Material.AIR) {
					event.getView().setCursor(item);
				}
				event.setCancelled(true);
			}
		} else if (event.getCursor().getType() == Material.AIR) {
			event.setCancelled(true);
		}
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) { }

}
