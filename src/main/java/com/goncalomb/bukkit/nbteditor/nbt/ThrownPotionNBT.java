package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.bkglib.reflect.NBTUtils;
import com.goncalomb.bukkit.bkglib.reflect.WorldUtils;

public class ThrownPotionNBT extends EntityNBT {
	
	public void setPotion(ItemStack potion) {
		if (potion == null) {
			_data.remove("Potion");
		} else {
			_data.setCompound("Potion", NBTUtils.nbtTagCompoundFromItemStack(potion));
		}
	}
	
	public ItemStack getPotion() {
		if (_data.hasKey("Potion")) {
			return NBTUtils.itemStackFromNBTTagCompound(_data.getCompound("Potion"));
		}
		return null;
	}
	
	public boolean isSet() {
		return _data.hasKey("Potion");
	}
	
	@Override
	public Entity spawn(Location location) {
		if (_data.hasKey("Potion")) {
			return WorldUtils.spawnPotion(location, _data);
		}
		return null;
	}
	
}
