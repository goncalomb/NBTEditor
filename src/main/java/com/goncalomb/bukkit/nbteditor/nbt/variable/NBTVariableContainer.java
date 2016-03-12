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
import java.util.Set;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public final class NBTVariableContainer implements Iterable<NBTVariable> {

	NBTGenericVariableContainer _generic;
	NBTTagCompound _data;

	NBTVariableContainer(NBTGenericVariableContainer generic, NBTTagCompound data) {
		_generic = generic;
		_data = data;
	}

	public boolean hasVariable(String name) {
		return _generic.hasVariable(name);
	}

	public String getName() {
		return _generic.getName();
	}

	public Set<String> getVarNames() {
		return _generic.getVarNames();
	}

	public NBTVariable getVariable(String name) {
		return _generic.getVariable(name, _data);
	}

	public Iterator<NBTVariable> iterator() {
		return new NBTVariableIterator(_generic._variables, _data);
	}

}
