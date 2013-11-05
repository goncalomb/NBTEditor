package com.goncalomb.bukkit.nbteditor.nbt;

import com.goncalomb.bukkit.nbteditor.nbt.variable.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;

public class SlimeNBT extends MobNBT {
	
	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Slime");
		variables.add("size", new IntegerVariable("Size", 0, 50)); // Limited to 50
		EntityNBTVariableManager.registerVariables(SlimeNBT.class, variables);
	}
	
}
