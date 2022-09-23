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

import java.util.logging.Logger;

public final class NBTTypes {

	private static NBTTypesAdapter adapter = null;

	public static void prepareReflection(Class<?> serverClass, Logger logger) throws Exception {
		String packageName = serverClass.getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf('.') + 1);

		Class<?> clazz = Class.forName("com.goncalomb.bukkit.mylib.reflect.NBTTypesAdapter_" + version);
		adapter = (NBTTypesAdapter) clazz.getConstructor().newInstance();
		logger.info("Loaded NBTTypes adapter for " + version);
	}

	// Converts from MyLib tags, primitives and strings to internal Minecraft tags.
	public static Object toInternal(Object object) {
		ensureAdapter(adapter);
		return adapter.toInternal(object);
	}

	// Converts internal Minecraft tags to MyLib tags, primitives and strings.
	public static Object fromInternal(Object object) {
		ensureAdapter(adapter);
		return adapter.fromInternal(object);
	}

	private static void ensureAdapter(Object adapter) throws RuntimeException {
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
	}
}
