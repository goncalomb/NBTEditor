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

import java.util.HashMap;
import java.util.function.Function;

import org.apache.commons.lang.ClassUtils;

import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;

public final class NBTTypesAdapter_v1_18_R2 implements NBTTypesAdapter {

	private static final class NBTTypes {
		private final Function<Object, Object> wrapFunc;
		private final Function<Object, Object> unwrapFunc;

		private NBTTypes(Function<Object, Object> wrapFunc, Function<Object, Object> unwrapFunc) {
			this.wrapFunc = wrapFunc;
			this.unwrapFunc = unwrapFunc;
		}

		// Wraps primitives and strings with Minecraft tags.
		private Object wrap(Object innerObject) {
			return wrapFunc.apply(innerObject);
		}

		// Unwraps primitives and strings from Minecraft tags.
		private Object unwrap(Object tagObject) {
			return unwrapFunc.apply(tagObject);
		}
	}

	private HashMap<Class<?>, NBTTypes> _innerTypeMap = new HashMap<Class<?>, NBTTypes>();;
	private HashMap<Class<?>, NBTTypes> _outerTypeMap = new HashMap<Class<?>, NBTTypes>();;

	public NBTTypesAdapter_v1_18_R2() throws Exception {
		registerNew(byte.class, ByteTag.class, (toWrap) -> ByteTag.valueOf((Byte) toWrap), (toUnwrap) -> ((ByteTag) toUnwrap).getAsByte());
		registerNew(short.class, ShortTag.class, (toWrap) -> ShortTag.valueOf((Short) toWrap), (toUnwrap) -> ((ShortTag) toUnwrap).getAsShort());
		registerNew(int.class, IntTag.class, (toWrap) -> IntTag.valueOf((Integer) toWrap), (toUnwrap) -> ((IntTag) toUnwrap).getAsInt());
		registerNew(long.class, LongTag.class, (toWrap) -> LongTag.valueOf((Long) toWrap), (toUnwrap) -> ((LongTag) toUnwrap).getAsLong());
		registerNew(float.class, FloatTag.class, (toWrap) -> FloatTag.valueOf((Float) toWrap), (toUnwrap) -> ((FloatTag) toUnwrap).getAsFloat());
		registerNew(double.class, DoubleTag.class, (toWrap) -> DoubleTag.valueOf((Double) toWrap), (toUnwrap) -> ((DoubleTag) toUnwrap).getAsDouble());
		registerNew(String.class, StringTag.class, (toWrap) -> StringTag.valueOf((String) toWrap), (toUnwrap) -> ((StringTag) toUnwrap).getAsString());
		registerNew(byte[].class, ByteArrayTag.class, (toWrap) -> new ByteArrayTag((byte[]) toWrap), (toUnwrap) -> ((ByteArrayTag) toUnwrap).getAsByteArray());
		registerNew(int[].class, IntArrayTag.class, (toWrap) -> new IntArrayTag((int[]) toWrap), (toUnwrap) -> ((IntArrayTag) toUnwrap).getAsIntArray());
		registerNew(long[].class, LongArrayTag.class, (toWrap) -> new LongArrayTag((long[]) toWrap), (toUnwrap) -> ((LongArrayTag) toUnwrap).getAsLongArray());
	}

	private void registerNew(Class<?> innerClass, Class<?> wrapperClass, Function<Object, Object> wrapFunc, Function<Object, Object> unwrapFunc) {
		NBTTypes handler = new NBTTypes(wrapFunc, unwrapFunc);
		_innerTypeMap.put((innerClass.isPrimitive() ? ClassUtils.primitiveToWrapper(innerClass) : innerClass), handler);
		_outerTypeMap.put(wrapperClass, handler);
	}

	public Object toInternal(Object object) {
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

	public Object fromInternal(Object object) {
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
}
