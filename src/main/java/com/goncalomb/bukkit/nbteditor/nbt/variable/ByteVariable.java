package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompoundWrapper;

public final class ByteVariable extends NumericVariable {
	
	public ByteVariable(String nbtKey) {
		this(nbtKey, Byte.MIN_VALUE);
	}
	
	public ByteVariable(String nbtKey, byte min) {
		this(nbtKey, min, Byte.MAX_VALUE);
	}
	
	public ByteVariable(String nbtKey, byte min, byte max) {
		super(nbtKey, min, max);
	}
	
	boolean set(NBTTagCompoundWrapper data, String value) {
		try {
			byte v = Byte.parseByte(value);
			if (v < _min || v > _max) return false;
			data.setByte(_nbtKey, v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	String get(NBTTagCompoundWrapper data) {
		if (data.hasKey(_nbtKey)) {
			return String.valueOf(data.getByte(_nbtKey));
		}
		return null;
	}
	
}
