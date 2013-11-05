package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompoundWrapper;

public abstract class NBTGenericVariable2X extends NBTGenericVariable {
	
	protected String _nbtKey2;
	
	NBTGenericVariable2X(String nbtKey1, String nbtKey2) {
		super(nbtKey1);
		_nbtKey2 = nbtKey2;
	}
	
	void clear(NBTTagCompoundWrapper data) {
		super.clear(data);
		data.remove(_nbtKey2);
	}
	
}
