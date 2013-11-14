package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public final class DoubleVariable extends NBTGenericVariable {
	
	public DoubleVariable(String nbtKey) {
		super(nbtKey);
	}
	
	boolean set(NBTTagCompoundWrapper data, String value) {
		try {
			data.setDouble(_nbtKey, Double.parseDouble(value));
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	String get(NBTTagCompoundWrapper data) {
		if (data.hasKey(_nbtKey)) {
			return String.valueOf(data.getDouble(_nbtKey));
		}
		return null;
	}

	@Override
	String getFormat() {
		return Lang._(NBTEditor.class, "variable.formats.double");
	}
	
}
