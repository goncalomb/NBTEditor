/*
 * Copyright (C) 2013 - Gonçalo Baltazar <http://goncalomb.com>
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

public final class Lang {
	
	private static String _lang;
	private static boolean _useFiles;
	private static HashMap<Class<? extends Plugin>, Properties> _data = new HashMap<Class<? extends Plugin>, Properties>();
	private static HashMap<Class<? extends Plugin>, HashMap<String, MessageFormat>> _formatCache = new HashMap<Class<? extends Plugin>, HashMap<String, MessageFormat>>();
	
	static void load(Plugin plugin) {
		if (_lang == null) {
			// Load language configuration file, language.yml.
			File configFile = new File(BKgLib.getGlobalDataFolder(), "language.yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
			config.options().copyDefaults(true);
			config.addDefault("language", "en");
			config.addDefault("use-files", false);
			_lang = config.getString("language");
			_useFiles = config.getBoolean("use-files");
			BKgLib.saveConfig(config, configFile);
		}
		// Is common language file loaded?
		if (!_data.containsKey(null)) {
			// If not, load it.
			String path = "lang/common/" + _lang + ".lang";
			File external = new File(BKgLib.getGlobalDataFolder(), "languages/" + _lang + ".lang");
			_data.put(null, loadLanguage(plugin, path, external));
		}
		// Load language file for that plugin.
		String path = "lang/" + _lang + ".lang";
		File external = new File(BKgLib.getGlobalDataFolder(), "languages/" + plugin.getName() +  "/" + _lang + ".lang");
		_data.put(plugin.getClass(), loadLanguage(plugin, path, external));
	}
	
	static void unload(Plugin plugin) {
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
	
	private static Properties loadLanguage(Plugin plugin, String internalFile, File externalFile) {
		Properties defaults;
		try {
			defaults = readProperties(plugin.getResource(internalFile), null);
		} catch (IOException e) {
			BKgLib.getLogger().log(Level.SEVERE, "Cannot load internal language file " + internalFile + ".", e);
			defaults = new Properties();
		}
		if (_useFiles) {
			if (externalFile.exists()) {
				try {
					return readProperties(externalFile, defaults);
				} catch (IOException e) {
					BKgLib.getLogger().log(Level.SEVERE, "Cannot load language file " + externalFile + ".", e);
				}
			} else {
				try {
					if (!externalFile.getParentFile().isDirectory()) {
						externalFile.getParentFile().mkdirs();
					}
					InputStream inStream = plugin.getResource(internalFile);
					OutputStream outStream = new FileOutputStream(externalFile);
					byte[] buffer = new byte[1024];
					int r;
					while ((r = inStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, r);
					}
					inStream.close();
					outStream.close();
				} catch (IOException e) {
					BKgLib.getLogger().log(Level.SEVERE, "Cannot save language file " + externalFile + ".", e);
				}
			}
		}
		return defaults;
	}
	
	private static Properties readProperties(File file, Properties defaults) throws IOException {
		return readProperties(new FileInputStream(file), defaults);
	}
	
	private static Properties readProperties(InputStream stream, Properties defaults) throws IOException {
		Properties properties = new Properties(defaults);
		properties.load(new InputStreamReader(stream, "UTF-8"));
		return properties;
	}
	
	public static String _(Class<? extends Plugin> context, String key) {
		String result = get(context, key);
		return (result == null ? key : result);
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
			return key;
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
