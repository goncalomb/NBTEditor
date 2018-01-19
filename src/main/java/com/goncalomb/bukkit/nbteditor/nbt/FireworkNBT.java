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

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public final class FireworkNBT extends EntityNBT implements SingleItemBasedNBT {

	public FireworkNBT() {
		super(EntityType.FIREWORK);
	}

	public FireworkNBT(ItemStack firework) {
		this();
		if (firework.getType() != Material.FIREWORK) throw new IllegalArgumentException("Invalid argument firework.");
		_data.setInt("Life", 0);
		_data.setCompound("FireworksItem", NBTUtils.itemStackToNBTData(firework));
		setLifeTimeFromItem(firework);
	}

	private void setLifeTimeFromItem(ItemStack firework) {
		if (firework == null) {
			_data.remove("FireworksItem");
		} else {
			_data.setInt("LifeTime", 12 + 12 * ((FireworkMeta) firework.getItemMeta()).getPower());
		}
	}

	public void setItem(ItemStack firework) {
		if (firework == null) {
			_data.remove("FireworksItem");
		} else {
			_data.setCompound("FireworksItem", NBTUtils.itemStackToNBTData(firework));
		}
		setLifeTimeFromItem(firework);
	}

	public ItemStack getItem() {
		if (_data.hasKey("FireworksItem")) {
			return NBTUtils.itemStackFromNBTData(_data.getCompound("FireworksItem"));
		}
		return null;
	}

	public boolean isSet() {
		return _data.hasKey("FireworksItem");
	}

}
