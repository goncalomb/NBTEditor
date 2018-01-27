package com.goncalomb.bukkit.nbteditor.commands.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.utils.CustomInventory;
import com.goncalomb.bukkit.mylib.utils.UtilsMc;
import com.goncalomb.bukkit.nbteditor.NBTEditor;
import com.goncalomb.bukkit.nbteditor.bos.BookOfSouls;
import com.goncalomb.bukkit.nbteditor.nbt.BaseNBT;
import com.goncalomb.bukkit.nbteditor.nbt.variables.ItemsVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.SpecialVariable;

public class InventoryForSpecialVariable<T extends SpecialVariable> extends CustomInventory {

	public static void openSpecialInventory(Player player, BaseNBT wrapper, SpecialVariable variable) {
		if (variable instanceof ItemsVariable) {
			new InventoryForItems(player, wrapper, (ItemsVariable) variable).openInventory(player, NBTEditor.getInstance());
		}
	}

	protected static final ItemStack ITEM_FILLER = UtilsMc.newSingleItemStack(Material.BARRIER, "Nothing here!");

	private HashMap<Integer, ItemStack> _placeholders = new HashMap<Integer, ItemStack>();
	private BaseNBT _wrapper;
	protected final T _variable;

	protected static ItemStack createPlaceholder(Material material, String name) {
		return createPlaceholder(material, name, null);
	}

	protected static ItemStack createPlaceholder(Material material, String name, String lore) {
		ArrayList<String> loreList = new ArrayList<String>(2);
		if (lore != null) {
			loreList.add(lore);
		}
		loreList.add("§oThis is a placeholder item");
		loreList.add("§oit will not be saved!");
		return UtilsMc.newSingleItemStack(material, name, loreList);
	}

	protected void setItem(int slot, ItemStack item) {
		_inventory.setItem(slot, item);
	}

	protected ItemStack getItem(int slot) {
		ItemStack item = _inventory.getItem(slot);
		if (item != null && item.equals(_placeholders.get(slot))) {
			return null;
		}
		return item;
	}

	protected void setPlaceholder(int slot, Material material, String name) {
		setPlaceholder(slot, material, name, null);
	}

	protected void setPlaceholder(int slot, Material material, String name, String lore) {
		setPlaceholder(slot, createPlaceholder(material, name, lore));
	}

	protected void setPlaceholder(int slot, ItemStack item) {
		_placeholders.put(slot, item);
		_inventory.setItem(slot, item);
	}

	public InventoryForSpecialVariable(Player owner, BaseNBT wrapper, T variable) {
		super(owner, 9, wrapper.getId());
		_wrapper = wrapper;
		_variable = variable;
	}

	protected final ItemStack[] getContents() {
		ItemStack[] items = _inventory.getContents();
		for (Entry<Integer, ItemStack> entry : _placeholders.entrySet()) {
			ItemStack item = items[entry.getKey()];
			if (item != null && item.equals(entry.getValue())) {
				items[entry.getKey()] = null;
			}
		}
		return items;
	}

	@Override
	protected void inventoryClick(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		if (slot >= 0 && slot < getInventory().getSize()) {
			ItemStack item = _inventory.getItem(slot);
			if (item != null) {
				if (item.equals(_placeholders.get(slot))) {
					event.setCurrentItem(null);
				} else if (item.equals(ITEM_FILLER)) {
					event.setCancelled(true);
				}
			}

		}
		if (BookOfSouls.isValidBook(event.getCurrentItem())) {
			event.setCancelled(true);
		}
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		_wrapper.save();
	}

}
