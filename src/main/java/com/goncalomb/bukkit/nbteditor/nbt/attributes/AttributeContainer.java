/*
 * Copyright (C) 2013, 2014 - Gonçalo Baltazar <http://goncalomb.com>
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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.bkglib.reflect.NBTTagList;

public final class AttributeContainer {
	
	private LinkedHashMap<AttributeType, Attribute> _attributes = new LinkedHashMap<AttributeType, Attribute>();
	
	public static AttributeContainer fromNBT(NBTTagList data) {
		AttributeContainer container = new AttributeContainer();
		for (Object attr : data.getAsArray()) {
			container.setAttribute(Attribute.fromNBT((NBTTagCompound) attr));
		}
		return container;
	}
	
	public Attribute getAttribute(AttributeType type) {
		return _attributes.get(type);
	}
	
	public void setAttribute(Attribute attribute) {
		_attributes.put(attribute.getType(), attribute);
	}
	
	public Attribute removeAttribute(AttributeType type) {
		return _attributes.remove(type);
	}
	
	public int size() {
		return _attributes.size();
	}
	
	public Collection<Attribute> values() {
		return Collections.unmodifiableCollection(_attributes.values());
	}
	
	public Collection<AttributeType> types() {
		return Collections.unmodifiableCollection(_attributes.keySet());
	}
	
	public NBTTagList toNBT() {
		NBTTagList data = new NBTTagList();
		for (Attribute attribute : _attributes.values()) {
			data.add(attribute.toNBT());
		}
		return data;
	}
	
}
