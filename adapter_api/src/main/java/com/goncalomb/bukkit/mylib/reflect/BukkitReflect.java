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

import org.bukkit.command.SimpleCommandMap;

public final class BukkitReflect {

	private static BukkitReflectAdapter adapter = null;

	public static void prepareReflection(Class<?> serverClass, Logger logger) throws Exception {
		String packageName = serverClass.getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf('.') + 1);

		Class<?> clazz = Class.forName("com.goncalomb.bukkit.mylib.reflect.BukkitReflectAdapter_" + version);
		adapter = (BukkitReflectAdapter) clazz.getConstructor().newInstance();
		logger.info("Loaded BukkitReflect adapter for " + version);
	}


	public static SimpleCommandMap getCommandMap() {
		ensureAdapter(adapter);
		return adapter.getCommandMap();
	}

	public static boolean isValidRawJSON(String text) {
		ensureAdapter(adapter);
		return adapter.isValidRawJSON(text);
	}

	public static String textToRawJSON(String text) {
		ensureAdapter(adapter);
		return adapter.textToRawJSON(text);
	}

	public static void ensureAdapter(Object adapter) throws RuntimeException {
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
	}
}
