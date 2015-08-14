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

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.nbteditor.nbt.EntityNBT;
import com.goncalomb.bukkit.nbteditor.nbt.SingleItemBasedNBT;

public abstract class InventoryForSingleItem<T extends EntityNBT & SingleItemBasedNBT> extends InventoryForBos<T> {
	
	public InventoryForSingleItem(BookOfSouls bos, Player owner, String title, ItemStack placeholder) {
		super(bos, owner, 9, title);
		for (int i = 0; i < 9; ++i) {
			if (i != 4) {
				setItem(i, ITEM_FILLER);
			}
		}
		ItemStack item = _entityNbt.getItem();
		if (item != null) {
			setItem(4, item);
		} else {
			setPlaceholder(4, placeholder);
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
		int slot = event.getRawSlot();
		boolean isShift = event.isShiftClick();
		ItemStack item = null;
		if (isShift && slot > 8 && event.getInventory().firstEmpty() == 4) {
			item = event.getCurrentItem();
		} else if (slot == 4 && !isShift && event.getCursor().getType() != Material.AIR) {
			item = event.getCursor();
		}
		if (item != null) {
			if (!isValidItem((Player) event.getWhoClicked(), item)) {
				event.setCancelled(true);
			}
		}
	}
	
	protected abstract boolean isValidItem(Player player, ItemStack item);
	
	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		_entityNbt.setItem(getContents()[4]);
		_bos.saveBook();
	}
	
}
