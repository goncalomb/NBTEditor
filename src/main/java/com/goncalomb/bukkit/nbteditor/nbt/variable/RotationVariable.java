package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.mylib.Lang;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public class RotationVariable extends NBTGenericVariable {

	public RotationVariable(String nbtKey) {
		super(nbtKey);
	}
	
	boolean set(NBTTagCompound data, String value) {
		String[] pieces = value.replace(',', '.').split("\\s+", 2);
		float yaw, pitch;
		if (pieces.length == 2) {
			try {
				yaw = Float.parseFloat(pieces[0]);
				pitch = Float.parseFloat(pieces[1]);
			} catch (NumberFormatException e) {
				return false;
			}
			data.setList(_nbtKey, yaw, pitch);
			return true;
		}
		return false;
	}
	
	String get(NBTTagCompound data) {
		if (data.hasKey(_nbtKey)) {
			Object[] vector = data.getListAsArray(_nbtKey);
			return (Float) vector[0] + " " + (Float) vector[1];
		}
		return null;
	}
	
	String getFormat() {
		return Lang._(NBTEditor.class, "variable.formats.rotation");
	}

}
