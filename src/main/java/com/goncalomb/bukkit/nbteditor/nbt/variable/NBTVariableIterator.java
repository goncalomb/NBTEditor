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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public final class NBTVariableIterator implements Iterator<NBTVariable> {

	Iterator<Entry<String, NBTGenericVariable>> _state;
	NBTTagCompound _data;
	String _separator;

	NBTVariableIterator(LinkedHashMap<String, NBTGenericVariable> hashMap, NBTTagCompound data) {
		_state = hashMap.entrySet().iterator();
		_data = data;
	}

	public boolean hasNext() {
		return _state.hasNext();
	}

	public NBTVariable next() {
		Entry<String, NBTGenericVariable> entry = _state.next();
		return new NBTVariable(entry.getKey(), entry.getValue(), _data);
	}

	public void remove() {
		throw new UnsupportedOperationException("Cannot remove NBTVariables.");
	}

}
