package com.goncalomb.bukkit.nbteditor.nbt.variable;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;

public final class NBTVariableIterator implements Iterator<NBTVariable> {
	
	Iterator<Entry<String, NBTGenericVariable>> _state;
	NBTTagCompoundWrapper _data;
	String _separator;
	
	NBTVariableIterator(LinkedHashMap<String, NBTGenericVariable> hashMap, NBTTagCompoundWrapper data) {
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
		throw new Error("Cannot remove NBTVariables.");
	}
	
}
