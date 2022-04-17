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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

public final class NBTTagCompound extends NBTBase {

	private static NBTTagCompoundAdapter adapter = null;

	public static void prepareReflection(Class<?> serverClass, Logger logger) throws Exception {
		String packageName = serverClass.getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf('.') + 1);

		Class<?> clazz = Class.forName("com.goncalomb.bukkit.mylib.reflect.NBTTagCompoundAdapter_" + version);
		adapter = (NBTTagCompoundAdapter) clazz.getConstructor().newInstance();
		logger.info("Loaded NBTTagCompound adapter for " + version);
	}

	private final Map<String, Object> _map;

	public NBTTagCompound() {
		this(adapter.newInternalInstance());
	}

	public NBTTagCompound(Object handle) {
		super(handle);
		_map = adapter.getMap(handle);
	}

	public byte getByte(String key) {
		return adapter.getByte(_handle, key);
	}

	public short getShort(String key) {
		ensureAdapter(adapter);
		return adapter.getShort(_handle, key);
	}

	public int getInt(String key) {
		ensureAdapter(adapter);
		return adapter.getInt(_handle, key);
	}

	public long getLong(String key) {
		ensureAdapter(adapter);
		return adapter.getLong(_handle, key);
	}

	public float getFloat(String key) {
		ensureAdapter(adapter);
		return adapter.getFloat(_handle, key);
	}

	public Double getDouble(String key) {
		ensureAdapter(adapter);
		return adapter.getDouble(_handle, key);
	}

	public String getString(String key) {
		ensureAdapter(adapter);
		return adapter.getString(_handle, key);
	}

	public byte[] getByteArray(String key) {
		ensureAdapter(adapter);
		return adapter.getByteArray(_handle, key);
	}

	public int[] getIntArray(String key) {
		ensureAdapter(adapter);
		return adapter.getIntArray(_handle, key);
	}

	public long[] getLongArray(String key) {
		ensureAdapter(adapter);
		return adapter.getLongArray(_handle, key);
	}

	public NBTTagCompound getCompound(String key) {
		ensureAdapter(adapter);
		return adapter.getCompound(_handle, key);
	}

	public NBTTagList getList(String key) {
		ensureAdapter(adapter);
		return adapter.getList(_handle, key);
	}

	public Object[] getListAsArray(String key) {
		NBTTagList list = getList(key);
		return (list == null ? null : list.getAsArray());
	}

	public void setByte(String key, byte value) {
		set(key, value);
	}

	public void setShort(String key, short value) {
		set(key, value);
	}

	public void setInt(String key, int value) {
		set(key, value);
	}

	public void setLong(String key, long value) {
		set(key, value);
	}

	public void setFloat(String key, float value) {
		set(key, value);
	}

	public void setDouble(String key, double value) {
		set(key, value);
	}

	public void setString(String key, String value) {
		set(key, value);
	}

	public void setCompound(String key, NBTTagCompound value) {
		set(key, value);
	}

	public void setList(String key, NBTTagList value) {
		set(key, value);
	}

	public void setList(String key, Object... objects) {
		set(key, new NBTTagList(objects));
	}

	public void setByteArray(String key, byte[] value) {
		set(key, value);
	}

	public void setIntArray(String key, int[] value) {
		set(key, value);
	}

	public void setLongArray(String key, long[] value) {
		set(key, value);
	}

	private void set(String key, Object value) {
		ensureAdapter(adapter);
		adapter.set(_handle, key, value);
	}

	public boolean hasKey(String key) {
		return _map.containsKey(key);
	}

	public void remove(String key) {
		_map.remove(key);
	}

	public int size() {
		return _map.size();
	}

	public boolean isEmpty() {
		return _map.isEmpty();
	}

	public void clear() {
		_map.clear();
	}

	public byte[] serialize() throws IOException {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			serialize(out);
			return out.toByteArray();
		}
	}

	public void serialize(OutputStream outputStream) throws IOException {
		ensureAdapter(adapter);
		adapter.serialize(_handle, outputStream);
	}

	public static NBTTagCompound unserialize(byte[] data) throws IOException {
		try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
			return unserialize(in);
		}
	}

	public static NBTTagCompound unserialize(InputStream inputStream) throws IOException {
		ensureAdapter(adapter);
		return adapter.unserialize(inputStream);
	}

	public void merge(NBTTagCompound other) {
		for (Entry<String, Object> tag : other._map.entrySet()) {
			_map.put(tag.getKey(), NBTBase.clone(tag.getValue()));
		}
	}

	@Override
	public NBTTagCompound clone() {
		return (NBTTagCompound) super.clone();
	}

	/*
	 * Converts a string-ified "mojangson" tag back into an NBTTagCompound
	 * This is the opposite of toString()
	 */
	public static NBTTagCompound fromString(String mojangson) throws Exception {
		ensureAdapter(adapter);
		return adapter.fromString(mojangson);
	}

	private static void ensureAdapter(Object adapter) throws RuntimeException {
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
	}
}
