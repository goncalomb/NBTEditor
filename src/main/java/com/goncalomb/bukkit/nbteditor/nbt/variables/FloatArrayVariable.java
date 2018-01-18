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

public class FloatArrayVariable extends NBTVariable {

	private int _count;
	private float _min;
	private float _max;

	public FloatArrayVariable(String key, int count, float min, float max) {
		super(key);
		_count = Math.max(0, Math.min(10, count));
		_min = min;
		_max = max;
	}

	public FloatArrayVariable(String nbtKey, int count) {
		this(nbtKey, count, -Float.MAX_VALUE, Float.MIN_VALUE);
	}

	@Override
	public boolean set(String value, Player player) {
		NBTTagCompound data = data();
		String[] pieces = value.replace(',', '.').split("\\s+", _count);
		if (pieces.length == _count) {
			Object[] values = new Object[_count];
			try {
				for (int i = 0; i < _count; i++) {
					float v = Float.parseFloat(pieces[i]);
					if (v < _min || v > _max) return false;
					values[i] = v;
				}
			} catch (NumberFormatException e) {
				return false;
			}
			data.setList(_key, values);
			return true;
		}
		return false;
	}

	@Override
	public String get() {
		NBTTagCompound data = data();
		if (data.hasKey(_key)) {
			Object[] vector = data.getListAsArray(_key);
			StringBuilder sb = new StringBuilder();
			for (Object v : vector) {
				if (sb.length() != 0) {
					sb.append(" ");
				}
				sb.append(v);
			}
			return sb.toString();
		}
		return null;
	}

	@Override
	public String getFormat() {
		return String.format("Set of %s decimal numbers between %s and %s.", _count, _min, _max);
	}

}
