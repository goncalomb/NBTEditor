package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public class TileNBTWrapper extends TileNBT {

	protected Block _block;

	public TileNBTWrapper(Block block) {
		super(NBTUtils.getTileEntityNBTData(block));
		_block = block;
	}

	public final Location getLocation() {
		return _block.getLocation();
	}

	public void save() {
		NBTUtils.setTileEntityNBTData(_block, _data);
	}

}
