package com.goncalomb.bukkit.nbteditor.nbt.variables;

import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.reflect.NBTTagList;

public class StringListVariable extends ListVariable {

	public StringListVariable(String key) {
		super(key);
	}

	@Override
	protected boolean addItem(NBTTagList list, String value, Player player) {
		list.add(value);
		return true;
	}

	@Override
	public String getFormat() {
		return "List of strings.";
	}

}
