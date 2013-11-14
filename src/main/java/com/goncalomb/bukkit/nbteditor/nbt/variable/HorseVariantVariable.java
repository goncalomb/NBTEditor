package com.goncalomb.bukkit.nbteditor.nbt.variable;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public final class HorseVariantVariable extends NBTGenericVariable{

	public HorseVariantVariable() {
		super("Variant");
	}
	
	boolean set(NBTTagCompoundWrapper data, String value) {
		String[] pieces = value.split("\\s+", 2);
		if (pieces.length == 2) {
			int markings, color;
			try {
				markings = Integer.parseInt(pieces[0]);
				color = Integer.parseInt(pieces[1]);
			} catch (NumberFormatException e) {
				return false;
			}
			if (markings >= 0 && markings <= 4 && color >= 0 && color <= 6) {
				data.setInt(_nbtKey, color | markings << 8);
				return true;
			}
		}
		return false;
	}
	
	String get(NBTTagCompoundWrapper data) {
		if (data.hasKey(_nbtKey)) {
			int variant = data.getInt(_nbtKey);
			return ((variant >> 8) & 0xFF) + " " + (variant & 0xFF);
		}
		return null;
	}
	
	String getFormat() {
		return Lang._(NBTEditor.class, "variable.formats.horse-variant");
	}
	
}
