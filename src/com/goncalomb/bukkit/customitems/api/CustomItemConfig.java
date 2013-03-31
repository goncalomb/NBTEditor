package com.goncalomb.bukkit.customitems.api;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;


final class CustomItemConfig {
	
	private Logger _logger;
	private FileConfiguration _defaultConfig;
	private ConfigurationSection _defaultItemSection;
	private File _configFile;
	private FileConfiguration _config;
	private ConfigurationSection _itemsSection;
	
	public CustomItemConfig(CustomItemManager manager, Plugin plugin) {
		_logger = manager.getLogger();
		_defaultConfig = new YamlConfiguration();
		_defaultItemSection = _defaultConfig.createSection("custom-items");
		
		_configFile = new File(manager.getDataFolder(), "ItemsConfig/" + plugin.getName() + ".yml");
		
		if (!_configFile.exists()) {
			_config = new YamlConfiguration();
			_config.options().header(
					"----- CustomItems ----- Item configuration file -----\n" +
					"This file configures the custom items registed by " + plugin.getName() + ".\n" +
					"For the changes to take effect you must reload the corresponding plugin.\n" +
					"\n" +
					"Note regarding allowed-worlds/blocked-worlds:\n" +
					"  allowed-worlds, when not empty, acts like a whitelist and only\n" +
					"  on worlds from this list the item will be enabled!\n");
		} else {
			_config = YamlConfiguration.loadConfiguration(_configFile);
		}
		
		_itemsSection = _config.getConfigurationSection("custom-items");
		if (_itemsSection == null) {
			_itemsSection = _config.createSection("custom-items");
		}
		
		_config.setDefaults(_defaultConfig);
	}
	
	public void configureItem(CustomItem item) {
		if (!_itemsSection.isSet(item.getSlug())) {
			_itemsSection.createSection(item.getSlug(), item._defaultConfig);
		} else {
			_defaultItemSection.createSection(item.getSlug(), item._defaultConfig);
		}
		item.applyConfig(_itemsSection.getConfigurationSection(item.getSlug()));
	}
	
	public void removeItem(CustomItem item) {
		_itemsSection.set(item.getSlug(), null);
		_defaultItemSection.set(item.getSlug(), null);
	}
	
	public void saveToFile() {
		try {
			_config.save(_configFile);
		} catch (IOException e) {
			_logger.log(Level.SEVERE, "Could not save config to " + _configFile, e);
		}
	}
	
	
}
