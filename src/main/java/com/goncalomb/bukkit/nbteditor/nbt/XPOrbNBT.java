package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;

import com.goncalomb.bukkit.bkglib.reflect.NBTUtils;
import com.goncalomb.bukkit.bkglib.reflect.WorldUtils;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.ShortVariable;

public class XPOrbNBT extends EntityNBT {
	
	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("XPOrb");
		variables.add("value", new ShortVariable("Value", (short) 0));
		EntityNBTVariableManager.registerVariables(EntityType.EXPERIENCE_ORB, variables);
	}
	
	@Override
	public Entity spawn(final Location location) {
		ExperienceOrb entity = WorldUtils.spawnXPOrb(location, (_data.hasKey("Value") ? _data.getShort("Value") : 0));
		NBTUtils.setEntityNBTTagCompound(entity, _data);
		return entity;
	}

}
