package com.goncalomb.bukkit.nbteditor.nbt;

import com.goncalomb.bukkit.nbteditor.nbt.variable.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;

public class ZombieNBT extends MobNBT {
	
	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Zombie");
		variables.add("is-villager", new BooleanVariable("IsVillager"));
		variables.add("is-baby", new BooleanVariable("IsBaby"));
		variables.add("conversion-time", new IntegerVariable("ConversionTime", -1));
		EntityNBTVariableManager.registerVariables(ZombieNBT.class, variables);
	}
	
}
