package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.reflect.NBTTagListWrapper;
import com.goncalomb.bukkit.reflect.NBTUtils;

public abstract class MinecartContainerNBT extends MinecartNBT {
	
	protected final void internalCopyFromChest(Block block, int count) {
		Inventory inv = ((Chest) block.getState()).getBlockInventory();
		NBTTagListWrapper items = new NBTTagListWrapper();
		for (int i = 0, l = count; i < l; ++i) {
			ItemStack item = inv.getItem(i);
			if (item != null) {
				NBTTagCompoundWrapper itemNBT = NBTUtils.nbtTagCompoundFromItemStack(item);
				itemNBT.setByte("Slot", (byte) i);
				items.add(itemNBT);
			}
		}
		_data.setList("Items", items);
	}
	
	public abstract void copyFromChest(Block block);
	
	public final void copyToChest(Block block) {
		Inventory inv = ((Chest) block.getState()).getBlockInventory();
		inv.clear();
		if (_data.hasKey("Items")) {
			NBTTagListWrapper items = _data.getList("Items");
			for (int i = 0, l = items.size(); i < l; ++i) {
				NBTTagCompoundWrapper itemNBT = (NBTTagCompoundWrapper) items.get(i);
				inv.setItem(itemNBT.getByte("Slot"), NBTUtils.itemStackFromNBTTagCompound(itemNBT));
			}
		}
	}
	
}
