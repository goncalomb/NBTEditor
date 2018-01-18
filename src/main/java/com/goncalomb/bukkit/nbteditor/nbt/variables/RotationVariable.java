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

public class RotationVariable extends NBTVariable {

	private int _count;
	private String _parentKey;

	public RotationVariable(String key, boolean triple, String parentKey) {
		super(key);
		_count = (triple ? 3 : 2);
		_parentKey = parentKey;
	}

	public RotationVariable(String nbtKey) {
		this(nbtKey, false, null);
	}

	@Override
	public boolean set(String value, Player player) {
		NBTTagCompound data = data();
		String[] pieces = value.replace(',', '.').split("\\s+", _count);
		if (pieces.length == _count) {
			Object[] values = new Object[_count];
			try {
				for (int i = 0; i < _count; i++) {
					values[i] = Float.parseFloat(pieces[i]);
				}
			} catch (NumberFormatException e) {
				return false;
			}
			if (_parentKey != null) {
				NBTTagCompound subData = data.getCompound(_parentKey);
				if (subData == null) {
					subData = new NBTTagCompound();
					data.setCompound(_parentKey, subData);
				}
				data = subData;
			}
			data.setList(_key, values);
			return true;
		}
		return false;
	}

	@Override
	public String get() {
		NBTTagCompound data = data();
		if (_parentKey != null) {
			data = data.getCompound(_parentKey);
			if (data == null) {
				return null;
			}
		}
		if (data.hasKey(_key)) {
			Object[] vector = data.getListAsArray(_key);
			if (vector.length == 2) {
				return (Float) vector[0] + " " + (Float) vector[1];
			} else if (vector.length == 3) {
				return (Float) vector[0] + " " + (Float) vector[1] + " " + (Float) vector[2];
			}
		}
		return null;
	}

	@Override
	public void clear() {
		NBTTagCompound data = data();
		if (_parentKey == null) {
			super.clear();
		} else {
			NBTTagCompound subData = data.getCompound(_parentKey);
			if (subData != null) {
				subData.remove(_key);
				if (subData.isEmpty()) {
					data.remove(_parentKey);
				}
			}
		}
	}

	@Override
	public String getFormat() {
		if (_count == 3) {
			return "Set of 3 decimal numbers: x , y and z angles, e.g. '25.6 -90 23'.";
		}
		return "Set of 2 decimal numbers: yaw and pitch angles, e.g. '25.6 -90'.";
	}

}
