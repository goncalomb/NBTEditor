package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

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
