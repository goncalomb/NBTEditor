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

import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompoundWrapper;

public final class IntegerVariable extends NumericVariable {
	
	public IntegerVariable(String nbtKey) {
		this(nbtKey, Integer.MIN_VALUE);
	}
	
	public IntegerVariable(String nbtKey, int min) {
		this(nbtKey, min, Integer.MAX_VALUE);
	}
	
	public IntegerVariable(String nbtKey, int min, int max) {
		super(nbtKey, min, max);
	}
	
	boolean set(NBTTagCompoundWrapper data, String value) {
		try {
			int v = Integer.parseInt(value);
			if (v < _min || v > _max) return false;
			data.setInt(_nbtKey, v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	String get(NBTTagCompoundWrapper data) {
		if (data.hasKey(_nbtKey)) {
			return String.valueOf(data.getInt(_nbtKey));
		}
		return null;
	}
	
}
