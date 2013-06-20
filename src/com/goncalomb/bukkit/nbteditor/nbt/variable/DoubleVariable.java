package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.betterplugin.Lang;

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
		return Lang._format("nbt.variable.formats.double");
	}
	
}
