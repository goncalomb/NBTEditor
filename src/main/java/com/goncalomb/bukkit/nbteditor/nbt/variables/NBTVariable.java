package com.goncalomb.bukkit.nbteditor.nbt.variables;

import java.util.List;

import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public abstract class NBTVariable implements Cloneable {

	private NBTTagCompound _data = null;
	protected final String _key;

	public NBTVariable(String key) {
		_key = key;
	}

	protected final NBTTagCompound data() {
		if (_data == null) {
			throw new RuntimeException("Cannot access unbound NBTVariable");
		}
		return _data;
	}

	public final NBTVariable bind(NBTTagCompound data) {
		try {
			NBTVariable var = (NBTVariable) this.clone();
			var._data = data;
			return var;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public abstract boolean set(String value, Player player);

	public abstract String get();

	public void clear() {
		_data.remove(_key);
	}

	public abstract String getFormat();

	public List<String> getPossibleValues() {
		return null;
	}

}
