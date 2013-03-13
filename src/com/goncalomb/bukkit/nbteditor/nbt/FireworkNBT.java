package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import com.goncalomb.bukkit.reflect.NBTUtils;

public final class FireworkNBT extends EntityNBT {
	
	public FireworkNBT(ItemStack firework) {
		if (firework.getType() != Material.FIREWORK) throw new IllegalArgumentException("Invalid argument firework.");
		_data.setInt("Life", 0);
		_data.setInt("LifeTime", 12 + 12 * ((FireworkMeta)firework.getItemMeta()).getPower());
		_data.setCompound("FireworksItem", NBTUtils.nbtTagCompoundFromItemStack(firework));
	}
	
}
