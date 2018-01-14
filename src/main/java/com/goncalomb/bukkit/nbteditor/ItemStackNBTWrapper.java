package com.goncalomb.bukkit.nbteditor;

import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.reflect.NBTUtils;
import com.goncalomb.bukkit.nbteditor.nbt.ItemNBT;

public class ItemStackNBTWrapper extends ItemNBT {

	ItemStack _stack;

	public ItemStackNBTWrapper(ItemStack stack) {
		super(stack.getType(), NBTUtils.getItemStackTag(stack));
		_stack = stack;
	}

	@Override
	public void save() {
		NBTUtils.setItemStackTag(_stack, _data);
	}

}
