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

public final class NBTTagListAdapter_v1_16_R3 implements NBTTagListAdapter {

	private static Field _typeField;
	private static Field _listField;
	protected static Class<?> _nbtTagListClass;

	public NBTTagListAdapter_v1_16_R3() throws Exception {
		_nbtTagListClass = BukkitReflectAdapter_v1_16_R3.getMinecraftClass("NBTTagList");
		_typeField = _nbtTagListClass.getDeclaredField("type");
		_typeField.setAccessible(true);
		_listField = _nbtTagListClass.getDeclaredField("list");
		_listField.setAccessible(true);
	}

	public Object newInternalInstance() {
		return BukkitReflectAdapter_v1_16_R3.newInstance(_nbtTagListClass);
	}

	@SuppressWarnings("unchecked")
	public List<Object> getList(Object handle) {
		return (List<Object>) BukkitReflectAdapter_v1_16_R3.getFieldValue(handle, _listField);
	}

	public void add(Object handle, Object value) {
		Object addHandle = NBTTypes.toInternal(value);
		BukkitReflectAdapter_v1_16_R3.setFieldValue(handle, _typeField, NBTBase.getTypeId(addHandle));
		getList(handle).add(addHandle);
	}
}
