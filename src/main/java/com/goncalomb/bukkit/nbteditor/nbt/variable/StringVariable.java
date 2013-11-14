package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public class StringVariable extends NBTGenericVariable {
	
	public StringVariable(String nbtKey) {
		super(nbtKey);
	}
	
	boolean set(NBTTagCompoundWrapper data, String value) {
		if (value.length() > 64) {
			return false;
		}
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
		return Lang._(NBTEditor.class, "variable.formats.string");
	}
	
}
