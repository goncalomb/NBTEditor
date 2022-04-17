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

import java.util.List;
import java.util.logging.Logger;

public final class NBTTagList extends NBTBase {

	private static NBTTagListAdapter adapter = null;

	public static void prepareReflection(Class<?> serverClass, Logger logger) throws Exception {
		String packageName = serverClass.getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf('.') + 1);

		Class<?> clazz = Class.forName("com.goncalomb.bukkit.mylib.reflect.NBTTagListAdapter_" + version);
		adapter = (NBTTagListAdapter) clazz.getConstructor().newInstance();
		logger.info("Loaded NBTTagList adapter for " + version);
	}

	private final List<Object> _list;

	public NBTTagList() {
		this(adapter.newInternalInstance());
	}

	NBTTagList(Object handle) {
		super(handle);
		_list = adapter.getList(handle);
	}

	public NBTTagList(Object... values) {
		this(adapter.newInternalInstance());
		for (Object value : values) {
			add(value);
		}
	}

	public Object get(int index) {
		return NBTTypes.fromInternal(_list.get(index));
	}

	public void add(Object value) {
		ensureAdapter(adapter);
		adapter.add(_handle, value);
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

	private static void ensureAdapter(Object adapter) throws RuntimeException {
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
	}
}
