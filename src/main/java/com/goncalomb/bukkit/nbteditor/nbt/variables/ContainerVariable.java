package com.goncalomb.bukkit.nbteditor.nbt.variables;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public class ContainerVariable extends NBTVariable implements SpecialVariable {

	public final int size;

	public ContainerVariable(String key, int size) {
		super(key);
		this.size = size;
	}

	@Override
	public boolean set(String value, Player player) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String get() {
		return (data().hasKey(_key) ? "items set" : null);
	}

	@Override
	public String getFormat() {
		return "Inventory for items.";
	}

	public void fromInventoryItems(Inventory inventory) {
		int l = Math.min(inventory.getSize(), size);
		NBTTagList items = new NBTTagList();
		for (int i = 0; i < l; ++i) {
			ItemStack item = inventory.getItem(i);
			if (item != null) {
				NBTTagCompound itemNBT = NBTUtils.itemStackToNBTData(item);
				itemNBT.setByte("Slot", (byte) i);
				items.add(itemNBT);
			}
		}
		data().setList(_key, items);
	}

	public void toInventoryItems(Inventory inventory) {
		inventory.clear();
		NBTTagCompound data = data();
		if (data.hasKey(_key)) {
			NBTTagList items = data.getList(_key);
			int l = Math.min(items.size(), Math.min(inventory.getSize(), size));
			for (int i = 0; i < l; ++i) {
				NBTTagCompound itemNBT = (NBTTagCompound) items.get(i);
				inventory.setItem(itemNBT.getByte("Slot"), NBTUtils.itemStackFromNBTData(itemNBT));
			}
		}
	}

}
