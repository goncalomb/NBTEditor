package com.goncalomb.bukkit.nbteditor.nbt.variables;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public class SingleItemVariable extends NBTVariable implements SpecialVariable {

	public SingleItemVariable(String key) {
		super(key);
	}

	@Override
	public boolean set(String value, Player player) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String get() {
		if (data().getCompound(_key) != null) {
			return "item set";
		}
		return null;
	}

	@Override
	public String getFormat() {
		return "An item.";
	}

	public void setItem(ItemStack item) {
		if (item == null) {
			clear();
		} else {
			data().setCompound(_key, NBTUtils.itemStackToNBTData(item));
		}
	}

	public ItemStack getItem() {
		NBTTagCompound data = data();
		if (data.hasKey(_key)) {
			return NBTUtils.itemStackFromNBTData(data.getCompound(_key));
		}
		return null;
	}

	public boolean isValidItem(Player player, ItemStack item) {
		return true;
	}

}
