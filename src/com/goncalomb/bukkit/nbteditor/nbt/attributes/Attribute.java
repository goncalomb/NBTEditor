package com.goncalomb.bukkit.nbteditor.nbt.attributes;

import java.util.ArrayList;
import java.util.List;

import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.reflect.NBTTagListWrapper;

public final class Attribute {
	
	private AttributeType _type;
	private double _base;
	private List<Modifier> _modifiers = new ArrayList<Modifier>();
	
	public static Attribute fromNBT(NBTTagCompoundWrapper data) {
		Attribute attribute = new Attribute(AttributeType.getByInternalName(data.getString("Name")), data.getDouble("Base"));
		if (data.hasKey("Modifiers")) {
			Object[] modifiersData = data.getListAsArray("Modifiers");
			attribute._modifiers = new ArrayList<Modifier>(modifiersData.length);
			for (Object mod : modifiersData) {
				attribute.addModifier(Modifier.fromNBT((NBTTagCompoundWrapper) mod));
			}
		}
		return attribute;
	}
	
	public Attribute(AttributeType type, double base) {
		_type = type;
		setBase(base);
	}
	
	public AttributeType getType() {
		return _type;
	}
	
	public double getMin() {
		return _type.getMin();
	}
	
	public double getMax() {
		return _type.getMax();
	}
	
	public double getBase() {
		return _base;
	}
	
	public void setBase(double value) {
		_base = Math.max(Math.min(value, getMax()), getMin());
	}
	
	public List<Modifier> getModifiers() {
		return new ArrayList<Modifier>(_modifiers);
	}
	
	public void setModifiers(List<Modifier> modifiers) {
		_modifiers.clear();
		if (modifiers != null) {
			_modifiers.addAll(modifiers);
		}
	}
	
	public void addModifier(Modifier modifier) {
		_modifiers.add(modifier);
	}
	
	public Modifier removeModifier(int index) {
		return _modifiers.remove(index);
	}
	
	public NBTTagCompoundWrapper toNBT() {
		NBTTagCompoundWrapper data = new NBTTagCompoundWrapper();
		data.setString("Name", _type._internalName);
		data.setDouble("Base", _base);
		if (_modifiers.size() > 0) {
			NBTTagListWrapper modifiersData = new NBTTagListWrapper();
			for (Modifier modifier : _modifiers) {
				modifiersData.add(modifier.toNBT());
			}
			data.setList("Modifiers", modifiersData);
		}
		return data;
	}
	
}
