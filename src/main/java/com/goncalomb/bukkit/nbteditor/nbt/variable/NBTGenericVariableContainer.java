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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public final class NBTGenericVariableContainer {
	
	private String _name;
	private HashMap<String, String> _variableNames;
	LinkedHashMap<String, NBTGenericVariable> _variables;
	
	public NBTGenericVariableContainer(String name) {
		_name = name;
		_variableNames = new HashMap<String, String>();
		_variables = new LinkedHashMap<String, NBTGenericVariable>();
	}
	
	public void add(String name, NBTGenericVariable variable) {
		String lower = name.toLowerCase();
		if (!_variableNames.containsKey(lower)) {
			_variableNames.put(lower, name);
			_variables.put(name, variable);
		}
	}
	
	public boolean hasVariable(String name) {
		return _variableNames.containsKey(name.toLowerCase());
	}
	
	public String getName() {
		return _name;
	}
	
	public Set<String> getVarNames() {
		return _variables.keySet();
	}
	
	public NBTVariableContainer boundToData(NBTTagCompound data) {
		return new NBTVariableContainer(this, data);
	}
	
	public NBTVariable getVariable(String name, NBTTagCompound data) {
		String formalName = _variableNames.get(name.toLowerCase());
		if (formalName != null) {
			return new NBTVariable(formalName, _variables.get(formalName), data);
		}
		return null;
	}
	
}
