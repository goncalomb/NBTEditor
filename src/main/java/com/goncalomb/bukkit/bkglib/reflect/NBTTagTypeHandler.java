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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.commons.lang.ClassUtils;

final class NBTTagTypeHandler {
	
	private static HashMap<Class<?>, NBTTagTypeHandler> _innerTypeMap;
	private static HashMap<Class<?>, NBTTagTypeHandler> _outerTypeMap;
	
	private Class<?> _class;
	private Constructor<?> _contructor;
	private Field _data;
	private Class<?> _dataType;
	
	public static void prepareReflection() throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		_innerTypeMap = new HashMap<Class<?>, NBTTagTypeHandler>();
		_outerTypeMap = new HashMap<Class<?>, NBTTagTypeHandler>();
		registerNew("NBTTagByte");
		registerNew("NBTTagShort");
		registerNew("NBTTagInt");
		registerNew("NBTTagLong");
		registerNew("NBTTagFloat");
		registerNew("NBTTagDouble");
		registerNew("NBTTagString");
	}
	
	private static void registerNew(String tagClassName) throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		NBTTagTypeHandler handler = new NBTTagTypeHandler(tagClassName);
		_innerTypeMap.put((handler._dataType.isPrimitive() ? ClassUtils.primitiveToWrapper(handler._dataType) : handler._dataType), handler);
		_outerTypeMap.put(handler._class, handler);
	}
	
	public static Object getTagFromObject(Object object) {
		if (object instanceof NBTBaseWrapper) {
			return ((NBTBaseWrapper) object)._nbtBaseObject;
		} else {
			NBTTagTypeHandler handler = _innerTypeMap.get(object.getClass());
			if (handler != null) {
				return handler.wrapWithTag(object);
			} else {
				throw new Error(object.getClass().getSimpleName() + " is not a valid NBTTag type.");
			}
		}
	}
	
	public static Object getObjectFromTag(Object tagObject) {
		if (tagObject == null) {
			return null;
		}
		NBTTagTypeHandler handler = _outerTypeMap.get(tagObject.getClass());
		if (handler != null) {
			return handler.unwrapTag(tagObject);
		} else {
			return NBTBaseWrapper.wrap(tagObject);
		}
	}
	
	private NBTTagTypeHandler(String tagClassName) throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		_class = BukkitReflect.getMinecraftClass(tagClassName);
		_data = _class.getDeclaredField("data");
		_data.setAccessible(true);
		_dataType = _data.getType();
		_contructor = _class.getConstructor(_dataType);
	}
	
	private Object wrapWithTag(Object innerObject) {
		return BukkitReflect.newInstance(_contructor, innerObject);
	}
	
	private Object unwrapTag(Object tagObject) {
		return BukkitReflect.getFieldValue(tagObject, _data);
	}
	
}
