/*
 * Copyright (C) 2013, 2014 - Gonçalo Baltazar <http://goncalomb.com>
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
import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public final class DoubleVariable extends NBTGenericVariable {
	
	public DoubleVariable(String nbtKey) {
		super(nbtKey);
	}
	
	boolean set(NBTTagCompound data, String value) {
		try {
			data.setDouble(_nbtKey, Double.parseDouble(value));
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	String get(NBTTagCompound data) {
		if (data.hasKey(_nbtKey)) {
			return String.valueOf(data.getDouble(_nbtKey));
		}
		return null;
	}

	@Override
	String getFormat() {
		return Lang._(NBTEditor.class, "variable.formats.double");
	}
	
}
