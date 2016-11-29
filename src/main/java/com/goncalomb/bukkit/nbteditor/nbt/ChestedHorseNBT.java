package com.goncalomb.bukkit.nbteditor.nbt;

import com.goncalomb.bukkit.nbteditor.nbt.variable.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;

public final class ChestedHorseNBT extends HorseNBT {

	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("ChestedHorse");
		variables.add("Chested", new BooleanVariable("ChestedHorse"));
		registerVariables(ChestedHorseNBT.class, variables);
	}

}
