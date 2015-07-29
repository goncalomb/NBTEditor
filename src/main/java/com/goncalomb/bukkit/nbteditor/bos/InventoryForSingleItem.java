/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.nbteditor.bos;

import java.util.HashMap;

import org.bukkit.Material;
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
	
	protected ItemStack getItemToCheck(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		boolean isShift = event.isShiftClick();
		if (isShift && slot > 8 && event.getInventory().firstEmpty() == 4) {
			return event.getCurrentItem();
		} else if (slot == 4 && !isShift && event.getCursor().getType() != Material.AIR) {
			return event.getCursor();
		}
		return null;
	}
	
	@Override
	protected void inventoryClick(InventoryClickEvent event) {
		super.inventoryClick(event);
		if (event.getRawSlot() != 4 && event.getRawSlot() < 9) {
			event.setCancelled(true);
		}
	}
	
}
