/*
 * Copyright (C) 2013, 2014, 2015 - Gonçalo Baltazar <http://goncalomb.com>
 *
 * This file is part of BKgLib.
 *
 * BKgLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BKgLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BKgLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.bkglib;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommand;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandManager;
import com.goncalomb.bukkit.bkglib.reflect.BukkitReflect;

public final class BKgLib {
	
	private static HashSet<Plugin> _plugins = new HashSet<Plugin>();
	static Logger _logger;
	private static File _globalDataFolder;
	
	static {
		if (_logger == null) {
			_logger = new Logger("BKgLibLogger", null) {
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
		// Initialize plugin stuff.
		if (_plugins.add(plugin)) {
			Lang.load(plugin);
		}
	}
	
	// Call this on Plugin.onDisable().
	public static void unbind(Plugin plugin) {
		if (_plugins.remove(plugin)) {
			//BKgCommandManager.unregisterAll(_commandMap, plugin);
			Lang.unload(plugin);
			Permission perm = getRootPermission(plugin);
			if (perm != null) {
				Bukkit.getPluginManager().removePermission(perm);
			}
		}
	}
	
	public static void registerCommand(BKgCommand command, Plugin plugin) {
		if (_plugins.contains(plugin)) {
			BKgCommandManager.register(command, plugin);
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
	
	public static Permission getRootPermission(Plugin plugin) {
		String permName = plugin.getName().toLowerCase() + ".*";
		Permission perm = Bukkit.getPluginManager().getPermission(permName);
		if (perm == null && _plugins.contains(plugin)) {
			perm = new Permission(permName, PermissionDefault.OP);
			Bukkit.getPluginManager().addPermission(perm);
		}
		return perm;
	}
	
	public static boolean isVanillaCommand(String name) {
		Command mineCommand = BukkitReflect.getCommandMap().getCommand("minecraft:" + name);
		if (mineCommand != null) {
			Command command = BukkitReflect.getCommandMap().getCommand(name);
			return (mineCommand == command);
		}
		return false;
	}
	
	public static File getGlobalDataFolder() {
		return _globalDataFolder;
	}
	
	static Logger getLogger() {
		return _logger;
	}
	
	private BKgLib() { }
	
}
