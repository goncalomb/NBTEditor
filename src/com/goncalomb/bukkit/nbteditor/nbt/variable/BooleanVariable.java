package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.lang.Lang;

public final class BooleanVariable extends NBTGenericVariable{

	public BooleanVariable(String nbtKey) {
		super(nbtKey);
	}
	
	boolean set(NBTTagCompoundWrapper data, String value) {
		if (value.equalsIgnoreCase("true")) {
			data.setByte(_nbtKey, (byte)1);
		} else if (value.equalsIgnoreCase("false")) {
			data.setByte(_nbtKey, (byte)0);
		} else {
			return false;
		}
		return true;
	}
	
	String get(NBTTagCompoundWrapper data) {
		if (data.hasKey(_nbtKey)) {
			return (data.getByte(_nbtKey) > 0 ? "true" : "false");
		}
		return null;
	}
	
	String getFormat() {
		return Lang._("nbt.variable.formats.boolean");
	}
	
}
