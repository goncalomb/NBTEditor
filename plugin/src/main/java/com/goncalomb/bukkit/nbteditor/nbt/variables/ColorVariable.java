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

import org.bukkit.Color;
import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public class ColorVariable extends NBTVariable {

	public ColorVariable(String key) {
		super(key);
	}

	@Override
	public boolean set(String value, Player player) {
		NBTTagCompound data = data();
		if (!value.startsWith("#")) {
			value = "#" + value;
		}
		try {
			java.awt.Color color = java.awt.Color.decode(value);
			int c = Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()).asRGB();
			data.setInt(_key, c);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public String get() {
		NBTTagCompound data = data();
		Color color = Color.fromRGB(data.getInt(_key));
		String r = Integer.toHexString(color.getRed());
		String g = Integer.toHexString(color.getGreen());
		String b = Integer.toHexString(color.getBlue());
		return "#" + (r.length() == 1 ? "0" + r : r) + (g.length() == 1 ? "0" + g : g) + (b.length() == 1 ? "0" + b : b);
	}

	@Override
	public String getFormat() {
		return "RGB format, #FFFFFF (e.g. #FF0000 for red).";
	}

}
