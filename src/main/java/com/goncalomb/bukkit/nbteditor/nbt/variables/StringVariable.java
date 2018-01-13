package com.goncalomb.bukkit.nbteditor.nbt.variables;

import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public class StringVariable extends NBTVariable {

	public StringVariable(String key) {
		super(key);
	}

	@Override
	public boolean set(String value, Player player) {
		NBTTagCompound data = data();
		if (value.length() > 64) {
			return false;
		}
		data.setString(_key, value);
		return true;
	}

	@Override
	public String get() {
		NBTTagCompound data = data();
		if (data.hasKey(_key)) {
			return data.getString(_key);
		}
		return null;
	}

	@Override
	public String getFormat() {
		return "String (max length: 64).";
	}

}
