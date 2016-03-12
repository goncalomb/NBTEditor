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

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public final class VectorVariable extends NBTGenericVariable{

	private boolean _allowHere;

	public VectorVariable(String nbtKey) {
		this(nbtKey, false);
	}

	public VectorVariable(String nbtKey, boolean allowHere) {
		super(nbtKey);
		_allowHere = allowHere;
	}

	boolean set(NBTTagCompound data, String value, Player player) {
		if (_allowHere && player != null) {
			if (value.equalsIgnoreCase("Here")) {
				Location loc = player.getLocation();
				data.setList(_nbtKey, loc.getBlockX() + 0.5d, loc.getBlockY() + 0.5d, loc.getBlockZ() + 0.5d);
				return true;
			}
			if (value.equalsIgnoreCase("HereExact")) {
				Location loc = player.getLocation();
				data.setList(_nbtKey, loc.getX(), loc.getY(), loc.getZ());
				return true;
			}
		}
		String[] pieces = value.replace(',', '.').split("\\s+", 3);
		if (pieces.length == 3) {
			Object[] vector = new Object[3];
			try {
				vector[0] = Double.parseDouble(pieces[0]);
				vector[1] = Double.parseDouble(pieces[1]);
				vector[2] = Double.parseDouble(pieces[2]);
			} catch (NumberFormatException e) {
				return false;
			}
			data.setList(_nbtKey, vector);
			return true;
		}
		return false;
	}

	String get(NBTTagCompound data) {
		if (data.hasKey(_nbtKey)) {
			Object[] vector = data.getListAsArray(_nbtKey);
			return (Double) vector[0] + ";" + (Double) vector[1] + ";" + (Double) vector[2];
		}
		return null;
	}

	String getFormat() {
		if (_allowHere) {
			return "Set of 3 decimal numbers, '0.00 0.00 0.00'. Use 'Here' or 'HereExact' to set with your current position.";
		}
		return "Set of 3 decimal numbers, '0.00 0.00 0.00'.";
	}

	public List<String> getPossibleValues() {
		if (_allowHere) {
			return Arrays.asList(new String[] { "Here", "HereExact" });
		}
		return null;
	}

}
