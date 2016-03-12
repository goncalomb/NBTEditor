/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
 *
 * This file is part of NBTEditor.
 *
 * NBTEditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NBTEditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NBTEditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.nbteditor.nbt.attributes;

import java.util.ArrayList;
import java.util.List;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;

public final class Attribute {

	private AttributeType _type;
	private double _base;
	private List<Modifier> _modifiers = new ArrayList<Modifier>();

	public static Attribute fromNBT(NBTTagCompound data) {
		Attribute attribute = new Attribute(AttributeType.getByInternalName(data.getString("Name")), data.getDouble("Base"));
		if (data.hasKey("Modifiers")) {
			Object[] modifiersData = data.getListAsArray("Modifiers");
			attribute._modifiers = new ArrayList<Modifier>(modifiersData.length);
			for (Object mod : modifiersData) {
				attribute.addModifier(Modifier.fromNBT((NBTTagCompound) mod));
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

	public NBTTagCompound toNBT() {
		NBTTagCompound data = new NBTTagCompound();
		data.setString("Name", _type._internalName);
		data.setDouble("Base", _base);
		if (_modifiers.size() > 0) {
			NBTTagList modifiersData = new NBTTagList();
			for (Modifier modifier : _modifiers) {
				modifiersData.add(modifier.toNBT());
			}
			data.setList("Modifiers", modifiersData);
		}
		return data;
	}

}
