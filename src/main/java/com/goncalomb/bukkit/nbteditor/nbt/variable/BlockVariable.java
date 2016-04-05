/*
 * Copyright (C) 2013-2016 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.nbteditor.nbt.variable;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.namemaps.MaterialMap;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public final class BlockVariable extends NBTGenericVariable2X {

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

	boolean set(NBTTagCompound data, String value, Player player) {
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
				data.setShort(_nbtKey, (short) material.getId());
				data.setShort(_nbtKey2, (short) blockData);
			} else {
				data.setInt(_nbtKey, material.getId());
				if (_dataAsInt) {
					data.setInt(_nbtKey2, (byte) (blockData & 0xFF));
				} else {
					data.setByte(_nbtKey2, (byte) (blockData & 0xFF));
				}
			}
			return true;
		}
		return false;
	}

	String get(NBTTagCompound data) {
		if (data.hasKey(_nbtKey) && data.hasKey(_nbtKey2)) {
			int materialId, materialData;
			if (_asShort) {
				materialId = data.getShort(_nbtKey) & 0xFF;
				materialData = data.getShort(_nbtKey2) & 0xFF;
			} else {
				materialId = data.getInt(_nbtKey);
				if (_dataAsInt) {
					materialData = data.getInt(_nbtKey2) & 0xFF;
				} else {
					materialData = data.getByte(_nbtKey2) & 0xFF;
				}
			}
			return MaterialMap.getName(Material.getMaterial(materialId)) + " " + materialData;
		}
		return null;
	}

	String getFormat() {
		return "Valid block name/id and data, '<name/id> [data]'.";
	}

	public List<String> getPossibleValues() {
		return MaterialMap.getBlockNames();
	}

}
