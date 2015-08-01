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

public class RotationVariable extends NBTGenericVariable {

	public RotationVariable(String nbtKey) {
		super(nbtKey);
	}
	
	boolean set(NBTTagCompound data, String value) {
		String[] pieces = value.replace(',', '.').split("\\s+", 2);
		float yaw, pitch;
		if (pieces.length == 2) {
			try {
				yaw = Float.parseFloat(pieces[0]);
				pitch = Float.parseFloat(pieces[1]);
			} catch (NumberFormatException e) {
				return false;
			}
			data.setList(_nbtKey, yaw, pitch);
			return true;
		}
		return false;
	}
	
	String get(NBTTagCompound data) {
		if (data.hasKey(_nbtKey)) {
			Object[] vector = data.getListAsArray(_nbtKey);
			return (Float) vector[0] + " " + (Float) vector[1];
		}
		return null;
	}
	
	String getFormat() {
		return Lang._(NBTEditor.class, "variable.formats.rotation");
	}

}
