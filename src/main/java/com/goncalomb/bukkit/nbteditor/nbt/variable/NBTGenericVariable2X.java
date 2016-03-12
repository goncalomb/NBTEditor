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

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public abstract class NBTGenericVariable2X extends NBTGenericVariable {

	protected String _nbtKey2;

	NBTGenericVariable2X(String nbtKey1, String nbtKey2) {
		super(nbtKey1);
		_nbtKey2 = nbtKey2;
	}

	void clear(NBTTagCompound data) {
		super.clear(data);
		data.remove(_nbtKey2);
	}

}
