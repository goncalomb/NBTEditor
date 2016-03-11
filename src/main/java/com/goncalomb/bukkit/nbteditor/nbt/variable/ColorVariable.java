/*
 * Copyright (C) 2013-2016 Gonçalo Baltazar <me@goncalomb.com>
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

import org.bukkit.Color;
import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public class ColorVariable extends NBTGenericVariable {

	public ColorVariable(String nbtKey) {
		super(nbtKey);
	}

	@Override
	boolean set(NBTTagCompound data, String value, Player player) {
		if (!value.startsWith("#")) {
			value = "#" + value;
		}
		try {
			java.awt.Color color = java.awt.Color.decode(value);
			int c = Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()).asRGB();
			data.setInt(_nbtKey, c);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	String get(NBTTagCompound data) {
		Color color = Color.fromRGB(data.getInt(_nbtKey));
		String r = Integer.toHexString(color.getRed());
		String g = Integer.toHexString(color.getGreen());
		String b = Integer.toHexString(color.getBlue());
		return "#" + (r.length() == 1 ? "0" + r : r) + (g.length() == 1 ? "0" + g : g) + (b.length() == 1 ? "0" + b : b);
	}

	@Override
	String getFormat() {
		return "RGB format, #FFFFFF (e.g. #FF0000 for red).";
	}

}
