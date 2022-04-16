package com.goncalomb.bukkit.nbteditor.nbt.variables;

import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;

public abstract class ListVariable extends NBTVariable {

	public ListVariable(String key) {
		super(key);
	}

	@Override
	public boolean set(String value, Player player) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String get() {
		NBTTagList list = data().getList(_key);
		return (list == null ? null : list.toString());
	}

	protected abstract boolean addItem(NBTTagList list, String value, Player player);

	public boolean add(String value, Player player) {
		NBTTagCompound data = data();
		NBTTagList list = data.getList(_key);
		if (list == null) {
			list = new NBTTagList();
			if (!addItem(list, value, player)) {
				return false;
			}
			data.setList(_key, list);
			return true;
		}
		return addItem(list, value, player);
	}

	public boolean remove(int index) {
		NBTTagList list = data().getList(_key);
		if (list != null && index < list.size()) {
			list.remove(index);
			if (list.size() == 0) {
				clear();
			}
			return true;
		}
		return false;
	}

}
