package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.bkglib.reflect.NBTUtils;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.ShortVariable;

public class MinecartSpawnerNBT extends MinecartNBT {
	
	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("MinecartSpawner");
		variables.add("count", new ShortVariable("SpawnCount", (short) 0));
		variables.add("range", new ShortVariable("SpawnRange", (short) 0));
		variables.add("delay", new ShortVariable("Delay", (short) 0));
		variables.add("min-delay", new ShortVariable("MinSpawnDelay", (short) 0));
		variables.add("max-delay", new ShortVariable("MaxSpawnDelay", (short) 0));
		variables.add("max-entities", new ShortVariable("MaxNearbyEntities", (short) 0));
		variables.add("player-range", new ShortVariable("RequiredPlayerRange", (short) 0));
		EntityNBTVariableManager.registerVariables(EntityType.MINECART_MOB_SPAWNER, variables);
	}
	
	public void MinecartNBT() {
		_data.setString("EntityId", "Pig");
	}
	
	public void copyFromSpawner(Block block) {
		NBTTagCompoundWrapper data = NBTUtils.getTileEntityNBTTagCompound(block);
		data.remove("id");
		data.remove("x");
		data.remove("y");
		data.remove("z");
		_data.setString("EntityId", "Pig");
		_data.remove("SpawnData");
		_data.remove("SpawnPotentials");
		_data.merge(data);
	}
	
	public void copyToSpawner(Block block) {
		NBTTagCompoundWrapper data = NBTUtils.getTileEntityNBTTagCompound(block);
		data.setString("EntityId", "Pig");
		data.remove("SpawnData");
		data.remove("SpawnPotentials");
		data.merge(_data);
		NBTUtils.setTileEntityNBTTagCompound(block, data);
	}
	
}
