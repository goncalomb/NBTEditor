/*
 * Copyright (C) 2013-2018 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.nbteditor.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.utils.CustomInventory;
import com.goncalomb.bukkit.nbteditor.bos.BookOfSouls;
import com.goncalomb.bukkit.nbteditor.nbt.SpawnerNBTWrapper;

final class InventoryForSpawnerEntities extends CustomInventory {

	public InventoryForSpawnerEntities(Player owner, SpawnerNBTWrapper spawner) {
		super(owner, 54, "Books of Souls (to grab a copy)");
		int i = 0;
		for (SpawnerNBTWrapper.SpawnerEntity entity : spawner.getEntities()) {
			if (i >= 54) {
				break;
			}
			_inventory.addItem((new BookOfSouls(entity.entityNBT)).getBook());
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
