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

package com.goncalomb.bukkit.nbteditor.nbt.variables;

import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public final class IntegerVariable extends NumericVariable {

	public IntegerVariable(String key) {
		this(key, Integer.MIN_VALUE);
	}

	public IntegerVariable(String key, int min) {
		this(key, min, Integer.MAX_VALUE);
	}

	public IntegerVariable(String key, int min, int max) {
		super(key, min, max);
	}

	@Override
	public boolean set(String value, Player player) {
        NBTTagCompound data = data();
		try {
			int v = Integer.parseInt(value);
			if (v < _min || v > _max) return false;
			data.setInt(_key, v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public String get() {
        NBTTagCompound data = data();
		if (data.hasKey(_key)) {
			return String.valueOf(data.getInt(_key));
		}
		return null;
	}

}
