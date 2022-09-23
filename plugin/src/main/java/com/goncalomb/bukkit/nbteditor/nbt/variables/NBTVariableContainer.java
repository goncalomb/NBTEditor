package com.goncalomb.bukkit.nbteditor.nbt.variables;

import java.util.Set;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public final class NBTVariableContainer {

	private NBTUnboundVariableContainer _unbound;
	private NBTTagCompound _data;

	NBTVariableContainer(NBTUnboundVariableContainer unbound, NBTTagCompound data) {
		_unbound = unbound;
		_data = data;
	}

	public boolean hasVariable(String name) {
		return _unbound.hasVariable(name);
	}

	public String getName() {
		return _unbound.getName();
	}

	public Set<String> getVariableNames() {
		return _unbound.getVariableNames();
	}

	public NBTVariable getVariable(String name) {
		return _unbound.getVariable(name, _data);
	}

}
