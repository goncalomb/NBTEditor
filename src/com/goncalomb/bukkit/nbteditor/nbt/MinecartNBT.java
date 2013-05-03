package com.goncalomb.bukkit.nbteditor.nbt;

import com.goncalomb.bukkit.nbteditor.nbt.variable.BlockVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.StringVariable;

public class MinecartNBT extends EntityNBT {
	
	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Minecart");
		variables.add("display-tile", new BooleanVariable("CustomDisplayTile"));
		variables.add("tile", new BlockVariable("DisplayTile", "DisplayData", false, true));
		variables.add("tile-offset", new IntegerVariable("DisplayOffset"));
		variables.add("name", new StringVariable("CustomName"));
		EntityNBTVariableManager.registerVariables(MinecartNBT.class, variables);
	}
	
}
