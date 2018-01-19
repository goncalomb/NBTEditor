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

package com.goncalomb.bukkit.nbteditor.nbt;

import java.util.Arrays;

import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public class EquippableNBT extends EntityNBT {

	private void setItems(String key, ItemStack... items) {
		if (items == null) {
			_data.remove(key);
			return;
		}
		Object[] data = new Object[items.length];
		boolean allNull = true;
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				data[i] = new NBTTagCompound();
			} else {
				data[i] = NBTUtils.itemStackToNBTData(items[i]);
				allNull = false;
			}
		}
		if (allNull) {
			_data.remove(key);
		} else {
			_data.setList(key, data);
		}
	}

	private ItemStack[] getItems(String key, int size) {
		ItemStack[] items = new ItemStack[size];
		Object[] data = _data.getListAsArray(key);
		if (data != null) {
			for (int i = 0; i < data.length; i++) {
				if (data[i] != null && data[i] instanceof NBTTagCompound) {
					items[i] = NBTUtils.itemStackFromNBTData((NBTTagCompound) data[i]);
				}
			}
		}
		return items;
	}

	public void setArmorItems(ItemStack feet, ItemStack legs, ItemStack chest, ItemStack head) {
		setItems("ArmorItems", feet, legs, chest, head);
	}

	public ItemStack[] getArmorItems() {
		return getItems("ArmorItems", 4);
	}

	public void setHandItems(ItemStack main, ItemStack off) {
		setItems("HandItems", main, off);
	}

	public ItemStack[] getHandItems() {
		return getItems("HandItems", 2);
	}

	@Override
	void onUnserialize() {
		super.onUnserialize();
		// Backward compatibility with pre-1.9.
		if (_data.hasKey("Equipment")) {
			Object[] equip = _data.getListAsArray("Equipment");
			_data.setList("HandItems", new NBTTagList(equip[0], new NBTTagCompound()));
			_data.setList("ArmorItems", new NBTTagList(Arrays.copyOfRange(equip, 1, 5)));
			_data.remove("Equipment");
		}
	}

}
