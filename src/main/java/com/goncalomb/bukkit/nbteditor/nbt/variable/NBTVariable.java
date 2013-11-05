package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompoundWrapper;

public final class NBTVariable {
	
	private String _name;
	private NBTGenericVariable _generic;
	private NBTTagCompoundWrapper _data;
	
	NBTVariable(String name, NBTGenericVariable generic, NBTTagCompoundWrapper data) {
		_name = name;
		_generic = generic;
		_data = data;
	}
	
	public String getName() {
		return _name;
	}
	
	public boolean setValue(String value) {
		return _generic.set(_data, value);
	}
	
	public String getValue() {
		return _generic.get(_data);
	}
	
	public void clear() {
		_generic.clear(_data);
	}
	
	public String getFormat() {
		return _generic.getFormat();
	}
	
}
