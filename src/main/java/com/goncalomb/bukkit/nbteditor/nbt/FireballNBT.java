package com.goncalomb.bukkit.nbteditor.nbt;

import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.VectorVariable;

public class FireballNBT extends EntityNBT {
	
	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Fireball");
		variables.add("power", new VectorVariable("power"));
		EntityNBTVariableManager.registerVariables(FireballNBT.class, variables);
	}
	
	public FireballNBT() {
		_data.setList("power", 0.0d, 0.0d, 0.0d);
	}
	
}
