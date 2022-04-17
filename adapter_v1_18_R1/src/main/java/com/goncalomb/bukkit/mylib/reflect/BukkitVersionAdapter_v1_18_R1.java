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

package com.goncalomb.bukkit.mylib.reflect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;

import net.minecraft.SharedConstants;

public class BukkitVersionAdapter_v1_18_R1 implements BukkitVersionAdapter {
	private final int _minecraftVersionMajor;
	private final int _minecraftVersionMinor;

	public BukkitVersionAdapter_v1_18_R1() throws Exception {
		if (Bukkit.getServer() == null) {
			// test environment, CraftBukkit / Minecraft not available
			_minecraftVersionMajor = Integer.MAX_VALUE;
			_minecraftVersionMinor = Integer.MAX_VALUE;
			return;
		}

		String version = SharedConstants.VERSION_STRING;
		Matcher matcher = Pattern.compile("^(\\d+)(?:\\.(\\d+)(?:\\.(\\d+))?)?$").matcher(version);
		if (matcher.find()) {
			_minecraftVersionMajor = Integer.parseInt(matcher.group(1));
			_minecraftVersionMinor = Integer.parseInt(matcher.group(2));
		} else {
			throw new RuntimeException("Invalid Minecraft version: " + version);
		}
	}

	public boolean isVersion(int minor, int major) {
		return _minecraftVersionMajor > major || (_minecraftVersionMajor == major && _minecraftVersionMinor >= minor);
	}
}
