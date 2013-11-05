package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompoundWrapper;

public abstract class NBTGenericVariable {
	
	protected String _nbtKey;
	
	NBTGenericVariable(String nbtKey) {
		_nbtKey = nbtKey;
	}
	
	abstract boolean set(NBTTagCompoundWrapper data, String value);
	
	abstract String get(NBTTagCompoundWrapper data);
	
	void clear(NBTTagCompoundWrapper data) {
		data.remove(_nbtKey);
	}
	
	abstract String getFormat();
	
}
