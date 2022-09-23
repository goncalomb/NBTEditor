/*
 * Copyright (C) 2013-2018 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.mylib.reflect;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public final class NBTTagListAdapter_v1_18_R1 implements NBTTagListAdapter {
	private static Field _listField;
	protected static Class<?> _nbtTagListClass;

	public NBTTagListAdapter_v1_18_R1() throws Exception {
		_nbtTagListClass = Class.forName("net.minecraft.nbt.NBTTagList");
		_listField = _nbtTagListClass.getDeclaredField("c");
		_listField.setAccessible(true);
	}

	public Object newInternalInstance() {
		return new ListTag();
	}

	@SuppressWarnings("unchecked")
	public List<Object> getList(Object handle) {
		try {
			return (List<Object>) _listField.get(handle);
		} catch (Exception e) {
			throw new RuntimeException("Error while getting field value " + _listField.getName() + " of class " + _listField.getDeclaringClass().getName() + ".", e);
		}
	}

	public void add(Object handle, Object value) {
		Tag addHandle = (Tag) NBTTypes.toInternal(value);
		ListTag list = ((ListTag) handle);
		list.add(addHandle);
	}
}
