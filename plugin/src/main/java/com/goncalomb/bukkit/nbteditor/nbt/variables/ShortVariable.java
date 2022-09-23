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

public final class ShortVariable extends NumericVariable {

	public ShortVariable(String key) {
		this(key, Short.MIN_VALUE);
	}

	public ShortVariable(String key, short min) {
		this(key, min, Short.MAX_VALUE);
	}

	public ShortVariable(String key, short min, short max) {
		super(key, min, max);
	}

	@Override
	public boolean set(String value, Player player) {
        NBTTagCompound data = data();
		try {
			short v = Short.parseShort(value);
			if (v < _min || v > _max) return false;
			data.setShort(_key, v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public String get() {
        NBTTagCompound data = data();
		if (data.hasKey(_key)) {
			return String.valueOf(data.getShort(_key));
		}
		return null;
	}

}
