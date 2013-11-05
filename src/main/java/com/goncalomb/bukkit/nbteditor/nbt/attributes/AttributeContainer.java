package com.goncalomb.bukkit.nbteditor.nbt.attributes;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.bkglib.reflect.NBTTagListWrapper;

public final class AttributeContainer {
	
	private LinkedHashMap<AttributeType, Attribute> _attributes = new LinkedHashMap<AttributeType, Attribute>();
	
	public static AttributeContainer fromNBT(NBTTagListWrapper data) {
		AttributeContainer container = new AttributeContainer();
		for (Object attr : data.getAsArray()) {
			container.setAttribute(Attribute.fromNBT((NBTTagCompoundWrapper) attr));
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
	
	public NBTTagListWrapper toNBT() {
		NBTTagListWrapper data = new NBTTagListWrapper();
		for (Attribute attribute : _attributes.values()) {
			data.add(attribute.toNBT());
		}
		return data;
	}
	
}
