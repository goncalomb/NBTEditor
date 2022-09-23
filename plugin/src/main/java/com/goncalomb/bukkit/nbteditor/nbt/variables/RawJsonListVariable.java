package com.goncalomb.bukkit.nbteditor.nbt.variables;

import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.reflect.BukkitReflect;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;

public class RawJsonListVariable extends ListVariable {

	public RawJsonListVariable(String key) {
		super(key);
	}

	@Override
	protected boolean addItem(NBTTagList list, String value, Player player) {
		if (BukkitReflect.isValidRawJSON(value)) {
			list.add(value);
		} else {
			if (player != null) {
				player.sendMessage("§eInvalid raw JSON text, converting...");
			}
			list.add(BukkitReflect.textToRawJSON(value));
		}
		return true;
	}

	@Override
	public String getFormat() {
		return "List of raw JSON text (normal strings will be converted).";
	}

}
