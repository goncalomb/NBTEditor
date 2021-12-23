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

public class NBTBase {

	private static boolean _isPrepared = false;

	protected static Class<?> _nbtBaseClass;
	protected static Class<?> _nbtTagCompoundClass;
	protected static Class<?> _nbtTagListClass;
	protected static Class<?> _nbtTagStringClass;

	private static Method _getTypeId;
	private static Method _clone;

	final Object _handle; // The wrapped Minecraft NBTBase instance.

	public static final void prepareReflection() {
		if (!_isPrepared) {
			_nbtBaseClass = BukkitReflect.getMinecraftClass("nbt.NBTBase");
			_nbtTagCompoundClass = BukkitReflect.getMinecraftClass("nbt.NBTTagCompound");
			_nbtTagListClass = BukkitReflect.getMinecraftClass("nbt.NBTTagList");
			_nbtTagStringClass = BukkitReflect.getMinecraftClass("nbt.NBTTagString");
			try {
				_getTypeId = _nbtBaseClass.getMethod("getTypeId");
				_clone = _nbtBaseClass.getMethod("clone");
				NBTTagCompound.prepareReflectionz();
				NBTTagList.prepareReflectionz();
				NBTTypes.prepareReflection();
				NBTUtils.prepareReflection();
			} catch (Exception e) {
				throw new RuntimeException("Error while preparing NBT wrapper classes.", e);
			}
			_isPrepared = true;
		}
	}

	// Wraps any Minecraft tags in MyLib tags.
	// Primitives and strings are wrapped with NBTBase.
	protected static final NBTBase wrap(Object object) {
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

	// Helper method for NBTTagCompoundWrapper.merge().
	// Clones any internal Minecraft tags.
	protected static final Object clone(Object nbtBaseObject) {
		return BukkitReflect.invokeMethod(nbtBaseObject, _clone);
	}

	protected NBTBase(Object handle) {
		_handle = handle;
	}

	protected final Object invokeMethod(Method method, Object... args) {
		return BukkitReflect.invokeMethod(_handle, method, args);
	}

	static byte getTypeId(Object handle) {
		return (Byte) BukkitReflect.invokeMethod(handle, _getTypeId);
	}

	public NBTBase clone() {
		return wrap(invokeMethod(_clone));
	}

	@Override
	public String toString() {
		return _handle.toString();
	}

}
