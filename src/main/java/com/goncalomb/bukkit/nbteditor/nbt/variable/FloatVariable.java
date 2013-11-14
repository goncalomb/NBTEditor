package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public final class FloatVariable extends NBTGenericVariable {
	
	private float _min;
	float _max;
	
	public FloatVariable(String nbtKey) {
		this(nbtKey, -Float.MAX_VALUE);
	}
	
	public FloatVariable(String nbtKey, float min) {
		this(nbtKey, min, Float.MAX_VALUE);
	}
	
	public FloatVariable(String nbtKey, float min, float max) {
		super(nbtKey);
		_min = min;
		_max = max;
	}
	
	boolean set(NBTTagCompoundWrapper data, String value) {
		try {
			float v = Float.parseFloat(value);
			if (v < _min || v > _max) return false;
			data.setFloat(_nbtKey, v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	String get(NBTTagCompoundWrapper data) {
		if (data.hasKey(_nbtKey)) {
			return String.valueOf(data.getFloat(_nbtKey));
		}
		return null;
	}

	@Override
	String getFormat() {
		return Lang._(NBTEditor.class, "variable.formats.float", _min, _max);
	}
	
}
