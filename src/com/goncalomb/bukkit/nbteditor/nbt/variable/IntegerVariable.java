package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;

public final class IntegerVariable extends NumericVariable {
	
	public IntegerVariable(String nbtKey) {
		this(nbtKey, Integer.MIN_VALUE);
	}
	
	public IntegerVariable(String nbtKey, int min) {
		this(nbtKey, min, Integer.MAX_VALUE);
	}
	
	public IntegerVariable(String nbtKey, int min, int max) {
		super(nbtKey, min, max);
	}
	
	boolean set(NBTTagCompoundWrapper data, String value) {
		try {
			int v = Integer.parseInt(value);
			if (v < _min || v > _max) return false;
			data.setInt(_nbtKey, v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	String get(NBTTagCompoundWrapper data) {
		if (data.hasKey(_nbtKey)) {
			return String.valueOf(data.getInt(_nbtKey));
		}
		return null;
	}
	
}
