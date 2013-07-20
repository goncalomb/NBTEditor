package com.goncalomb.bukkit.nbteditor.nbt;

import com.goncalomb.bukkit.nbteditor.nbt.variable.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.StringVariable;

public final class HorseNBT extends BreedNBT {
	
	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Horse");
		variables.add("type", new IntegerVariable("Type", 0, 4));
		variables.add("tamed", new BooleanVariable("Tame"));
		variables.add("chested", new BooleanVariable("ChestedHorse"));
		variables.add("eating", new BooleanVariable("EatingHaystack"));
		variables.add("owner", new StringVariable("OwnerName"));
		variables.add("variant", new IntegerVariable("Variant"));
		EntityNBTVariableManager.registerVariables(HorseNBT.class, variables);
	}
	
}
