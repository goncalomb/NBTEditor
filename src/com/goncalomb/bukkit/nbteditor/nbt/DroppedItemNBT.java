package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.reflect.NBTUtils;

public class DroppedItemNBT extends ItemsNBT {
	
	public void setItem(ItemStack item) {
		if (item == null) {
			_data.remove("Item");
		} else {
			_data.setCompound("Item", NBTUtils.nbtTagCompoundFromItemStack(item));
		}
	}
	
	public ItemStack getItem() {
		if (_data.hasKey("Item")) {
			return NBTUtils.itemStackFromNBTTagCompound(_data.getCompound("Item"));
		}
		return null;
	}
	
	public boolean isSet() {
		return _data.hasKey("Item");
	}
	
	@Override
	public Entity spawn(Location location) {
		if (_data.hasKey("Item")) {
			Entity entity = location.getWorld().dropItem(location, getItem());
			NBTUtils.setEntityNBTTagCompound(entity, _data);
			return entity;
		}
		return null;
	}
	
}
