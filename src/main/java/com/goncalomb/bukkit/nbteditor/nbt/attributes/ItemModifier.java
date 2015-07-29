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
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public final class ItemModifier extends Modifier {
	
	private AttributeType _attributeType;
	
	public static ItemModifier fromNBT(NBTTagCompound data) {
		return new ItemModifier(AttributeType.getByInternalName(data.getString("AttributeName")), data.getString("Name"), data.getDouble("Amount"), data.getInt("Operation"), new UUID(data.getLong("UUIDMost"), data.getLong("UUIDLeast")));
	}
	
	public static List<ItemModifier> getItemStackModifiers(ItemStack item) {
		NBTTagCompound tag = NBTUtils.getItemStackTag(item);
		if (tag.hasKey("AttributeModifiers")) {
			Object[] modifiersData = tag.getListAsArray("AttributeModifiers");
			List<ItemModifier> modifiers = new ArrayList<ItemModifier>(modifiersData.length);
			for (Object data : modifiersData) {
				modifiers.add(fromNBT((NBTTagCompound) data));
			}
			return modifiers;
		}
		return new ArrayList<ItemModifier>();
	}
	
	public static void setItemStackModifiers(ItemStack item, List<ItemModifier> modifiers) {
		NBTTagList modifiersData = new NBTTagList();
		for (ItemModifier modifier : modifiers) {
			modifiersData.add(modifier.toNBT());
		}
		NBTTagCompound tag = NBTUtils.getItemStackTag(item);
		tag.setList("AttributeModifiers", modifiersData);
		NBTUtils.setItemStackTag(item, tag);
	}
	
	public ItemModifier(AttributeType attributeType, String name, double amount, int operation) {
		super(name, amount, operation);
		_attributeType = attributeType;
	}
	
	public ItemModifier(AttributeType attributeType, String name, double amount, int operation, UUID uuid) {
		super(name, amount, operation, uuid);
		_attributeType = attributeType;
	}
	
	public AttributeType getAttributeType() {
		return _attributeType;
	}
	
	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound data = super.toNBT();
		data.setString("AttributeName", _attributeType._internalName);
		return data;
	}
	
}
