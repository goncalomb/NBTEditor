package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.bkglib.betterplugin.Lang;

public abstract class NumericVariable extends NBTGenericVariable {
	
	protected int _min;
	protected int _max;
	
	public NumericVariable(String nbtKey, int min, int max) {
		super(nbtKey);
		_min = min;
		_max = max;
	}
	
	String getFormat() {
		return Lang._format("nbt.variable.formats.integer", _min, _max);
	}
	
}
