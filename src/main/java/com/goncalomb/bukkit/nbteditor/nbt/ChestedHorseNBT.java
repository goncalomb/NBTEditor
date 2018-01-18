package com.goncalomb.bukkit.nbteditor.nbt;

import com.goncalomb.bukkit.nbteditor.nbt.variables.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.NBTUnboundVariableContainer;

public final class ChestedHorseNBT extends HorseNBT {

	static {
		NBTUnboundVariableContainer variables = new NBTUnboundVariableContainer("ChestedHorse");
		variables.add("Chested", new BooleanVariable("ChestedHorse"));
		registerVariables(ChestedHorseNBT.class, variables);
	}

}
