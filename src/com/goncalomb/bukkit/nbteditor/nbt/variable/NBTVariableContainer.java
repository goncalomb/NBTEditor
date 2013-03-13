package com.goncalomb.bukkit.nbteditor.nbt.variable;

import java.util.Iterator;
import java.util.Set;

import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;

public final class NBTVariableContainer implements Iterable<NBTVariable> {
	
	NBTGenericVariableContainer _generic;
	NBTTagCompoundWrapper _data;
	
	NBTVariableContainer(NBTGenericVariableContainer generic, NBTTagCompoundWrapper data) {
		_generic = generic;
		_data = data;
	}
	
	public boolean hasVariable(String name) {
		return _generic._variables.containsKey(name);
	}
	
	public String getName() {
		return _generic._name;
	}
	
	public Set<String> getVarNames() {
		return _generic.getVarNames();
	}
	
	public NBTVariable getVariable(String name) {
		return new NBTVariable(name, _generic._variables.get(name), _data);
	}
	
	public Iterator<NBTVariable> iterator() {
		return new NBTVariableIterator(_generic._variables, _data);
	}
	
}
