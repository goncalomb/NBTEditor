/*
 * Copyright (C) 2013-2016 Gonçalo Baltazar <me@goncalomb.com>
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
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.ShortVariable;

public class MinecartSpawnerNBT extends MinecartNBT {

	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("MinecartSpawner");
		variables.add("Count", new ShortVariable("SpawnCount", (short) 0));
		variables.add("Range", new ShortVariable("SpawnRange", (short) 0));
		variables.add("Delay", new ShortVariable("Delay", (short) 0));
		variables.add("MinDelay", new ShortVariable("MinSpawnDelay", (short) 0));
		variables.add("MaxDelay", new ShortVariable("MaxSpawnDelay", (short) 0));
		variables.add("MaxEntities", new ShortVariable("MaxNearbyEntities", (short) 0));
		variables.add("PlayerRange", new ShortVariable("RequiredPlayerRange", (short) 0));
		registerVariables(EntityType.MINECART_MOB_SPAWNER, variables);
	}

	public void MinecartNBT() {
		NBTTagCompound simplePig = new NBTTagCompound();
		simplePig.setString("id", "Pig");
		_data.setCompound("SpawnData", simplePig);
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
