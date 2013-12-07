/*
 * Copyright (C) 2013 - Gonçalo Baltazar <http://goncalomb.com>
 *
 * This file is part of CustomItemsAPI.
 *
 * CustomItemsAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CustomItemsAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CustomItemsAPI.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.customitems.api;

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.goncalomb.bukkit.bkglib.BKgLib;

final class CustomItemConfig {
	
	private FileConfiguration _defaultConfig;
	private ConfigurationSection _defaultItemSection;
	private File _configFile;
	private FileConfiguration _config;
	private ConfigurationSection _itemsSection;
	
	public CustomItemConfig(Plugin plugin) {
		_defaultConfig = new YamlConfiguration();
		_defaultItemSection = _defaultConfig.createSection("custom-items");
		
		_configFile = new File(BKgLib.getGlobalDataFolder(), "CustomItemsAPI/ItemsConfig/" + plugin.getName() + ".yml");
		
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
		BKgLib.saveConfig(_config, _configFile);
	}
	
}
