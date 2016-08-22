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

public class ZombieNBT extends MobNBT {

	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Zombie");
		variables.add("Type", new IntegerVariable("ZombieType", 0, 6));
		variables.add("IsBaby", new BooleanVariable("IsBaby"));
		variables.add("ConversionTime", new IntegerVariable("ConversionTime", -1));
		variables.add("CanBreakDoors", new BooleanVariable("CanBreakDoors"));
		registerVariables(ZombieNBT.class, variables);
	}

	@Override
	void onUnserialize() {
		super.onUnserialize();
		// Backward compatibility with pre-1.10.
		if (_data.hasKey("IsVillager")) {
			if (_data.getByte("IsVillager") != 0) {
				if (_data.hasKey("VillagerProfession")) {
					_data.setInt("ZombieType", _data.getInt("VillagerProfession") + 1);
					_data.remove("VillagerProfession");
				} else {
					_data.setInt("ZombieType", 1);
				}
			}
			_data.remove("IsVillager");
		}
	}

}
