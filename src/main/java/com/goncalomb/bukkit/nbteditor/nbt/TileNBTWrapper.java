package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
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

	public boolean cloneFrom(TileNBTWrapper other) {
		NBTTagCompound clone = other._data.clone();
		if (_data.getString("id").equals(other._data.getString("id"))) {
			clone.setInt("x", _data.getInt("x"));
			clone.setInt("y", _data.getInt("y"));
			clone.setInt("z", _data.getInt("z"));
			_data = clone;
			return true;
		}
		return false;
	}

	@Override
	public void save() {
		NBTUtils.setTileEntityNBTData(_block, _data);
	}

}
