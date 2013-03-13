package com.goncalomb.bukkit.nbteditor.nbt.variable;

import java.util.LinkedHashMap;
import java.util.Set;

import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;

public final class NBTGenericVariableContainer {
	
	String _name;
	LinkedHashMap<String, NBTGenericVariable> _variables;
	
	public NBTGenericVariableContainer(String name) {
		_name = name;
		_variables = new LinkedHashMap<String, NBTGenericVariable>();
	}
	
	public void add(String name, NBTGenericVariable variable) {
		_variables.put(name, variable);
	}
	
	public boolean hasVariable(String name) {
		return _variables.containsKey(name);
	}
	
	public String getName() {
		return _name;
	}
	
	public Set<String> getVarNames() {
		return _variables.keySet();
	}
	
	public NBTVariableContainer boundToData(NBTTagCompoundWrapper data) {
		return new NBTVariableContainer(this, data);
	}
	
	public NBTVariable getVariable(String name, NBTTagCompoundWrapper data) {
		if (hasVariable(name)) {
			return new NBTVariable(name, _variables.get(name), data);
		}
		return null;
	}
	
}
