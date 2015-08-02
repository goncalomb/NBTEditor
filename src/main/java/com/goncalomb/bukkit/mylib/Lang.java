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

package com.goncalomb.bukkit.mylib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

public final class Lang {
	
	private static String _lang;
	private static boolean _useFiles;
	private static HashMap<Class<? extends Plugin>, Properties> _data = new HashMap<Class<? extends Plugin>, Properties>();
	private static HashMap<Class<? extends Plugin>, HashMap<String, MessageFormat>> _formatCache = new HashMap<Class<? extends Plugin>, HashMap<String, MessageFormat>>();
	
	public static void load(Plugin plugin) {
		if (_lang == null) {
			// Load language configuration file, language.yml.
			File configFile = new File(plugin.getDataFolder(), "language.yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
			config.options().copyDefaults(true);
			config.addDefault("language", "en");
			config.addDefault("use-files", false);
			_lang = config.getString("language");
			_useFiles = config.getBoolean("use-files");
			try {
				config.save(configFile);
			} catch (IOException e) {
				plugin.getLogger().log(Level.SEVERE, "Cannot save file " + configFile + ".", e);
			}
		}
		// Is common language file loaded?
		if (!_data.containsKey(null)) {
			// If not, load it.
			_data.put(null, loadLanguage(plugin, true));
		}
		// Load language file for that plugin.
		_data.put(plugin.getClass(), loadLanguage(plugin, false));
	}
	
	public static void unload(Plugin plugin) {
		_data.remove(plugin.getClass());
		_formatCache.remove(plugin.getClass());
		if (_data.size() == 1) {
			// Only the common language file is loaded.
			// Destroy everything.
			_lang = null;
			_useFiles = false;
			_data.clear();
			_formatCache.clear();
		}
	}
	
	private static Properties loadLanguage(Plugin plugin, boolean loadCommon) {
		String internalFile = "lang" + (loadCommon ? "/common/" : "/") + _lang + ".lang";
		File externalFile = new File(plugin.getDataFolder(), "languages" + (loadCommon ? "/" : "/" + plugin.getName()) + "/" + _lang + ".lang");
		if (_useFiles) {
			if (externalFile.exists()) {
				try {
					return readProperties(externalFile);
				} catch (IOException e) {
					plugin.getLogger().log(Level.SEVERE, "Cannot load language file " + externalFile + ".", e);
				}
			} else {
				try {
					createExternalFile(plugin, internalFile, externalFile);
				} catch (IOException e) {
					plugin.getLogger().log(Level.SEVERE, "Cannot save language file " + externalFile + ".", e);
				}
			}
		}
		// External file is disabled, missing or broken, use internal.
		InputStream stream = plugin.getResource(internalFile);
		if (stream != null) {
			try {
				return readProperties(stream);
			} catch (IOException e) {
				plugin.getLogger().log(Level.SEVERE, "Cannot load internal language file " + internalFile + ".", e);
			}
		} else if (!externalFile.exists()) {
			plugin.getLogger().warning("Missing language file " + externalFile + ".");
		}
		return new Properties();
	}
	
	private static void createExternalFile(Plugin plugin, String internalFile, File externalFile) throws IOException {
		InputStream inStream = null;
		OutputStream outStream = null;
		try {
			inStream = plugin.getResource(internalFile);
			if (inStream != null) {
				Files.createParentDirs(externalFile);
				outStream = new FileOutputStream(externalFile);
				ByteStreams.copy(inStream, outStream);
				plugin.getLogger().info("Created external language file " + externalFile + ".");
			}
		} finally {
			if (inStream != null) inStream.close();
			if (outStream != null) outStream.close();
		}
	}
	
	private static Properties readProperties(File file) throws IOException {
		return readProperties(new FileInputStream(file));
	}
	
	private static Properties readProperties(InputStream stream) throws IOException {
		Properties properties = new Properties();
		properties.load(new InputStreamReader(stream, "UTF-8"));
		return properties;
	}
	
	public static String _(Class<? extends Plugin> context, String key) {
		String result = get(context, key);
		return (result == null ? "#" + key : result);
	}
	
	public static String _(Class<? extends Plugin> context, String key, Object... objects) {
		HashMap<String, MessageFormat> formatCache = _formatCache.get(context);
		if (formatCache == null) {
			formatCache = new HashMap<String, MessageFormat>();
			_formatCache.put(context, formatCache);
		}
		MessageFormat msgFormat = formatCache.get(key);
		if (msgFormat == null) {
			String pattern = get(context, key);
			if (pattern != null) {
				msgFormat = new MessageFormat(pattern);
				formatCache.put(key, msgFormat);
				return msgFormat.format(objects);
			}
			return "#" + key;
		}
		return msgFormat.format(objects);
	}
	
	private static String get(Class<? extends Plugin> context, String key) {
		Properties strings = _data.get(context);
		if (strings != null) {
			String result = strings.getProperty(key);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	private Lang() { }
	
}
