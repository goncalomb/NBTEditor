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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.nbteditor.nbt.DroppedItemNBT;

public final class InventoryForDroppedItems extends InventoryForSingleItem {
	
	private static HashMap<Integer, ItemStack> _placeholders = new HashMap<Integer, ItemStack>();
	
	static {
		_placeholders.put(4, createPlaceholder(Material.PAPER, "§6The item goes here."));
	}
	
	private BookOfSouls _bos;
	
	public InventoryForDroppedItems(BookOfSouls bos, Player owner) {
		super("Define the item here...", _placeholders, ((DroppedItemNBT) bos.getEntityNBT()).getItem(), bos, owner);
		_bos = bos;
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		((DroppedItemNBT) _bos.getEntityNBT()).setItem(getContents()[4]);
		_bos.saveBook();
		((Player)event.getPlayer()).sendMessage("§aItem set.");
	}

}
