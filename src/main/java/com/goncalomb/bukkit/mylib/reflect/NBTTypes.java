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

package com.goncalomb.bukkit.mylib.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.commons.lang.ClassUtils;

final class NBTTypes {
	
	private static HashMap<Class<?>, NBTTypes> _innerTypeMap = new HashMap<Class<?>, NBTTypes>();;
	private static HashMap<Class<?>, NBTTypes> _outerTypeMap = new HashMap<Class<?>, NBTTypes>();;
	
	private Class<?> _class;
	private Constructor<?> _contructor;
	private Field _data;
	private Class<?> _dataType;
	
	public static void prepareReflection() throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		registerNew("NBTTagByte");
		registerNew("NBTTagShort");
		registerNew("NBTTagInt");
		registerNew("NBTTagLong");
		registerNew("NBTTagFloat");
		registerNew("NBTTagDouble");
		registerNew("NBTTagString");
	}
	
	private static void registerNew(String tagClassName) throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		NBTTypes handler = new NBTTypes(tagClassName);
		_innerTypeMap.put((handler._dataType.isPrimitive() ? ClassUtils.primitiveToWrapper(handler._dataType) : handler._dataType), handler);
		_outerTypeMap.put(handler._class, handler);
	}
	
	// Converts from MyLib tags, primitives and strings to internal Minecraft tags.
	public static Object toInternal(Object object) {
		if (object instanceof NBTBase) {
			return ((NBTBase) object)._handle;
		} else {
			NBTTypes handler = _innerTypeMap.get(object.getClass());
			if (handler != null) {
				return handler.wrap(object);
			} else {
				throw new RuntimeException(object.getClass() + " is not a valid NBTTag type.");
			}
		}
	}
	
	// Converts internal Minecraft tags to MyLib tags, primitives and strings.
	public static Object fromInternal(Object object) {
		if (object == null) {
			return null;
		}
		NBTTypes handler = _outerTypeMap.get(object.getClass());
		if (handler != null) {
			return handler.unwrap(object);
		} else {
			return NBTBase.wrap(object);
		}
	}
	
	private NBTTypes(String tagClassName) throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		_class = BukkitReflect.getMinecraftClass(tagClassName);
		_data = _class.getDeclaredField("data");
		_data.setAccessible(true);
		_dataType = _data.getType();
		_contructor = _class.getConstructor(_dataType);
	}
	
	// Wraps primitives and strings with Minecraft tags.
	private Object wrap(Object innerObject) {
		return BukkitReflect.newInstance(_contructor, innerObject);
	}
	
	// Unwraps primitives and strings from Minecraft tags.
	private Object unwrap(Object tagObject) {
		return BukkitReflect.getFieldValue(tagObject, _data);
	}
	
}
