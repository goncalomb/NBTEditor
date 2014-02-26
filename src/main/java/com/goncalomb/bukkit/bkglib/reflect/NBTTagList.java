/*
 * Copyright (C) 2013, 2014 - Gonçalo Baltazar <http://goncalomb.com>
 *
 * This file is part of BKgLib.
 *
 * BKgLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BKgLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BKgLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.bkglib.reflect;

import java.lang.reflect.Field;
import java.util.List;

public final class NBTTagList extends NBTBase {
	
	private static Field _listField;
	List<Object> _list;
	
	static void prepareReflectionz() throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		_listField = _nbtTagListClass.getDeclaredField("list");
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
		_list.add(NBTTypes.toInternal(value));
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
