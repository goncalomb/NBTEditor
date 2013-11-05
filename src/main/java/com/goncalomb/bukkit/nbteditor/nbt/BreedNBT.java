package com.goncalomb.bukkit.nbteditor.nbt;

import com.goncalomb.bukkit.nbteditor.nbt.variable.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;

public class BreedNBT extends MobNBT {
	
	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Breed");
		variables.add("in-love", new IntegerVariable("InLove", 0));
		variables.add("age", new IntegerVariable("Age"));
		EntityNBTVariableManager.registerVariables(BreedNBT.class, variables);
	}
	
}
