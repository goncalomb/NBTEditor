/*
 * Copyright (C) 2013-2018 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.nbteditor.nbt.variable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public final class BooleanVariable extends NBTGenericVariable{

	private static final List<String> POSSIBLE_VALUES = Collections.unmodifiableList(Arrays.asList(new String[] { "true", "false" }));

	public BooleanVariable(String nbtKey) {
		super(nbtKey);
	}

	boolean set(NBTTagCompound data, String value, Player player) {
		String lower = value.toLowerCase();
		if ("true".startsWith(lower)) {
			data.setByte(_nbtKey, (byte)1);
		} else if ("false".startsWith(lower)) {
			data.setByte(_nbtKey, (byte)0);
		} else {
			return false;
		}
		return true;
	}

	String get(NBTTagCompound data) {
		if (data.hasKey(_nbtKey)) {
			return (data.getByte(_nbtKey) > 0 ? "true" : "false");
		}
		return null;
	}

	String getFormat() {
		return "Boolean value 'true' or 'false'.";
	}

	public List<String> getPossibleValues() {
		return POSSIBLE_VALUES;
	}

}
