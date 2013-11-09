package com.goncalomb.bukkit.bkglib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	private static HashMap<String, MessageFormat> _formatCache = new HashMap<String, MessageFormat>();
	
	static void load(Plugin plugin) {
		if (_lang == null) {
			File configFile = new File(BKgLib.getGlobalDataFolder(), "language.yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
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
					defaults.store(new FileOutputStream(externalFile), null);
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
		String format = get(context, key);
		if (format != null) {
			MessageFormat msgFormat = _formatCache.get(key);
			if (msgFormat == null) {
				msgFormat = new MessageFormat(format);
				_formatCache.put(key, msgFormat);
			}
			return msgFormat.format(objects);
		}
		return key;
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
