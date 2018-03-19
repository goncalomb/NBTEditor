package com.goncalomb.bukkit.nbteditor.nbt.variables;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public class ItemsVariable extends NBTVariable implements SpecialVariable {

	private String[] _descriptions;
	private boolean _useSlot;

	public ItemsVariable(String key, String[] descriptions) {
		this(key, descriptions, false);
	}

	public ItemsVariable(String key, String[] descriptions, boolean useSlot) {
		super(key);
		_descriptions = descriptions;
		_useSlot = useSlot;
	}

	@Override
	public boolean set(String value, Player player) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String get() {
		Object[] list = data().getListAsArray(_key);
		int k = 0;
		if (list != null) {
			for (int i = 0; i < Math.min(list.length, count()); i++) {
				if (list[i] != null && list[i] instanceof NBTTagCompound && ((NBTTagCompound) list[i]).hasKey("id")) {
					k++;
				}
			}
		}
		return (k == 0 ? null : k + " item(s)");
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
		NBTTagList list = new NBTTagList();
		boolean allNull = true;
		if (_useSlot) {
			for (int i = 0; i < size; i++) {
				if (items[i] != null) {
					NBTTagCompound itemNBT = NBTUtils.itemStackToNBTData(items[i]);
					itemNBT.setByte("Slot", (byte) i);
					list.add(itemNBT);
					allNull = false;
				}
			}
		} else {
			for (int i = 0; i < size; i++) {
				if (items[i] == null) {
					list.add(new NBTTagCompound());
				} else {
					list.add(NBTUtils.itemStackToNBTData(items[i]));
					allNull = false;
				}
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
			if (_useSlot) {
				for (int i = 0; i < list.length; i++) {
					if (list[i] != null && list[i] instanceof NBTTagCompound) {
						byte slot = ((NBTTagCompound) list[i]).getByte("Slot");
						if (slot >= 0 && slot < items.length) {
							items[slot] = NBTUtils.itemStackFromNBTData((NBTTagCompound) list[i]);
						}
					}
				}
			} else {
				int size = Math.min(list.length, items.length);
				for (int i = 0; i < size; i++) {
					if (list[i] != null && list[i] instanceof NBTTagCompound) {
						items[i] = NBTUtils.itemStackFromNBTData((NBTTagCompound) list[i]);
					}
				}
			}
		}
		return items;
	}

}
