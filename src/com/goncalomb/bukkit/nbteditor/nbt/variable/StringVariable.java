package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;

public class StringVariable extends NBTGenericVariable {
	
	public StringVariable(String nbtKey) {
		super(nbtKey);
	}
	
	boolean set(NBTTagCompoundWrapper data, String value) {
		data.setString(_nbtKey, value);
		return true;
	}
	
	String get(NBTTagCompoundWrapper data) {
		if (data.hasKey(_nbtKey)) {
			return data.getString(_nbtKey);
		}
		return null;
	}
	
	String getFormat() {
		return "No one will ever read this. Muuhahahaaaa!!1";
	}
	
}
