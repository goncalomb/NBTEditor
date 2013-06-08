package com.goncalomb.bukkit.betterplugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.plugin.Plugin;

public final class Lang {

	private static Logger _logger;
	private static String _lang;
	private static boolean _useFiles;
	private static LinkedHashMap<Plugin, HashMap<String, String>> _data = new LinkedHashMap<Plugin, HashMap<String, String>>();
	private static HashMap<String, MessageFormat> _formatCache = new HashMap<String, MessageFormat>();
	
	static {
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
	
	private Lang() { }
	
	static void registerPlugin(BetterPlugin plugin) {
		if (_data.size() == 0) {
			getLanguage();
		}
		_formatCache.clear();
		loadLanguage(plugin);
	}
	
	static void unregisterPlugin(BetterPlugin plugin) {
		_data.remove(plugin);
		if (_data.size() == 1) {
			_data.clear();
		}
		_formatCache.clear();
	}
	
	private static void getLanguage() {
		File configFile = new File(BetterPlugin.getGmbConfigFolder(), "lang-config.yml");
		FileConfiguration config = (configFile.exists() ? YamlConfiguration.loadConfiguration(configFile) : new YamlConfiguration());
		config.options().copyDefaults(true);
		config.addDefault("language", "en");
		config.addDefault("use-files", false);
		_lang = config.getString("language");
		_useFiles = config.getBoolean("use-files");
		try {
			config.save(configFile);
		} catch (IOException e) {
			_logger.log(Level.SEVERE, "Could not save config to " + configFile, e);
		}
	}
	
	private static HashMap<String, String> readPairs(File file) {
		try {
			return readPairs(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	private static HashMap<String, String> readPairs(InputStream stream) {
		if (stream == null) {
			return null;
		}
		HashMap<String, String> pairs = new HashMap<String, String>();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				int i = line.indexOf('=');
				if (i >= 0) {
					String key = line.substring(0, i).trim();
					String value = line.substring(i + 1);
					if (key.length() > 0 && value.length() > 0 && key.charAt(0) != '#') {
						pairs.put(key, value);
					}
				}
			}
			reader.close();
		} catch (IOException e) {
			return null;
		}
		return pairs;
	}
	
	private static HashMap<String, String> loadLanguage(Plugin plugin, String internalFile, String externalFile) {
		HashMap<String, String> pairs = readPairs(plugin.getResource(internalFile));
		if (pairs != null && _useFiles) {
			File langFile = new File(BetterPlugin.getGmbConfigFolder(), "lang/" + externalFile);
			if (langFile.exists()) {
				HashMap<String, String> filePairs = readPairs(langFile);
				if (filePairs != null) {
					for (String key : pairs.keySet()) {
						if (filePairs.containsKey(key)) {
							pairs.put(key, filePairs.get(key));
						}
					}
				}
			} else {
				_logger.info("Creating language file (" + _lang + ").");
				try {
					langFile.getParentFile().mkdirs();
					InputStream inStream = plugin.getResource(internalFile);
					FileOutputStream outStream = new FileOutputStream(langFile);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = inStream.read(buffer)) > 0) {
					   outStream.write(buffer, 0, length);
					}
					outStream.close();
				} catch (IOException e) {
					_logger.log(Level.SEVERE, "Could not save language file to " + langFile, e);
				}
			}
		}
		return pairs;
	}
	
	private static void loadLanguage(Plugin plugin) {
		if (_data.size() == 0) {
			_logger.info("Loading common language file (" + _lang + ").");
			String fileName = "common." + _lang + ".lang";
			HashMap<String, String> pairs = loadLanguage(plugin, Lang.class.getPackage().getName().replace('.', '/') + "/" + fileName, fileName);
			if (pairs == null) {
				_logger.warning("Missing common language file (" + _lang + ")!");
			} else {
				_data.put(null, pairs);
			}
		}
		
		_logger.info("Loading language file (" + _lang + ") for " + plugin.getName() + ".");
		String fileName = _lang + ".lang";
		HashMap<String, String> pairs = loadLanguage(plugin, "lang/" + fileName, plugin.getName() + "/" + fileName);
		if (pairs == null) {
			_logger.warning("Missing language file (" + _lang + ") for " + plugin.getName() + "!");
		} else {
			_data.put(plugin, pairs);
		}
	}
	
	public static String _(String key) {
		String result = get(key);
		return (result == null ? key : result);
	}
	
	public static String _format(String key, Object... objects) {
		String format = get(key);
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
	
	private static String get(String key) {
		for (HashMap<String, String> pairs : _data.values()) {
			String result = pairs.get(key);
			if (result != null) {
				return result;
			}
		}
		_logger.warning("Translation key not found, " + key + "!");
		return null;
	}
	
}
