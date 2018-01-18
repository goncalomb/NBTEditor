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

	private boolean _asShort;
	private boolean _dataAsInt;

	public BlockVariable(String blockNbtKey, String dataNbtKey) {
		this(blockNbtKey, dataNbtKey, false);
	}

	public BlockVariable(String blockNbtKey, String dataNbtKey, boolean asShort) {
		this(blockNbtKey, dataNbtKey, asShort, false);
	}

	public BlockVariable(String blockNbtKey, String dataNbtKey, boolean asShort, boolean dataAsInt) {
		super(blockNbtKey, dataNbtKey);
		_asShort = asShort;
		_dataAsInt = dataAsInt;
	}

	@Override
	public boolean set(String value, Player player) {
		NBTTagCompound data = data();
		String[] pieces = value.split(" ", 2);
		Material material = MaterialMap.getByName(pieces[0]);
		if (material == null) {
			material = MaterialMap.getByName("minecraft:" + pieces[0]);
		}
		if (material == null) {
			try {
				int blockId = Integer.parseInt(pieces[0]);
				material = Material.getMaterial(blockId);
			} catch (NumberFormatException e) {
				return false;
			}
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
			if (_asShort) {
				data.setShort(_key, (short) material.getId());
				data.setShort(_key2, (short) blockData);
			} else {
				data.setInt(_key, material.getId());
				if (_dataAsInt) {
					data.setInt(_key2, (byte) (blockData & 0xFF));
				} else {
					data.setByte(_key2, (byte) (blockData & 0xFF));
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public String get() {
		NBTTagCompound data = data();
		if (data.hasKey(_key) && data.hasKey(_key2)) {
			int materialId, materialData;
			if (_asShort) {
				materialId = data.getShort(_key) & 0xFF;
				materialData = data.getShort(_key2) & 0xFF;
			} else {
				materialId = data.getInt(_key);
				if (_dataAsInt) {
					materialData = data.getInt(_key2) & 0xFF;
				} else {
					materialData = data.getByte(_key2) & 0xFF;
				}
			}
			return MaterialMap.getName(Material.getMaterial(materialId)) + " " + materialData;
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
