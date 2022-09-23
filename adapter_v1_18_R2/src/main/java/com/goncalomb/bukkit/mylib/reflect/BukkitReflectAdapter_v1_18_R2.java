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

import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public final class BukkitReflectAdapter_v1_18_R2 implements BukkitReflectAdapter {
	public BukkitReflectAdapter_v1_18_R2() {
		// No setup needed but this constructor must exist
	}

	public SimpleCommandMap getCommandMap() {
		return ((CraftServer) Bukkit.getServer()).getCommandMap();
	}

	public boolean isValidRawJSON(String text) {
		try {
			if (Component.Serializer.fromJson(text) != null) {
				return true;
			}
		} catch (Exception ex) {
			// Invalid, fallthrough to return false
		}
		return false;
	}

	public String textToRawJSON(String text) {
		return Component.Serializer.toJson(new TextComponent(text));
	}
}
