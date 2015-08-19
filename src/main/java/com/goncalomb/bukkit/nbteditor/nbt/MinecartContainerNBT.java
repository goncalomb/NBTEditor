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

package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public class MinecartContainerNBT extends MinecartNBT {
	
	public void setItemsFromInventory(Inventory inventory) {
		int l = Math.min(inventory.getSize(), getInventorySize());
		NBTTagList items = new NBTTagList();
		for (int i = 0; i < l; ++i) {
			ItemStack item = inventory.getItem(i);
			if (item != null) {
				NBTTagCompound itemNBT = NBTUtils.itemStackToNBTData(item);
				itemNBT.setByte("Slot", (byte) i);
				items.add(itemNBT);
			}
		}
		_data.setList("Items", items);
	}
	
	public void setItemsToInventory(Inventory inventory) {
		inventory.clear();
		if (_data.hasKey("Items")) {
			NBTTagList items = _data.getList("Items");
			int l = Math.min(items.size(), Math.min(inventory.getSize(), getInventorySize()));
			for (int i = 0; i < l; ++i) {
				NBTTagCompound itemNBT = (NBTTagCompound) items.get(i);
				inventory.setItem(itemNBT.getByte("Slot"), NBTUtils.itemStackFromNBTData(itemNBT));
			}
		}
	}
	
	public int getInventorySize() {
		if (getEntityType() == EntityType.MINECART_CHEST) {
			return 27;
		} else if (getEntityType() == EntityType.MINECART_HOPPER) {
			return 5;
		}
		return 0;
	}
	
}
