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

package com.goncalomb.bukkit.nbteditor.nbt;

import java.util.Arrays;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;

@Deprecated
public class EquippableNBT extends EntityNBT {

	@Override
	void onUnserialize() {
		super.onUnserialize();
		// Backward compatibility with pre-1.9.
		if (_data.hasKey("Equipment")) {
			Object[] equip = _data.getListAsArray("Equipment");
			_data.setList("HandItems", new NBTTagList(equip[0], new NBTTagCompound()));
			_data.setList("ArmorItems", new NBTTagList(Arrays.copyOfRange(equip, 1, 5)));
			_data.remove("Equipment");
		}
	}

}
