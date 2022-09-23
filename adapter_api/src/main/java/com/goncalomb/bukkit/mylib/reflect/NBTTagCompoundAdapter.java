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
import java.util.Map;

public interface NBTTagCompoundAdapter {
	Object newInternalInstance();

	Map<String, Object> getMap(Object handle);

	byte getByte(Object handle, String key);

	short getShort(Object handle, String key);

	int getInt(Object handle, String key);

	long getLong(Object handle, String key);

	float getFloat(Object handle, String key);

	Double getDouble(Object handle, String key);

	String getString(Object handle, String key);

	byte[] getByteArray(Object handle, String key);

	int[] getIntArray(Object handle, String key);

	long[] getLongArray(Object handle, String key);

	NBTTagCompound getCompound(Object handle, String key);

	NBTTagList getList(Object handle, String key);

	void set(Object handle, String key, Object value);

	void serialize(Object handle, OutputStream outputStream) throws IOException;

	NBTTagCompound unserialize(InputStream inputStream) throws IOException;

	NBTTagCompound fromString(String mojangson) throws Exception;
}
