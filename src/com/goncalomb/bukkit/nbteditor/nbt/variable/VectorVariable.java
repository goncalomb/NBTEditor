package com.goncalomb.bukkit.nbteditor.nbt.variable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.lang.Lang;

public final class VectorVariable extends NBTGenericVariable{

	public VectorVariable(String nbtKey) {
		super(nbtKey);
	}
	
	boolean set(NBTTagCompoundWrapper data, String value) {
		Matcher matcher = Pattern.compile("^(-?\\d+.?\\d*);(-?\\d+.?\\d*);(-?\\d+.?\\d*)$").matcher(value);
		if (matcher.find()) {
			Object[] vector = new Object[3];
			try {
				vector[0] = Double.parseDouble(matcher.group(1));
				vector[1] = Double.parseDouble(matcher.group(2));
				vector[2] = Double.parseDouble(matcher.group(3));
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
