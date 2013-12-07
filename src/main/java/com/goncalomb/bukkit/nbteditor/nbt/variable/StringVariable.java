/*
 * Copyright (C) 2013 - Gonçalo Baltazar <http://goncalomb.com>
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

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public class StringVariable extends NBTGenericVariable {
	
	public StringVariable(String nbtKey) {
		super(nbtKey);
	}
	
	boolean set(NBTTagCompoundWrapper data, String value) {
		if (value.length() > 64) {
			return false;
		}
		data.setString(_nbtKey, value);
		return true;
	}
	
	String get(NBTTagCompoundWrapper data) {
		if (data.hasKey(_nbtKey)) {
			return data.getString(_nbtKey);
		}
		return null;
	}
	
	String getFormat() {
		return Lang._(NBTEditor.class, "variable.formats.string");
	}
	
}
