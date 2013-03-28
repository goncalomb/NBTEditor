package com.goncalomb.bukkit.betterplugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public final class Lang {

	private static Logger _logger;
	private static String _lang;
	private static boolean _useFiles;
	private static HashMap<Plugin, FileConfiguration> _data = new HashMap<Plugin, FileConfiguration>();
	private static HashMap<String, MessageFormat> _formatCache = new HashMap<String, MessageFormat>();
	
	static {
		_logger = new Logger(null, null) {
			@Override
			public void log(LogRecord logRecord) {
				logRecord.setMessage("[gmbLang] " + logRecord.getMessage());
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
		_formatCache.clear();
	}
	
	private static void getLanguage() {
		File configFile = new File(BetterPlugin.getGmbConfigFolder(), "language_config.yml");
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
	
	private static FileConfiguration loadLanguage(Plugin plugin, String internalFile, String externalFile) {
		InputStream stream = plugin.getResource(internalFile);
		FileConfiguration lang = null;
		
		if (_useFiles) {
			File langFile = new File(plugin.getDataFolder().getParent(), "XptoLang/" + externalFile);
			if (langFile.exists()) {
				lang = YamlConfiguration.loadConfiguration(langFile);
				if (stream != null) {
					lang.setDefaults(YamlConfiguration.loadConfiguration(stream));
				}
			} else if (stream != null) {
				lang = YamlConfiguration.loadConfiguration(stream);
				_logger.info("Creating language file (" + _lang + ") for " + plugin.getName() + ".");
				try {
					lang.save(langFile);
				} catch (IOException e) {
					_logger.log(Level.SEVERE, "Could not save language file to " + langFile, e);
				}
			}
		} else if (stream != null) {
			lang = YamlConfiguration.loadConfiguration(stream);
		}
		return lang;
	}
	
	private static void loadLanguage(Plugin plugin) {
		if (_data.size() == 0) {
			String fileName = "general_" + _lang + ".yml";
			_logger.info("Loading general language file (" + _lang + ").");
			FileConfiguration lang = loadLanguage(plugin, Lang.class.getPackage().getName().replace('.', '/') + "/" + fileName, fileName);
			if (lang == null) {
				_logger.warning("Missing general language file (" + _lang + ")!");
			}
			_data.put(null, lang);
		}
		
		_logger.info("Loading language file (" + _lang + ") for " + plugin.getName() + ".");
		FileConfiguration lang = loadLanguage(plugin, "language_" + _lang + ".yml", plugin.getName() + "/" + _lang + ".yml");
		if (lang == null) {
			_logger.warning("Missing language file (" + _lang + ") for " + plugin.getName() + "!");
		}
		_data.put(plugin, lang);
	}
	
	public static String _(String key) {
		String result = (String) get(key, false);
		return (result == null ? key : result);
	}
	
	public static String _format(String key, Object... objects) {
		String format = (String) get(key, false);
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
	
	@SuppressWarnings("unchecked")
	public static List<String> _list(String key) {
		List<String> result = (List<String>) get(key, true);
		return (result == null ? Arrays.asList(key) : result);
	}
	
	private static Object get(String key, boolean asList) {
		boolean found = false;
		Object result = null;
		if (asList) {
			for (FileConfiguration file : _data.values()) {
				if (file != null && file.contains(key)) {
					found = true;
					List<String> resultList = file.getStringList(key);
					if (resultList != null && resultList.size() > 0) {
						result = resultList;
					}
					break;
				}
			}
		} else {
			for (FileConfiguration file : _data.values()) {
				if (file != null && file.contains(key)) {
					found = true;
					if (file.isString(key)) {
						result = file.getString(key);
					}
					break;
				}
			}
		}
		
		if (!found) {
			_logger.warning("Translation key not found, " + key + "!");
		} else {
			if (result == null) {
				_logger.warning("Invalid type for translation key " + key + ", expecting String" + (asList ? " List" : "") + "!");
			}
		}
		return result;
	}
	
}
