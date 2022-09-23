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

package com.goncalomb.bukkit.nbteditor.nbt.variables;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.namemaps.MaterialMap;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public class BlockStateVariable extends NBTVariable {

	public BlockStateVariable(String key) {
		super(key);
	}

	@Override
	public boolean set(String value, Player player) {
		Material material = MaterialMap.getByName(value);
		if (material == null) {
			material = MaterialMap.getByName("minecraft:" + value);
		}
		if (material != null && material.isBlock()) {
			NBTTagCompound data = new NBTTagCompound();
			data.setString("Name", MaterialMap.getName(material));
			data().setCompound(_key, data);
			return true;
		}
		return false;
	}

	@Override
	public String get() {
		NBTTagCompound data = data().getCompound(_key);
		if (data != null && data.hasKey("Name")) {
			return data.getString("Name");
		}
		return null;
	}

	@Override
	public String getFormat() {
		return "Valid block name.";
	}

	@Override
	public List<String> getPossibleValues() {
		return MaterialMap.getBlockNames();
	}

}
