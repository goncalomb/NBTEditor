package com.goncalomb.bukkit.nbteditor.nbt;

import java.util.ArrayList;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.nbteditor.nbt.variables.NBTUnboundVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variables.NBTVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.NBTVariableContainer;

public abstract class BaseNBT {

	private String _id;
	protected NBTTagCompound _data;
	protected NBTUnboundVariableContainer _container;

	public BaseNBT(NBTTagCompound data) {
		_data = data;
		_id = _data.getString("id");
		if (_id == null) {
			throw new RuntimeException("Invalid data id.");
		}
		_container = getVariableContainer(_id);
		if (_container == null) {
			throw new RuntimeException("Invalid NBT object.");
		}
	}

	protected abstract NBTUnboundVariableContainer getVariableContainer(String id);

	public NBTVariableContainer[] getAllVariables() {
		ArrayList<NBTVariableContainer> list = new ArrayList<NBTVariableContainer>(3);
		NBTUnboundVariableContainer container = _container;
		while (container != null) {
			list.add(container.bind(_data));
			container = container.getParent();
		}
		return list.toArray(new NBTVariableContainer[0]);
	}

	public NBTVariable getVariable(String name) {
		name = name.toLowerCase();
		NBTUnboundVariableContainer container = _container;
		while (container != null) {
			if (container.hasVariable(name)) {
				return container.getVariable(name, _data);
			}
			container = container.getParent();
		}
		return null;
	}

	public void save() {
		throw new UnsupportedOperationException();
	}

}
