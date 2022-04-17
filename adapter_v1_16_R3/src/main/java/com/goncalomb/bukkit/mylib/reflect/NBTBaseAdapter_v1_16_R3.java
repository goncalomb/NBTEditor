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

import java.lang.reflect.Method;

public class NBTBaseAdapter_v1_16_R3 implements NBTBaseAdapter {

	protected static Class<?> _nbtBaseClass;
	protected static Class<?> _nbtTagCompoundClass;
	protected static Class<?> _nbtTagListClass;
	protected static Class<?> _nbtTagStringClass;

	private static Method _getTypeId;
	private static Method _clone;

	public NBTBaseAdapter_v1_16_R3() {
		_nbtBaseClass = BukkitReflect.getMinecraftClass("NBTBase");
		_nbtTagCompoundClass = BukkitReflect.getMinecraftClass("NBTTagCompound");
		_nbtTagListClass = BukkitReflect.getMinecraftClass("NBTTagList");
		_nbtTagStringClass = BukkitReflect.getMinecraftClass("NBTTagString");
		try {
			_getTypeId = _nbtBaseClass.getMethod("getTypeId");
			_clone = _nbtBaseClass.getMethod("clone");
		} catch (Exception e) {
			throw new RuntimeException("Error while preparing NBT wrapper classes.", e);
		}
	}

	@Override
	public NBTBase wrap(Object object) {
		if (_nbtTagCompoundClass.isInstance(object)) {
			return new NBTTagCompound(object);
		} else if (_nbtTagListClass.isInstance(object)) {
			return new NBTTagList(object);
		} else if (_nbtBaseClass.isInstance(object)) {
			return new NBTBase(object);
		} else {
			throw new RuntimeException(object.getClass() + " is not a valid NBT tag type.");
		}
	}

	@Override
	public Object clone(Object nbtBaseObject) {
		return BukkitReflect.invokeMethod(nbtBaseObject, _clone);
	}

	@Override
	public byte getTypeId(Object handle) {
		return (Byte) BukkitReflect.invokeMethod(handle, _getTypeId);
	}
}
