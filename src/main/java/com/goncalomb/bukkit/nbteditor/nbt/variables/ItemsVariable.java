package com.goncalomb.bukkit.nbteditor.nbt.variables;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public class ItemsVariable extends NBTVariable implements SpecialVariable {

	private String[] _descriptions;

	public ItemsVariable(String key, String[] descriptions) {
		super(key);
		_descriptions = descriptions;
	}

	@Override
	public boolean set(String value, Player player) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String get() {
		return null;
	}

	@Override
	public String getFormat() {
		return "Items.";
	}

	public int count() {
		return _descriptions.length;
	}

	public String getDescription(int index) {
		return _descriptions[index];
	}

	public void setItems(ItemStack... items) {
		NBTTagCompound data = data();
		if (items == null) {
			data.remove(_key);
			return;
		}
		int size = Math.min(items.length, count());
		Object[] list = new Object[size];
		boolean allNull = true;
		for (int i = 0; i < size; i++) {
			if (items[i] == null) {
				list[i] = new NBTTagCompound();
			} else {
				list[i] = NBTUtils.itemStackToNBTData(items[i]);
				allNull = false;
			}
		}
		if (allNull) {
			data.remove(_key);
		} else {
			data.setList(_key, list);
		}
	}

	public ItemStack[] getItems() {
		NBTTagCompound data = data();
		Object[] list = data.getListAsArray(_key);
		ItemStack[] items = new ItemStack[count()];
		if (list != null) {
			for (int i = 0; i < Math.min(list.length, count()); i++) {
				if (list[i] != null && list[i] instanceof NBTTagCompound) {
					items[i] = NBTUtils.itemStackFromNBTData((NBTTagCompound) list[i]);
				}
			}
		}
		return items;
	}

}
