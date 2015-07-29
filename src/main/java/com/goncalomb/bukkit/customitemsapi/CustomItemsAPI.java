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

package com.goncalomb.bukkit.customitemsapi;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;

import com.avaje.ebean.EbeanServer;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public final class CustomItemsAPI implements Plugin {
	
	private static CustomItemsAPI _instance = new CustomItemsAPI();
	
	@Override
	public File getDataFolder() {
		return NBTEditor.getInstance().getDataFolder();
	}

	@Override
	public Logger getLogger() {
		return NBTEditor.getInstance().getLogger();
	}

	@Override
	public String getName() {
		return "CustomItemsAPI";
	}
	
	public static CustomItemsAPI getInstance() {
		return _instance;
	}
	
	/* Everything else is fake. */
	
	private void throwNotImplemented() {
		throw new NotImplementedException(
				"This class is a fake plugin. Don't use it. "
				+ "It only exists to provide support while the CustomItemsAPI is being merged into the NBTEditor. "
				+ "At no time this will be initialized like a normal plugin."
		);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		throwNotImplemented();
		return null;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		throwNotImplemented();
		return false;
	}

	@Override
	public FileConfiguration getConfig() {
		throwNotImplemented();
		return null;
	}

	@Override
	public EbeanServer getDatabase() {
		throwNotImplemented();
		return null;
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String arg0, String arg1) {
		throwNotImplemented();
		return null;
	}

	@Override
	public PluginDescriptionFile getDescription() {
		throwNotImplemented();
		return null;
	}

	@Override
	public PluginLoader getPluginLoader() {
		throwNotImplemented();
		return null;
	}

	@Override
	public InputStream getResource(String arg0) {
		throwNotImplemented();
		return null;
	}

	@Override
	public Server getServer() {
		throwNotImplemented();
		return null;
	}

	@Override
	public boolean isEnabled() {
		throwNotImplemented();
		return false;
	}

	@Override
	public boolean isNaggable() {
		throwNotImplemented();
		return false;
	}

	@Override
	public void onDisable() {
		throwNotImplemented();
	}

	@Override
	public void onEnable() {
		throwNotImplemented();
	}

	@Override
	public void onLoad() {
		throwNotImplemented();
	}

	@Override
	public void reloadConfig() {
		throwNotImplemented();
	}

	@Override
	public void saveConfig() {
		throwNotImplemented();
	}

	@Override
	public void saveDefaultConfig() {
		throwNotImplemented();
	}

	@Override
	public void saveResource(String arg0, boolean arg1) {
		throwNotImplemented();
	}

	@Override
	public void setNaggable(boolean arg0) {
		throwNotImplemented();
	}
	
}
