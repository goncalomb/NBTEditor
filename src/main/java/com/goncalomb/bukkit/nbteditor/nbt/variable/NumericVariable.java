package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public abstract class NumericVariable extends NBTGenericVariable {
	
	protected int _min;
	protected int _max;
	
	public NumericVariable(String nbtKey, int min, int max) {
		super(nbtKey);
		_min = min;
		_max = max;
	}
	
	String getFormat() {
		return Lang._(NBTEditor.class, "variable.formats.integer", _min, _max);
	}
	
}
