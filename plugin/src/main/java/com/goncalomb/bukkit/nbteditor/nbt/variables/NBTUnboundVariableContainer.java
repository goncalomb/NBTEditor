package com.goncalomb.bukkit.nbteditor.nbt.variables;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public final class NBTUnboundVariableContainer {

	private String _name;
	// XXX: move this to private after finishing moving entities to the new variable system
	public NBTUnboundVariableContainer _parent;
	private HashMap<String, String> _variableNames;
	LinkedHashMap<String, NBTVariable> _variables;

	public NBTUnboundVariableContainer(String name) {
		this(name, null);
	}

	public NBTUnboundVariableContainer(String name, NBTUnboundVariableContainer parent) {
		_name = name;
		_parent = parent;
		_variableNames = new HashMap<String, String>();
		_variables = new LinkedHashMap<String, NBTVariable>();
	}

	public void add(String name, NBTVariable variable) {
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

	public NBTUnboundVariableContainer getParent() {
		return _parent;
	}

	public Set<String> getVariableNames() {
		return _variables.keySet();
	}

	public NBTVariableContainer bind(NBTTagCompound data) {
		return new NBTVariableContainer(this, data);
	}

	public NBTVariable getVariable(String name, NBTTagCompound data) {
		String formalName = _variableNames.get(name.toLowerCase());
		if (formalName != null) {
			return _variables.get(formalName).bind(data);
		}
		return null;
	}

}
