/*
 * Copyright (C) 2013 - Gonçalo Baltazar <http://goncalomb.com>
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
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.bkglib.reflect.NBTUtils;

public final class JukeboxNBTWrapper extends TileNBTWrapper {
	
	public JukeboxNBTWrapper(Block block) {
		super(block);
	}
	
	public void setRecord(ItemStack item) {
		if (item == null || item.getType() == Material.AIR) {
			_data.setInt("Record", 0);
			_data.setCompound("RecordItem", new NBTTagCompoundWrapper());
		} else {
			_data.setInt("Record", item.getTypeId());
			_data.setCompound("RecordItem", NBTUtils.nbtTagCompoundFromItemStack(item));
		}
	}
	
	@Override
	public void save() {
		if (_data.getInt("Record") != 0 && _block.getData() == 0) {
			_block.setData((byte) 1);
		} else if (_data.getInt("Record") == 0 && _block.getData() != 0) {
			_block.setData((byte) 0);
		}
		super.save();
	}

}
