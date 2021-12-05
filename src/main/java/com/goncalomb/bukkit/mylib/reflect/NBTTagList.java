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

public final class NBTTagList extends NBTBase {

	private static Field _typeField;
	private static Field _listField;
	List<Object> _list;

	static void prepareReflectionz() throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		_typeField = _nbtTagListClass.getDeclaredField("w");
		_typeField.setAccessible(true);
		_listField = _nbtTagListClass.getDeclaredField("c");
		_listField.setAccessible(true);
	}

	public NBTTagList() {
		this(BukkitReflect.newInstance(_nbtTagListClass));
	}

	@SuppressWarnings("unchecked")
	NBTTagList(Object handle) {
		super(handle);
		_list = (List<Object>) BukkitReflect.getFieldValue(handle, _listField);
	}

	public NBTTagList(Object... values) {
		this(BukkitReflect.newInstance(_nbtTagListClass));
		for (Object value : values) {
			add(value);
		}
	}

	public Object get(int index) {
		return NBTTypes.fromInternal(_list.get(index));
	}

	public void add(Object value) {
		Object handle = NBTTypes.toInternal(value);
		BukkitReflect.setFieldValue(_handle, _typeField, NBTBase.getTypeId(handle));
		_list.add(handle);
	}

	public Object remove(int index) {
		return NBTTypes.fromInternal(_list.remove(index));
	}

	public int size() {
		return _list.size();
	}

	public Object[] getAsArray() {
		int length = size();
		Object[] result = new Object[length];
		for (int i = 0; i < length; ++i) {
			result[i] = get(i);
		}
		return result;
	}

	@Override
	public NBTTagList clone() {
		return (NBTTagList) super.clone();
	}

}
