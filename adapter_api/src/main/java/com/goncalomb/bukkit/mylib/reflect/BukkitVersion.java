package com.goncalomb.bukkit.mylib.reflect;

import java.util.logging.Logger;

public final class BukkitVersion {

	private static BukkitVersionAdapter adapter = null;

	public static void prepareReflection(Class<?> serverClass, Logger logger) throws Exception {
		String packageName = serverClass.getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf('.') + 1);

		Class<?> clazz = Class.forName("com.goncalomb.bukkit.mylib.reflect.BukkitVersionAdapter_" + version);
		adapter = (BukkitVersionAdapter) clazz.getConstructor().newInstance();
		logger.info("Loaded BukkitVersion adapter for " + version);
	}

	public static void prepareForTest() throws Exception {
		Class<?> clazz = Class.forName("com.goncalomb.bukkit.mylib.reflect.BukkitVersionAdapter_v1_18_R2");
		adapter = (BukkitVersionAdapter) clazz.getConstructor().newInstance();
	}

	public static boolean isVersion(int minor) {
		return isVersion(minor, 1);
	}

	public static boolean isVersion(int minor, int major) {
		ensureAdapter(adapter);
		return adapter.isVersion(minor, major);
	}

	private static void ensureAdapter(Object adapter) throws RuntimeException {
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
	}
}
