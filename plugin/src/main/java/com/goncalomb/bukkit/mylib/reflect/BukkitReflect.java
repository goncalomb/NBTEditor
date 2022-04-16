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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;

import com.google.gson.JsonParseException;

public final class BukkitReflect {

	private final static class CachedPackage {

		private String _packageName;
		private HashMap<String, Class<?>> _cache = new HashMap<String, Class<?>>();

		public CachedPackage(String packageName) {
			_packageName = packageName;
		}

		public Class<?> getClass(String className) {
			Class <?> clazz = _cache.get(className);
			if (clazz == null) {
				try {
					clazz = this.getClass().getClassLoader().loadClass(_packageName + "." + className);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException("Cannot find class " + _packageName + "." + className + ":", e);
				}
				_cache.put(className, clazz);
			}
			return clazz;
		}

	}

	private static boolean _isPrepared = false;

	private static CachedPackage _craftBukkitPackage;
	private static CachedPackage _minecraftPackage;

	private static Method _getCommandMap;
	private static Method _ChatSerializer_a_serialize;
	private static Method _ChatSerializer_a_unserialize;
	private static Constructor<?> _ChatComponentTextClass_contructor;

	public static void prepareReflection() {
		if (!_isPrepared) {
			Class<?> craftServerClass = Bukkit.getServer().getClass();
			_craftBukkitPackage = new CachedPackage(craftServerClass.getPackage().getName());
			try {
				Method getHandle = craftServerClass.getMethod("getHandle");
				_minecraftPackage = new CachedPackage(getHandle.getReturnType().getPackage().getName().replace(".server.dedicated", ""));
				_getCommandMap = craftServerClass.getMethod("getCommandMap");
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("Cannot find the required methods on the server class.", e);
			}

			_isPrepared = true;

			try {
				Class<?> iChatBaseComponentClass = getMinecraftClass("network.chat.IChatBaseComponent");
				Class<?> chatSerializerClass = getMinecraftClass("network.chat.IChatBaseComponent$ChatSerializer");
				_ChatSerializer_a_serialize = chatSerializerClass.getMethod("a", iChatBaseComponentClass);
				_ChatSerializer_a_unserialize = chatSerializerClass.getMethod("a", String.class);
				Class<?> chatComponentTextClass = getMinecraftClass("network.chat.ChatComponentText");
				_ChatComponentTextClass_contructor = chatComponentTextClass.getConstructor(String.class);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("Error while preparing ChatSerializer.", e);
			}
		}
	}

	private BukkitReflect() { }

	public static Class<?> getCraftBukkitClass(String className) {
		prepareReflection();
		return _craftBukkitPackage.getClass(className);
	}

	public static Class<?> getMinecraftClass(String className) {
		prepareReflection();
		return _minecraftPackage.getClass(className);
	}

	public static SimpleCommandMap getCommandMap() {
		prepareReflection();
		return (SimpleCommandMap) invokeMethod(Bukkit.getServer(), _getCommandMap);
	}

	public static boolean isValidRawJSON(String text) {
		prepareReflection();
		try {
			_ChatSerializer_a_unserialize.invoke(null, text);
			return true;
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof JsonParseException) {
				return false;
			}
			throw new RuntimeException(e);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new RuntimeException(e);
		}
	}

	public static String textToRawJSON(String text) {
		prepareReflection();
		try {
			return (String) _ChatSerializer_a_serialize.invoke(null, _ChatComponentTextClass_contructor.newInstance(text));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| InstantiationException e) {
			throw new RuntimeException(e);
		}
	}

	// Other helper methods...

	static Object invokeConstuctor(Constructor<?> constuctor, Object... args) {
		try {
			return constuctor.newInstance(args);
		} catch (Exception e) {
			throw new RuntimeException("Error while invoking " + constuctor.getName() + ".", e);
		}
	}

	static Object invokeMethod(Object object, Method method, Object... args) {
		try {
			return method.invoke(object, args);
		} catch (Exception e) {
			throw new RuntimeException("Error while invoking " + method.getName() + ".", e);
		}
	}

	static Object getFieldValue(Object object, Field field) {
		try {
			return field.get(object);
		} catch (Exception e) {
			throw new RuntimeException("Error while getting field value " + field.getName() + " of class " + field.getDeclaringClass().getName() + ".", e);
		}
	}

	static void setFieldValue(Object object, Field field, Object value) {
		try {
			field.set(object, value);
		} catch (Exception e) {
			throw new RuntimeException("Error while setting field value " + field.getName() + " of class " + field.getDeclaringClass().getName() + ".", e);
		}
	}

	static Object newInstance(Class<?> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Error creating instance of " + clazz.getName() + ".", e);
		}
	}

	static Object newInstance(Constructor<?> contructor, Object... initargs) {
		try {
			return contructor.newInstance(initargs);
		} catch (Exception e) {
			throw new RuntimeException("Error creating instance of " + contructor.getDeclaringClass().getName() + ".", e);
		}
	}

}
