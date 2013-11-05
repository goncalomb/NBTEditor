package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.goncalomb.bukkit.bkglib.reflect.WorldUtils;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.StringVariable;

public class EnderPearlNBT extends EntityNBT {
	
	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Enderpearl");
		variables.add("owner", new StringVariable("ownerName"));
		EntityNBTVariableManager.registerVariables(EntityType.ENDER_PEARL, variables);
	}
	
	@Override
	public Entity spawn(Location location) {
		return WorldUtils.spawnEnderpearl(location, _data);
	}
	
}
