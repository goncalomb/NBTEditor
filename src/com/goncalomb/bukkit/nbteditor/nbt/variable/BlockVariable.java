package com.goncalomb.bukkit.nbteditor.nbt.variable;

import org.bukkit.Material;

import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.lang.Lang;

public final class BlockVariable extends NBTGenericVariable2X {
	
	private boolean _asShort;
	
	public BlockVariable(String blockNbtKey, String dataNbtKey) {
		this(blockNbtKey, dataNbtKey, false);
	}
	
	public BlockVariable(String blockNbtKey, String dataNbtKey, boolean asShort) {
		super(blockNbtKey, dataNbtKey);
		_asShort = asShort;
	}
	
	boolean set(NBTTagCompoundWrapper data, String value) {
		String[] pieces = value.split(":", 2);
		Material material = Material.getMaterial(pieces[0]);
		if (material == null) {
			try {
				int blockId = Integer.parseInt(pieces[0]);
				if (blockId < 0 || blockId > 0xFF) return false;
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
				// The enderman is the only entity that uses short.
				if (material.getId() > 127) {
					return false; // Enderman crashes the game with id > 127.
				}
				data.setShort(_nbtKey, (short) (material.getId() & 0xFF));
				data.setShort(_nbtKey2, (short) (blockData & 0xFF));
			} else {
				data.setByte(_nbtKey, (byte) (material.getId() & 0xFF));
				data.setByte(_nbtKey2, (byte) (blockData & 0xFF));
			}
			return true;
		}
		return false;
	}
	
	String get(NBTTagCompoundWrapper data) {
		if (data.hasKey(_nbtKey) && data.hasKey(_nbtKey2)) {
			int materialId, materialData;
			if (_asShort) {
				materialId = data.getShort(_nbtKey) & 0xFF;
				materialData = data.getShort(_nbtKey2) & 0xFF;
			} else {
				materialId = data.getByte(_nbtKey) & 0xFF;
				materialData = data.getByte(_nbtKey2) & 0xFF;
			}
			return Material.getMaterial(materialId).name() + ":" + materialData;
		}
		return null;
	}
	
	String getFormat() {
		return Lang._("nbt.variable.formats.block");
	}
	
}
