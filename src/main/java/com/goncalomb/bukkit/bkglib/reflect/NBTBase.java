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

import java.lang.reflect.Method;
import java.util.Map.Entry;

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
			_nbtBaseClass = BukkitReflect.getMinecraftClass("NBTBase");
			_nbtTagCompoundClass = BukkitReflect.getMinecraftClass("NBTTagCompound");
			_nbtTagListClass = BukkitReflect.getMinecraftClass("NBTTagList");
			_nbtTagStringClass = BukkitReflect.getMinecraftClass("NBTTagString");
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
	
	// Wraps any Minecraft tags in BKgLib tags.
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
	
	private String toStringAny(Object object) {
		// Some "dirty" code to fix the internal Mojanson encoder.
		StringBuilder buffer = new StringBuilder();
		if (_nbtTagCompoundClass.isInstance(object)) {
			// We need this to force using this method on all compound values.
			buffer.append("{");
			int i = 0;
			for (Entry<String, Object> entry : (new NBTTagCompound(object))._map.entrySet()) {
				if (i++ != 0) buffer.append(",");
				buffer.append(entry.getKey());
				buffer.append(":");
				buffer.append(toStringAny(entry.getValue()));
			}
			buffer.append("}");
		} else if (_nbtTagListClass.isInstance(object)) {
			// We need this to force using this method on all list values.
			// Mojang, WHY do lists need to be numbered!?!?.
			// Strings with ':' on unnumbered lists break the parser.
			// The numbers don't even determine the position on the list.
			// Le sigh.
			buffer.append("[");
			int i = 0;
			for (Object obj : (new NBTTagList(object))._list) {
				if (i != 0) buffer.append(",");
				buffer.append(i++);
				buffer.append(":");
				buffer.append(toStringAny(obj));
			}
			buffer.append("]");
		} else if (_nbtTagStringClass.isInstance(object)) {
			// This is the actual fix.
			// The " character on Strings needs to be escaped.
			String str = (String) NBTTypes.fromInternal(object);
			buffer.append('"');
			int j = 0, l = str.length();
			for (int i = 0; i < l; ++i) {
				char c = str.charAt(i);
				// There is a problem with the internal decoder, it does not recognize \\ as an escaped \.
				// It's not possible to encode strings that end with \. Mojang, fix it.
				if (/*c == '\' || */c == '"') {
					buffer.append(str.substring(j, i));
					buffer.append("\\" +  c);
					j = i + 1;
				}
			}
			if (j != l) {
				buffer.append(str.substring(j, l));
			}
			buffer.append('"');
		} else {
			return object.toString();
		}
		return buffer.toString();
	}
	
	@Override
	public String toString() {
		return toStringAny(_handle);
	}
	
}
