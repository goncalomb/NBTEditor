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

public final class FloatVariable extends NBTVariable {

	private float _min;
	float _max;

	public FloatVariable(String key) {
		this(key, -Float.MAX_VALUE);
	}

	public FloatVariable(String key, float min) {
		this(key, min, Float.MAX_VALUE);
	}

	public FloatVariable(String nbtKey, float min, float max) {
		super(nbtKey);
		_min = min;
		_max = max;
	}

	@Override
	public boolean set(String value, Player player) {
		NBTTagCompound data = data();
		try {
			float v = Float.parseFloat(value);
			if (v < _min || v > _max) return false;
			data.setFloat(_key, v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public String get() {
		NBTTagCompound data = data();
		if (data.hasKey(_key)) {
			return String.valueOf(data.getFloat(_key));
		}
		return null;
	}

	@Override
	public String getFormat() {
		return String.format("Decimal between %s and %s.", _min, _max);
	}

}
