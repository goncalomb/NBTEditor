/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.bkglib;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommand;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandManager;
import com.goncalomb.bukkit.bkglib.reflect.BukkitReflect;

public final class BKgLib {
	
	// Call this on Plugin.onEnable().
	public static void bind(Plugin plugin) {
		Lang.load(plugin);
	}
	
	// Call this on Plugin.onDisable().
	public static void unbind(Plugin plugin) {
		Lang.unload(plugin);
		Permission perm = getRootPermission(plugin);
		if (perm != null) {
			Bukkit.getPluginManager().removePermission(perm);
		}
	}
	
	public static void registerCommand(BKgCommand command, Plugin plugin) {
		BKgCommandManager.register(command, plugin);
	}
	
	public static Permission getRootPermission(Plugin plugin) {
		String permName = plugin.getName().toLowerCase() + ".*";
		Permission perm = Bukkit.getPluginManager().getPermission(permName);
		if (perm == null) {
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
	
	private BKgLib() { }
	
}
