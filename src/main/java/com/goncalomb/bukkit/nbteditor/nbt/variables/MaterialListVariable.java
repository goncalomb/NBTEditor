package com.goncalomb.bukkit.nbteditor.nbt.variables;

import java.util.List;

import com.goncalomb.bukkit.mylib.namemaps.MaterialMap;

public class MaterialListVariable extends StringListVariable {

	public MaterialListVariable(String key) {
		super(key);
	}

	@Override
	public String getFormat() {
		return "A list of materials.";
	}

	@Override
	public List<String> getPossibleValues() {
		return MaterialMap.getNames();
	}

}
