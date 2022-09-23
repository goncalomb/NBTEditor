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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.TagParser;

public final class NBTTagCompoundAdapter_v1_18_R2 implements NBTTagCompoundAdapter {
	public NBTTagCompoundAdapter_v1_18_R2() {
		// No setup needed but this constructor must exist
	}

	public Object newInternalInstance() {
		return new CompoundTag();
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getMap(Object handle) {
		return (Map<String, Object>) ((Object)((CompoundTag) handle).tags);
	}

	public byte getByte(Object handle, String key) {
		return ((CompoundTag) handle).getByte(key);
	}

	public short getShort(Object handle, String key) {
		return ((CompoundTag) handle).getShort(key);
	}

	public int getInt(Object handle, String key) {
		return ((CompoundTag) handle).getInt(key);
	}

	public long getLong(Object handle, String key) {
		return ((CompoundTag) handle).getLong(key);
	}

	public float getFloat(Object handle, String key) {
		return ((CompoundTag) handle).getFloat(key);
	}

	public Double getDouble(Object handle, String key) {
		return ((CompoundTag) handle).getDouble(key);
	}

	public String getString(Object handle, String key) {
		if (!((CompoundTag) handle).contains(key)) {
			return null;
		}
		return ((CompoundTag) handle).getString(key);
	}

	public byte[] getByteArray(Object handle, String key) {
		if (!((CompoundTag) handle).contains(key)) {
			return null;
		}
		return ((CompoundTag) handle).getByteArray(key);
	}

	public int[] getIntArray(Object handle, String key) {
		if (!((CompoundTag) handle).contains(key)) {
			return null;
		}
		return ((CompoundTag) handle).getIntArray(key);
	}

	public long[] getLongArray(Object handle, String key) {
		if (!((CompoundTag) handle).contains(key)) {
			return null;
		}
		return ((CompoundTag) handle).getLongArray(key);
	}

	public NBTTagCompound getCompound(Object handle, String key) {
		if (!((CompoundTag) handle).contains(key)) {
			return null;
		}
		CompoundTag obj = ((CompoundTag) handle).getCompound(key);
		if (obj != null) {
			return new NBTTagCompound(obj);
		}
		return null;
	}

	public NBTTagList getList(Object handle, String key) {
		Object obj = getMap(handle).get(key);
		if (obj != null && obj instanceof ListTag) {
			return new NBTTagList(obj);
		}
		return null;
	}

	public void set(Object handle, String key, Object value) {
		getMap(handle).put(key, NBTTypes.toInternal(value));
	}

	public void serialize(Object handle, OutputStream outputStream) throws IOException {
		NbtIo.writeCompressed((CompoundTag) handle, outputStream);
	}

	public NBTTagCompound unserialize(InputStream inputStream) throws IOException {
		return new NBTTagCompound(NbtIo.readCompressed(inputStream));
	}

	public NBTTagCompound fromString(String mojangson) throws Exception {
		return new NBTTagCompound(TagParser.parseTag(mojangson));
	}
}

