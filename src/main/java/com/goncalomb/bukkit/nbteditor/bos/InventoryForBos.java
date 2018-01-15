/*
 * Copyright (C) 2013-2018 Gonçalo Baltazar <me@goncalomb.com>
 *
 * This file is part of NBTEditor.
 *
 * NBTEditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NBTEditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NBTEditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.nbteditor.bos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.utils.CustomInventory;
import com.goncalomb.bukkit.mylib.utils.UtilsMc;
import com.goncalomb.bukkit.nbteditor.nbt.EntityNBT;

abstract class InventoryForBos<T extends EntityNBT> extends CustomInventory {

	protected static final ItemStack ITEM_FILLER = UtilsMc.newSingleItemStack(Material.BARRIER, "Nothing here!");

	private HashMap<Integer, ItemStack> _placeholders = new HashMap<Integer, ItemStack>();
	protected final BookOfSouls _bos;
	protected final T _entityNbt;
	private boolean _allowBos;

	protected static ItemStack createPlaceholder(Material material, String name) {
		return createPlaceholder(material, name, null);
	}

	protected static ItemStack createPlaceholder(Material material, String name, String lore) {
		ArrayList<String> loreList = new ArrayList<String>(2);
		if (lore != null) {
			loreList.add(lore);
		}
		loreList.add("§oThis is a placeholder item, it will not be saved!");
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

	public InventoryForBos(BookOfSouls bos, Player owner, int size, String title) {
		this(bos, owner, size, title, false);
	}

	@SuppressWarnings("unchecked")
	public InventoryForBos(BookOfSouls bos, Player owner, int size, String title, boolean allowBos) {
		super(owner, size, title);
		_bos = bos;
		_entityNbt = (T) bos.getEntityNBT();
		_allowBos = allowBos;
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
		if (!_allowBos && BookOfSouls.isValidBook(event.getCurrentItem())) {
			event.setCancelled(true);
		}
	}

}
