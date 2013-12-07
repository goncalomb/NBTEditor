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
import java.util.Collection;

public final class NBTTagCompoundWrapper extends NBTBaseWrapper {
	
	private static Method _getByte;
	private static Method _getShort;
	private static Method _getInt;
	private static Method _getLong;
	private static Method _getFloat;
	private static Method _getDouble;
	private static Method _getString;
	private static Method _getCompound;
	private static Method _getList;
	private static Method _get;
	
	private static Method _setByte;
	private static Method _setShort;
	private static Method _setInt;
	private static Method _setLong;
	private static Method _setDouble;
	private static Method _setFloat;
	private static Method _setString;
	private static Method _setCompound;
	private static Method _set;
	
	private static Method _hasKey;
	private static Method _remove;
	private static Method _c; // Gets a Collection with all NBTBase objects.
	
	private static Method _tagCompoundSerialize;
	private static Method _tagCompoundUnserialize;
	
	static void prepareReflectionz() throws SecurityException, NoSuchMethodException {
		_getByte = _nbtTagCompoundClass.getMethod("getByte", String.class);
		_getShort = _nbtTagCompoundClass.getMethod("getShort", String.class);
		_getInt = _nbtTagCompoundClass.getMethod("getInt", String.class);
		_getLong = _nbtTagCompoundClass.getMethod("getLong", String.class);
		_getFloat = _nbtTagCompoundClass.getMethod("getFloat", String.class);
		_getDouble = _nbtTagCompoundClass.getMethod("getDouble", String.class);
		_getString = _nbtTagCompoundClass.getMethod("getString", String.class);
		_getCompound = _nbtTagCompoundClass.getMethod("getCompound", String.class);
		_getList = _nbtTagCompoundClass.getMethod("getList", String.class);
		_get = _nbtTagCompoundClass.getMethod("get", String.class);
		
		_setByte = _nbtTagCompoundClass.getMethod("setByte", String.class, byte.class);
		_setShort = _nbtTagCompoundClass.getMethod("setShort", String.class, short.class);
		_setInt = _nbtTagCompoundClass.getMethod("setInt", String.class, int.class);
		_setLong = _nbtTagCompoundClass.getMethod("setLong", String.class, long.class);
		_setFloat = _nbtTagCompoundClass.getMethod("setFloat", String.class, float.class);
		_setDouble = _nbtTagCompoundClass.getMethod("setDouble", String.class, double.class);
		_setString = _nbtTagCompoundClass.getMethod("setString", String.class, String.class);
		_setCompound = _nbtTagCompoundClass.getMethod("setCompound", String.class, _nbtTagCompoundClass);
		_set = _nbtTagCompoundClass.getMethod("set", String.class, _nbtBaseClass);
		
		_hasKey = _nbtTagCompoundClass.getMethod("hasKey", String.class);
		_remove = _nbtTagCompoundClass.getMethod("remove", String.class);
		_c = _nbtTagCompoundClass.getMethod("c");
		
		Class<?> nbtCompressedStreamToolsClass = BukkitReflect.getMinecraftClass("NBTCompressedStreamTools");
		_tagCompoundSerialize = nbtCompressedStreamToolsClass.getMethod("a", _nbtTagCompoundClass);
		_tagCompoundUnserialize = nbtCompressedStreamToolsClass.getMethod("a", byte[].class);
	}
	
	public NBTTagCompoundWrapper() {
		super(BukkitReflect.newInstance(_nbtTagCompoundClass));
	}
	
	NBTTagCompoundWrapper(Object nbtTagCompoundObject) {
		super(nbtTagCompoundObject);
	}
	
	public byte getByte(String key) {
		return (Byte) invokeMethod(_getByte, key);
	}
	
	public short getShort(String key) {
		return (Short) invokeMethod(_getShort, key);
	}
	
	public int getInt(String key) {
		return (Integer) invokeMethod(_getInt, key);
	}
	
	public long getLong(String key) {
		return (Long) invokeMethod(_getLong, key);
	}
	
	public float getFloat(String key) {
		return (Float) invokeMethod(_getFloat, key);
	}
	
	public Double getDouble(String key) {
		return (Double) invokeMethod(_getDouble, key);
	}
	
	public String getString(String key) {
		return (String) invokeMethod(_getString, key);
	}
	
	public NBTTagCompoundWrapper getCompound(String key) {
		if (hasKey(key)) {
			return new NBTTagCompoundWrapper(invokeMethod(_getCompound, key));
		}
		return null;
	}
	
	public NBTTagListWrapper getList(String key) {
		if (hasKey(key)) {
			return new NBTTagListWrapper(invokeMethod(_getList, key));
		}
		return null;
	}
	
	public Object[] getListAsArray(String key) {
		NBTTagListWrapper list = getList(key);
		return (list == null ? null : list.getAsArray());
	}
	
	public Object get(String key) {
		return NBTTagTypeHandler.getObjectFromTag(invokeMethod(_get, key));
	}
	
	public void setByte(String key, byte value) {
		invokeMethod(_setByte, key, value);
	}
	
	public void setShort(String key, short value) {
		invokeMethod(_setShort, key, value);
	}
	
	public void setInt(String key, int value) {
		invokeMethod(_setInt, key, value);
	}
	
	public void setLong(String key, long value) {
		invokeMethod(_setLong, key, value);
	}
	
	public void setFloat(String key, float value) {
		invokeMethod(_setFloat, key, value);
	}
	
	public void setDouble(String key, double value) {
		invokeMethod(_setDouble, key, value);
	}
	
	public void setString(String key, String value) {
		invokeMethod(_setString, key, value);
	}
	
	public void setCompound(String key, NBTTagCompoundWrapper value) {
		invokeMethod(_setCompound, key, value._nbtBaseObject);
	}
	
	public void setList(String key, NBTTagListWrapper value) {
		invokeMethod(_set, key, value._nbtBaseObject);
	}
	
	public void setList(String key, Object... objects) {
		invokeMethod(_set, key, (new NBTTagListWrapper(objects))._nbtBaseObject);
	}
	
	public void set(String key, Object value) {
		invokeMethod(_set, key, NBTTagTypeHandler.getTagFromObject(value));
	}
	
	public boolean hasKey(String key) {
		return (Boolean) invokeMethod(_hasKey, key);
	}
	
	public void remove(String key) {
		invokeMethod(_remove, key);
	}
	
	public static NBTTagCompoundWrapper unserialize(byte[] data) {
		prepareReflection();
		try {
			return new NBTTagCompoundWrapper(_tagCompoundUnserialize.invoke(null, data));
		} catch (Exception e) {
			throw new Error("Error while unserializing NBTTagCompound.", e);
		}
	}
	
	public byte[] serialize() {
		try {
			return (byte[]) _tagCompoundSerialize.invoke(null, _nbtBaseObject);
		} catch (Exception e) {
			throw new Error("Error while serializing NBTTagCompound.", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void merge(NBTTagCompoundWrapper other) {
		Collection<Object> tags = (Collection<Object>) BukkitReflect.invokeMethod(other._nbtBaseObject, _c);
		for (Object tag : tags) {
			invokeMethod(_set, NBTBaseWrapper.getName(tag), NBTBaseWrapper.clone(tag));
		}
	}
	
	@Override
	public NBTTagCompoundWrapper clone() {
		return (NBTTagCompoundWrapper) super.clone();
	}
	
}
