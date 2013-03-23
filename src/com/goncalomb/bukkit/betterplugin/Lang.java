package com.goncalomb.bukkit.betterplugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

public final class Lang {

	private static Logger _logger;
	private static Plugin _plugin;
	private static String _lang;
	private static boolean _useFiles;
	private static HashMap<Plugin, FileConfiguration> _data;
	private static HashMap<String, MessageFormat> _formatCache;
	
	static {
		_logger = new Logger(null, null) {
			@Override
			public void log(LogRecord logRecord) {
				logRecord.setMessage("[XptoLang] " + logRecord.getMessage());
				super.log(logRecord);
			}
		};
		_logger.setLevel(Level.ALL);
		_logger.setParent(Bukkit.getLogger());
	}
	
	private Lang() { }
	
	public static void registerPlugin(Plugin plugin) {
		if (_plugin == null) {
			getLanguage(plugin);
			_data = new HashMap<Plugin, FileConfiguration>();
			_formatCache = new HashMap<String, MessageFormat>();
			bindToPlugin(plugin);
		}
		_formatCache.clear();
		loadLanguage(plugin);
	}
	
	private static void getLanguage(Plugin plugin) {
		File configFile = new File(plugin.getDataFolder().getParent(), "XptoLang/config.yml");
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
			FileConfiguration lang = loadLanguage(plugin, Lang.class.getPackage().getName().replace('.', '/') + "/" + fileName, fileName);
			if (lang == null) {
				_logger.warning("Missing general language file (" + _lang + ")!");
			}
			_data.put(null, lang);
		}
		
		FileConfiguration lang = loadLanguage(plugin, "language_" + _lang + ".yml", plugin.getName() + "/" + _lang + ".yml");
		if (lang == null) {
			_logger.warning("Missing language file (" + _lang + ") for " + plugin.getName() + "!");
		}
		_data.put(plugin, lang);
	}
	
	private static void bindToPlugin(Plugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
			@EventHandler
			private void onPluginDisable(PluginDisableEvent event) {
				if (_data.remove(event.getPlugin()) != null) {
					_formatCache.clear();
					if (event.getPlugin() == _plugin) {
						_plugin = null;
						if (_data.size() > 1) {
							Iterator<Plugin> it = _data.keySet().iterator();
							Plugin plugin;
							while ((plugin = it.next()) == null);
							bindToPlugin(plugin);
						} else {
							_lang = null;
							_useFiles = false;
							_data = null;
							_formatCache = null;
						}
					}
				}
			}
		}, plugin);
		_plugin = plugin;
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
		if (_plugin == null) {
			_logger.warning("XptoLang not initialized!");
			return null;
		}
		
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
