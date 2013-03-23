package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.betterplugin.Lang;
import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;

public final class VectorVariable extends NBTGenericVariable{

	public VectorVariable(String nbtKey) {
		super(nbtKey);
	}
	
	boolean set(NBTTagCompoundWrapper data, String value) {
		String[] pieces = value.replace(',', '.').split("\\s+", 3);
		if (pieces.length == 3) {
			Object[] vector = new Object[3];
			try {
				vector[0] = Double.parseDouble(pieces[0]);
				vector[1] = Double.parseDouble(pieces[1]);
				vector[2] = Double.parseDouble(pieces[2]);
			} catch (NumberFormatException e) {
				return false;
			}
			data.setList(_nbtKey, vector);
			return true;
		}
		return false;
	}
	
	String get(NBTTagCompoundWrapper data) {
		if (data.hasKey(_nbtKey)) {
			Object[] vector = data.getListAsArray(_nbtKey);
			return (Double) vector[0] + ";" + (Double) vector[1] + ";" + (Double) vector[2];
		}
		return null;
	}
	
	String getFormat() {
		return Lang._("nbt.variable.formats.vector");
	}
	
}
