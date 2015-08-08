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

package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.mylib.Lang;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public final class BooleanVariable extends NBTGenericVariable{

	public BooleanVariable(String nbtKey) {
		super(nbtKey);
	}
	
	boolean set(NBTTagCompound data, String value) {
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
		return Lang._(NBTEditor.class, "variable.formats.boolean");
	}
	
}
