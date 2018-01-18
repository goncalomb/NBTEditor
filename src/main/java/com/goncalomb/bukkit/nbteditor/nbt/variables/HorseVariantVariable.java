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

public final class HorseVariantVariable extends NBTVariable {

	public HorseVariantVariable() {
		super("Variant");
	}

	@Override
	public boolean set(String value, Player player) {
		NBTTagCompound data = data();
		String[] pieces = value.split("\\s+", 2);
		if (pieces.length == 2) {
			int markings, color;
			try {
				markings = Integer.parseInt(pieces[0]);
				color = Integer.parseInt(pieces[1]);
			} catch (NumberFormatException e) {
				return false;
			}
			if (markings >= 0 && markings <= 4 && color >= 0 && color <= 6) {
				data.setInt(_key, color | markings << 8);
				return true;
			}
		}
		return false;
	}

	@Override
	public String get() {
		NBTTagCompound data = data();
		if (data.hasKey(_key)) {
			int variant = data.getInt(_key);
			return ((variant >> 8) & 0xFF) + " " + (variant & 0xFF);
		}
		return null;
	}

	@Override
	public String getFormat() {
		return "Two integers, the fist one controls the horse markings (0 to 4), the second one controls the color (0 to 6), e.g. '4 1'.";
	}

}
