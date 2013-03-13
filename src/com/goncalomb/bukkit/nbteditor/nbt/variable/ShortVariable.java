package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;

public final class ShortVariable extends NumericVariable {
	
	public ShortVariable(String nbtKey) {
		this(nbtKey, Short.MIN_VALUE);
	}
	
	public ShortVariable(String nbtKey, short min) {
		this(nbtKey, min, Short.MAX_VALUE);
	}
	
	public ShortVariable(String nbtKey, short min, short max) {
		super(nbtKey, min, max);
	}
	
	boolean set(NBTTagCompoundWrapper data, String value) {
		try {
			short v = Short.parseShort(value);
			if (v < _min || v > _max) return false;
			data.setShort(_nbtKey, v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	String get(NBTTagCompoundWrapper data) {
		if (data.hasKey(_nbtKey)) {
			return String.valueOf(data.getShort(_nbtKey));
		}
		return null;
	}
	
}
