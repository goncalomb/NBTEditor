package com.goncalomb.bukkit.nbteditor.nbt;

import com.goncalomb.bukkit.nbteditor.nbt.variable.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.StringVariable;

public class TamedNBT extends BreedNBT {
	
	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Tameable");
		variables.add("owner", new StringVariable("Owner"));
		variables.add("sitting", new BooleanVariable("Sitting"));
		EntityNBTVariableManager.registerVariables(TamedNBT.class, variables);
	}
	
}
