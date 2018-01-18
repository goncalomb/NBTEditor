package com.goncalomb.bukkit.nbteditor.nbt.variables;

public abstract class NBTVariableDouble extends NBTVariable {

	protected String _key2;

	public NBTVariableDouble(String key, String key2) {
		super(key);
		_key2 = key2;
	}

	@Override
	public void clear() {
		super.clear();
		data().remove(_key);
	}

}
