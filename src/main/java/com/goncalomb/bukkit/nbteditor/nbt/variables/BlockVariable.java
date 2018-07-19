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

public final class BlockVariable extends NBTVariableDouble {

	public static enum DataType { INT, SHORT, BYTE }

	private DataType _dataType;

	public BlockVariable(String blockKey, String dataKey, DataType dataType) {
		super(blockKey, dataKey);
		_dataType = dataType;
	}

	@Override
	public boolean set(String value, Player player) {
		NBTTagCompound data = data();
		String[] pieces = value.split(" ", 2);
		Material material = MaterialMap.getByName(pieces[0]);
		if (material == null) {
			material = MaterialMap.getByName("minecraft:" + pieces[0]);
		}
		if (material != null && material.isBlock()) {
			int blockData = 0;
			if (pieces.length == 2) {
				try {
					blockData = Integer.parseInt(pieces[1]);
					if (blockData < 0 || blockData > 0xFF) return false;
				} catch (NumberFormatException e) {
					return false;
				}
			}
			data.setString(_key, MaterialMap.getName(material));
			switch (_dataType) {
			case INT:
				data.setInt(_key2, (byte) blockData);
				break;
			case SHORT:
				data.setShort(_key2, (short) blockData);
				break;
			case BYTE:
				data.setByte(_key2, (byte) blockData);
				break;
			}
			return true;
		}
		return false;
	}

	@Override
	public String get() {
		NBTTagCompound data = data();
		if (data.hasKey(_key) && data.hasKey(_key2)) {
			String name = data.getString(_key);
			int blockData = 0;
			switch (_dataType) {
			case INT:
				blockData = data.getInt(_key2) & 0xFF;
				break;
			case SHORT:
				blockData = data.getShort(_key2) & 0xFF;
				break;
			case BYTE:
				blockData = data.getByte(_key2) & 0xFF;
				break;
			}
			return name + " " + blockData;
		}
		return null;
	}

	@Override
	public String getFormat() {
		return "Valid block name/id and data, '<name/id> [data]'.";
	}

	@Override
	public List<String> getPossibleValues() {
		return MaterialMap.getBlockNames();
	}

}
