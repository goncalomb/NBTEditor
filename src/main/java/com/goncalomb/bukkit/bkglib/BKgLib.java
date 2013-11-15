package com.goncalomb.bukkit.bkglib;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommand;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandManager;

public final class BKgLib {
	
	private static HashSet<Plugin> _plugins = new HashSet<Plugin>();
	private static HashMap<Plugin, Permission> _topPermissions = new HashMap<Plugin, Permission>();
	static Logger _logger;
	private static File _globalDataFolder;
	private static SimpleCommandMap _commandMap;
	
	static {
		if (_logger == null) {
			_logger = new Logger(null, null) {
				@Override
				public void log(LogRecord logRecord) {
					logRecord.setMessage("[com.goncalomb] " + logRecord.getMessage());
					super.log(logRecord);
				}
			};
			_logger.setLevel(Level.ALL);
			_logger.setParent(Bukkit.getLogger());
		}
	}
	
	// Call this on Plugin.onEnable().
	public static void bind(Plugin plugin) {
		// For now this is a private Library, only accept CustomItemsAPI and NBTEditor.
		String pgName = plugin.getName();
		if (!pgName.equals("CustomItemsAPI") && !pgName.equals("NBTEditor")) {
			return;
		}
		// Initialize some stuff.
		if (_globalDataFolder == null) {
			_globalDataFolder = new File(plugin.getDataFolder().getParentFile(), "com.goncalomb");
		}
		if (_commandMap == null) {
			Server server = Bukkit.getServer();
			Class<?> craftServerClass = server.getClass();
			try {
				Method getCommandMap = craftServerClass.getMethod("getCommandMap");
				_commandMap = (SimpleCommandMap) getCommandMap.invoke(server);
			} catch (Exception e) {
				throw new RuntimeException("Error while initializing the BetterPlugin class. This Plugin is not compatible with this version of Bukkit.");
			}
		}
		// Initialize plugin stuff.
		if (_plugins.add(plugin)) {
			Lang.load(plugin);
		}
	}
	
	// Call this on Plugin.onDisable().
	public static void unbind(Plugin plugin) {
		if (_plugins.remove(plugin)) {
			BKgCommandManager.unregisterAll(_commandMap, plugin);
			Lang.unload(plugin);
			Permission perm = _topPermissions.get(plugin);
			if (perm != null) {
				Bukkit.getPluginManager().removePermission(perm);
			}
		}
	}
	
	public static void registerCommand(BKgCommand command, Plugin plugin) {
		if (_plugins.contains(plugin)) {
			BKgCommandManager.register(_commandMap, command, plugin);
		}
	}
	
	public static File getDataFolder(Plugin plugin) {
		if (_plugins.contains(plugin)) {
			return new File(_globalDataFolder, plugin.getName());
		}
		return plugin.getDataFolder();
	}
	
	public static boolean saveConfig(FileConfiguration config, File file) {
		try {
			config.save(file);
			return true;
		} catch (IOException e) {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot save " + file);
			return false;
		}
	}
	
	public static Permission getTopPermission(Plugin plugin) {
		Permission perm = _topPermissions.get(plugin);
		if (perm == null) {
			perm = new Permission(plugin.getName().toLowerCase() + ".*");
			_topPermissions.put(plugin, perm);
			Bukkit.getPluginManager().addPermission(perm);
		}
		return perm;
	}
	
	public static File getGlobalDataFolder() {
		return _globalDataFolder;
	}
	
	static Logger getLogger() {
		return _logger;
	}
	
	private BKgLib() { }
	
}
