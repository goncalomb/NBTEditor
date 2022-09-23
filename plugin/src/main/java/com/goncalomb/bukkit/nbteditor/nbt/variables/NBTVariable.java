package com.goncalomb.bukkit.nbteditor.nbt.variables;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public abstract class NBTVariable implements Cloneable {

	private NBTTagCompound _data = null;
	private String[] _keyPath = null;
	protected final String _key;

	public NBTVariable(String key) {
		_keyPath = key.split("/");
		_key = _keyPath[_keyPath.length - 1];
		_keyPath = Arrays.copyOfRange(_keyPath, 0, _keyPath.length - 1);
	}

	protected final NBTTagCompound data() {
		if (_data == null) {
			throw new RuntimeException("Cannot access unbound NBTVariable");
		}
		NBTTagCompound data = _data;
		NBTTagCompound next = data;
		for (String k : _keyPath) {
			next = data.getCompound(k);
			if (next == null) {
				next = new NBTTagCompound();
				data.setCompound(k, next);
			}
			data = next;
		}
		return next;
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
		data().remove(_key);
	}

	public abstract String getFormat();

	public List<String> getPossibleValues() {
		return null;
	}

}
