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

import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public class EquippableNBT extends EntityNBT {
	
	private ItemStack[] _equipment;
	
	public void setEquipment(ItemStack hand, ItemStack feet, ItemStack legs, ItemStack chest, ItemStack head) {
		if (hand == null && feet == null && legs == null && chest == null && head == null) {
			clearEquipment();
			return;
		}
		_equipment = new ItemStack[] { hand, feet, legs, chest, head };
		Object[] equipmentData = new Object[5];
		for (int i = 0; i < 5; ++i) {
			if (_equipment[i] != null) {
				equipmentData[i] = NBTUtils.itemStackToNBTData(_equipment[i]);
			} else {
				equipmentData[i] = new NBTTagCompound();
			}
		}
		_data.setList("Equipment", equipmentData);
	}
	
	public ItemStack[] getEquipment() {
		if (_equipment == null) {
			_equipment = new ItemStack[5];
			if (_data.hasKey("Equipment")) {
				Object[] equipmentData = _data.getListAsArray("Equipment");
				for (int i = 0; i < 5; ++i) {
					_equipment[i] = NBTUtils.itemStackFromNBTData((NBTTagCompound) equipmentData[i]);
				}
			}
		}
		return _equipment;
	}
	
	public void clearEquipment() {
		_data.remove("Equipment");
		_equipment = null;
	}

}
