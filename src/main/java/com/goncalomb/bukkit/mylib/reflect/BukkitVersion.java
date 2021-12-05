package com.goncalomb.bukkit.mylib.reflect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;

public final class BukkitVersion {

	private static int _minecraftVersionMajor;
	private static int _minecraftVersionMinor;

	private static void getVersion() {
		if (_minecraftVersionMajor == 0) {
			Object server = Bukkit.getServer();
			if (server == null) {
				// test environment, CraftBukkit / Minecraft not available
				_minecraftVersionMajor = Integer.MAX_VALUE;
				_minecraftVersionMinor = Integer.MAX_VALUE;
				return;
			}
			try {
				Object mcServer = BukkitReflect.invokeMethod(server, BukkitReflect.getCraftBukkitClass("CraftServer").getDeclaredMethod("getServer"));
				String version = (String) BukkitReflect.invokeMethod(mcServer, BukkitReflect.getMinecraftClass("server.MinecraftServer").getDeclaredMethod("getVersion"));
				Matcher matcher = Pattern.compile("^(\\d+)(?:\\.(\\d+)(?:\\.(\\d+))?)?$").matcher(version);
				if (matcher.find()) {
					_minecraftVersionMajor = Integer.parseInt(matcher.group(1));
					_minecraftVersionMinor = Integer.parseInt(matcher.group(2));
				} else {
					throw new RuntimeException("Invalid Minecraft version.");
				}
			} catch (Exception e) {
				throw new RuntimeException("Error finding Minecraft version.", e);
			}
		}
	}

	private BukkitVersion() { }

	public static boolean isVersion(int minor) {
		return isVersion(minor, 1);
	}

	public static boolean isVersion(int minor, int major) {
		getVersion();
		return _minecraftVersionMajor > major || (_minecraftVersionMajor == major && _minecraftVersionMinor >= minor);
	}

}
