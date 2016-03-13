/*
 * Copyright (C) 2013-2016 Gonçalo Baltazar <me@goncalomb.com>
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
import com.goncalomb.bukkit.nbteditor.nbt.variable.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;

public class BreedNBT extends MobNBT {

	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Breed");
		variables.add("InLove", new IntegerVariable("InLove", 0));
		variables.add("Age", new IntegerVariable("Age"));
		variables.add("ForcedAge", new IntegerVariable("ForcedAge")); // XXX: not working?
		variables.add("AgeLocked", new BooleanVariable("AgeLocked"));
		registerVariables(BreedNBT.class, variables);
	}

}
