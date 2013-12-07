/*
 * Copyright (C) 2013 - Gonçalo Baltazar <http://goncalomb.com>
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

import java.lang.reflect.Method;

public final class NBTTagListWrapper extends NBTBaseWrapper {
	
	private static Method _get;
	private static Method _add;
	private static Method _size;
	
	static void prepareReflectionz() throws SecurityException, NoSuchMethodException {
		_get = _nbtTagListClass.getMethod("get", int.class);
		_add = _nbtTagListClass.getMethod("add", _nbtBaseClass);
		_size = _nbtTagListClass.getMethod("size");
	}
	
	public NBTTagListWrapper() {
		super(BukkitReflect.newInstance(_nbtTagListClass));
	}
	
	NBTTagListWrapper(Object nbtTagListObject) {
		super(nbtTagListObject);
	}
	
	public NBTTagListWrapper(Object... values) {
		super(BukkitReflect.newInstance(_nbtTagListClass));
		for (Object value : values) {
			add(value);
		}
	}
	
	public Object get(int index) {
		return NBTTagTypeHandler.getObjectFromTag(invokeMethod(_get, index));
	}
	
	public void add(Object value) {
		invokeMethod(_add, NBTTagTypeHandler.getTagFromObject(value));
	}
	
	public int size() {
		return (Integer) invokeMethod(_size);
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
	public NBTTagListWrapper clone() {
		return (NBTTagListWrapper) super.clone();
	}
	
}
