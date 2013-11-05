package com.goncalomb.bukkit.nbteditor.nbt;

import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.ShortVariable;

public class ItemsNBT extends EntityNBT {
	
	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Items");
		variables.add("health", new ShortVariable("Health", (short) 0));
		variables.add("age", new ShortVariable("Age", (short) 0, (short) 6000));
		EntityNBTVariableManager.registerVariables(ItemsNBT.class, variables);
	}
	
}
