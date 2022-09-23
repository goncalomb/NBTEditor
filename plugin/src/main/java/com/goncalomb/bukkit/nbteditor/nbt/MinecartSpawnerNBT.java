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

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public class MinecartSpawnerNBT extends EntityNBT {

	protected MinecartSpawnerNBT() {
		super(EntityType.MINECART_MOB_SPAWNER);
	}

	public void copyFromSpawner(Block block) {
		NBTTagCompound data = NBTUtils.getTileEntityNBTData(block);
		data.remove("id");
		data.remove("x");
		data.remove("y");
		data.remove("z");
		_data.remove("SpawnData");
		_data.remove("SpawnPotentials");
		_data.merge(data);
	}

	public void copyToSpawner(Block block) {
		NBTTagCompound data = NBTUtils.getTileEntityNBTData(block);
		data.remove("SpawnData");
		data.remove("SpawnPotentials");
		data.merge(_data);
		NBTUtils.setTileEntityNBTData(block, data);
	}

}
