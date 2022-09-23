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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public final class NBTTagCompoundAdapter_v1_16_R3 implements NBTTagCompoundAdapter {

	protected static Class<?> _nbtTagListClass;
	protected static Class<?> _nbtTagCompoundClass;
	private static Method _getByte;
	private static Method _getShort;
	private static Method _getInt;
	private static Method _getLong;
	private static Method _getFloat;
	private static Method _getDouble;
	private static Method _getString;
	private static Method _getByteArray;
	private static Method _getIntArray;
	private static Method _getLongArray;
	private static Field _mapField;

	private static Method _tagSerializeStream;
	private static Method _tagUnserializeStream;
	private static Method _parseMojangson;

	public NBTTagCompoundAdapter_v1_16_R3() throws Exception {
		_nbtTagCompoundClass = BukkitReflectAdapter_v1_16_R3.getMinecraftClass("NBTTagCompound");
		_nbtTagListClass = BukkitReflectAdapter_v1_16_R3.getMinecraftClass("NBTTagList");
		_getByte = _nbtTagCompoundClass.getMethod("getByte", String.class);
		_getShort = _nbtTagCompoundClass.getMethod("getShort", String.class);
		_getInt = _nbtTagCompoundClass.getMethod("getInt", String.class);
		_getLong = _nbtTagCompoundClass.getMethod("getLong", String.class);
		_getFloat = _nbtTagCompoundClass.getMethod("getFloat", String.class);
		_getDouble = _nbtTagCompoundClass.getMethod("getDouble", String.class);
		_getString = _nbtTagCompoundClass.getMethod("getString", String.class);
		_getByteArray = _nbtTagCompoundClass.getMethod("getByteArray", String.class);
		_getIntArray = _nbtTagCompoundClass.getMethod("getIntArray", String.class);
		_getLongArray = _nbtTagCompoundClass.getMethod("getLongArray", String.class);
		_mapField = _nbtTagCompoundClass.getDeclaredField("map");
		_mapField.setAccessible(true);

		Class<?>_mojangsonParserClass = BukkitReflectAdapter_v1_16_R3.getMinecraftClass("MojangsonParser");
		_parseMojangson = _mojangsonParserClass.getMethod("parse", String.class);

		Class<?> nbtCompressedStreamToolsClass = BukkitReflectAdapter_v1_16_R3.getMinecraftClass("NBTCompressedStreamTools");
		_tagSerializeStream = nbtCompressedStreamToolsClass.getMethod("a", _nbtTagCompoundClass, OutputStream.class);
		_tagUnserializeStream = nbtCompressedStreamToolsClass.getMethod("a", InputStream.class);
	}

	public Object newInternalInstance() {
		return BukkitReflectAdapter_v1_16_R3.newInstance(_nbtTagCompoundClass);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getMap(Object handle) {
		return (Map<String, Object>) BukkitReflectAdapter_v1_16_R3.getFieldValue(handle, _mapField);
	}

	public byte getByte(Object handle, String key) {
		return (Byte) BukkitReflectAdapter_v1_16_R3.invokeMethod(handle, _getByte, key);
	}

	public short getShort(Object handle, String key) {
		return (Short) BukkitReflectAdapter_v1_16_R3.invokeMethod(handle, _getShort, key);
	}

	public int getInt(Object handle, String key) {
		return (Integer) BukkitReflectAdapter_v1_16_R3.invokeMethod(handle, _getInt, key);
	}

	public long getLong(Object handle, String key) {
		return (Long) BukkitReflectAdapter_v1_16_R3.invokeMethod(handle, _getLong, key);
	}

	public float getFloat(Object handle, String key) {
		return (Float) BukkitReflectAdapter_v1_16_R3.invokeMethod(handle, _getFloat, key);
	}

	public Double getDouble(Object handle, String key) {
		return (Double) BukkitReflectAdapter_v1_16_R3.invokeMethod(handle, _getDouble, key);
	}

	public String getString(Object handle, String key) {
		return (String) BukkitReflectAdapter_v1_16_R3.invokeMethod(handle, _getString, key);
	}

	public byte[] getByteArray(Object handle, String key) {
		return (byte[]) BukkitReflectAdapter_v1_16_R3.invokeMethod(handle, _getByteArray, key);
	}

	public int[] getIntArray(Object handle, String key) {
		return (int[]) BukkitReflectAdapter_v1_16_R3.invokeMethod(handle, _getIntArray, key);
	}

	public long[] getLongArray(Object handle, String key) {
		return (long[]) BukkitReflectAdapter_v1_16_R3.invokeMethod(handle, _getLongArray, key);
	}

	public NBTTagCompound getCompound(Object handle, String key) {
		Object obj = getMap(handle).get(key);
		if (obj != null && _nbtTagCompoundClass.isInstance(obj)) {
			return new NBTTagCompound(obj);
		}
		return null;
	}

	public NBTTagList getList(Object handle, String key) {
		Object obj = getMap(handle).get(key);
		if (obj != null && _nbtTagListClass.isInstance(obj)) {
			return new NBTTagList(obj);
		}
		return null;
	}

	public void set(Object handle, String key, Object value) {
		getMap(handle).put(key, NBTTypes.toInternal(value));
	}

	public void serialize(Object handle, OutputStream outputStream) throws IOException {
		BukkitReflectAdapter_v1_16_R3.invokeMethod(null, _tagSerializeStream, handle, outputStream);
	}

	public NBTTagCompound unserialize(InputStream inputStream) throws IOException {
		return new NBTTagCompound(BukkitReflectAdapter_v1_16_R3.invokeMethod(null, _tagUnserializeStream, inputStream));
	}

	public NBTTagCompound fromString(String mojangson) throws Exception {
		return new NBTTagCompound(BukkitReflectAdapter_v1_16_R3.invokeMethod(null, _parseMojangson, mojangson));
	}
}
