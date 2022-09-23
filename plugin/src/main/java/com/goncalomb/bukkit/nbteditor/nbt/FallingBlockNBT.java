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

import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

import com.goncalomb.bukkit.mylib.namemaps.MaterialMap;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public class FallingBlockNBT extends EntityNBT {

	protected FallingBlockNBT() {
		super(EntityType.FALLING_BLOCK);
	}

	public void copyFromTileEntity(Block block) {
		NBTTagCompound data = new NBTTagCompound();
		data.setString("Name", MaterialMap.getName(block.getType()));
		// TODO: add block states
		_data.setCompound("BlockState", data);
		NBTTagCompound tileEntityData = NBTUtils.getTileEntityNBTData(block);
		if (tileEntityData != null) {
			_data.setCompound("TileEntityData", tileEntityData);
		} else {
			_data.remove("TileEntityData");
		}
	}

	public boolean hasTileEntityData() {
		return _data.hasKey("TileEntityData");
	}

}
