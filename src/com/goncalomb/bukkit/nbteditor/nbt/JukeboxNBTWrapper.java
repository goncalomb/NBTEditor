package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.reflect.NBTUtils;

public final class JukeboxNBTWrapper extends TileNBTWrapper {
	
	public JukeboxNBTWrapper(Block block) {
		super(block);
	}
	
	public void setRecord(ItemStack item) {
		if (item == null || item.getType() == Material.AIR) {
			_data.setInt("Record", 0);
			_data.setCompound("RecordItem", new NBTTagCompoundWrapper());
		} else {
			_data.setInt("Record", item.getTypeId());
			_data.setCompound("RecordItem", NBTUtils.nbtTagCompoundFromItemStack(item));
		}
	}
	
	@Override
	public void save() {
		if (_data.getInt("Record") != 0 && _block.getData() == 0) {
			_block.setData((byte) 1);
		} else if (_data.getInt("Record") == 0 && _block.getData() != 0) {
			_block.setData((byte) 0);
		}
		super.save();
	}

}
