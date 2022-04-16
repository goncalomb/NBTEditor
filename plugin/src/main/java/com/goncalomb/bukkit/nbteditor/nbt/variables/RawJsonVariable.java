package com.goncalomb.bukkit.nbteditor.nbt.variables;

import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.reflect.BukkitReflect;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public class RawJsonVariable extends NBTVariable {

	public RawJsonVariable(String key) {
		super(key);
	}

	@Override
	public boolean set(String value, Player player) {
		NBTTagCompound data = data();
		if (BukkitReflect.isValidRawJSON(value)) {
			data.setString(_key, value);
		} else {
			if (player != null) {
				player.sendMessage("§eInvalid raw JSON text, converting...");
			}
			data.setString(_key, BukkitReflect.textToRawJSON(value));
		}
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
		return "Raw JSON text (normal strings will be converted).";
	}

}
