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

package com.goncalomb.bukkit.nbteditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public final class ItemStorage {

	private static File _dataFolder;

	static void setDataFolder(File dataFolder) {
		if (_dataFolder == null) {
			_dataFolder = dataFolder;
			_dataFolder.mkdirs();
		}
	}

	public static boolean isValidName(String name) {
		return name.matches("^[0-9a-zA-Z\\-_]{1,64}$");
	}

	public static boolean addItem(ItemStack item, String name) {
		_dataFolder.mkdirs();
		File file = new File(_dataFolder, name + ".dat");
		if (!file.exists()) {
			try {
				file.createNewFile();
				FileOutputStream out = new FileOutputStream(file);
				NBTUtils.itemStackToNBTData(item).serialize(out);
				out.close();
				return true;
			} catch (Exception e) {
				file.delete();
				throw new RuntimeException("Error while storing item.", e);
			}
		}
		return false;
	}

	public static ItemStack getItem(String name) {
		File file = new File(_dataFolder, name + ".dat");
		if (file.exists()) {
			try {
				FileInputStream in = new FileInputStream(file);
				ItemStack item = NBTUtils.itemStackFromNBTData(NBTTagCompound.unserialize(in));
				in.close();
				return item;
			} catch (Exception e) {
				file.delete();
				throw new RuntimeException("Error while loading stored item.", e);
			}
		}
		return null;
	}

	public static boolean existsItem(String name) {
		return (new File(_dataFolder, name + ".dat")).exists();
	}

	public static boolean removeItem(String name) {
		File file = new File(_dataFolder, name + ".dat");
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	public static List<String> listItems() {
		List<String> names = new ArrayList<String>();
		Pattern pattern = Pattern.compile("^([0-9a-zA-Z\\-_]{1,64})\\.dat$");
		String[] fileNames = _dataFolder.list();
		if (fileNames != null) {
			for (String name : fileNames) {
				Matcher matcher = pattern.matcher(name);
				if (matcher.find()) {
					names.add(matcher.group(1));
				}
			}
		}
		Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
		return Collections.unmodifiableList(names);
	}

}
