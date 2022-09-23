/*
 * Copyright (C) 2013-2018 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.customitems.api;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

final class CustomItemConfig {

	private FileConfiguration _defaultConfig;
	private ConfigurationSection _defaultItemSection;
	private File _configFile;
	private FileConfiguration _config;
	private ConfigurationSection _itemsSection;

	public CustomItemConfig(String group) {
		_defaultConfig = new YamlConfiguration();
		_defaultItemSection = _defaultConfig.createSection("custom-items");

		_configFile = new File(CustomItemManager._plugin.getDataFolder(), "CustomItems" + File.separator + group + ".yml");

		if (!_configFile.exists()) {
			_config = new YamlConfiguration();
			_config.options().header(
					"----- CustomItems (" + group + ") ----- Item configuration file -----\n" +
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
			Bukkit.getLogger().log(Level.SEVERE, "Cannot save file " + _configFile, e);
		}
	}

}
