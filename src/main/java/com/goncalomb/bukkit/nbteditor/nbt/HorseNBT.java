/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
 *
 * This file is part of NBTEditor.
 *
 * NBTEditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NBTEditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NBTEditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.nbteditor.nbt;

import com.goncalomb.bukkit.nbteditor.nbt.variable.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.HorseVariantVariable;
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
		variables.add("variant", new HorseVariantVariable());
		EntityNBTVariableManager.registerVariables(HorseNBT.class, variables);
	}
	
}
