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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public class TileNBTWrapper {
	
	protected Block _block;
	protected NBTTagCompound _data;
	
	public static final boolean allowsCustomName(Material mat) {
		return (mat == Material.CHEST || mat == Material.FURNACE
				|| mat == Material.DISPENSER || mat == Material.DROPPER
				|| mat == Material.HOPPER || mat == Material.BREWING_STAND
				|| mat == Material.ENCHANTMENT_TABLE || mat == Material.COMMAND);
	}
	
	public TileNBTWrapper(Block block) {
		_block = block;
		_data = NBTUtils.getTileEntityNBTData(_block);
	}
	
	public final boolean allowsCustomName() {
		return allowsCustomName(_block.getType());
	}
	
	public final void setCustomName(String name) {
		if (allowsCustomName()) {
			if (name == null) {
				_data.setString("CustomName", "");
			} else {
				_data.setString("CustomName", name);
			}
		}
	}
	
	public final String getCustomName() {
		return (allowsCustomName() ? _data.getString("CustomName") : null);
	}
	
	public final Location getLocation() {
		return _block.getLocation();
	}
	
	public void save() {
		NBTUtils.setTileEntityNBTData(_block, _data);
	}
	
}
