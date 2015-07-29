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

import org.bukkit.block.Block;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;
import com.goncalomb.bukkit.nbteditor.nbt.variable.BlockVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.ByteVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.FloatVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;

public class FallingBlockNBT extends EntityNBT {
	
	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("FallingBlock");
		variables.add("block", new BlockVariable("TileID", "Data"));
		variables.add("time", new ByteVariable("Time", (byte)0));
		variables.add("drop-item", new BooleanVariable("DropItem"));
		variables.add("hurt-entities", new BooleanVariable("HurtEntities"));
		variables.add("fall-hurt-amount", new FloatVariable("FallHurtAmount", 0));
		variables.add("fall-hurt-max", new IntegerVariable("FallHurtMax", 0));
		EntityNBTVariableManager.registerVariables(FallingBlockNBT.class, variables);
	}
	
	public FallingBlockNBT() {
		_data.setByte("Time", (byte) 1);
	}
	
	public void copyFromTileEntity(Block block) {
		_data.setInt("TileID", block.getTypeId());
		_data.setByte("Data", block.getData());
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
