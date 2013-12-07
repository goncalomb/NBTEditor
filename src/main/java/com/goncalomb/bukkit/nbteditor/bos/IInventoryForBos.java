/*
 * Copyright (C) 2013 - Gonçalo Baltazar <http://goncalomb.com>
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

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.utils.CustomInventory;
import com.goncalomb.bukkit.bkglib.utils.UtilsMc;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

abstract class IInventoryForBos extends CustomInventory {
	
	protected final static ItemStack _itemFiller = UtilsMc.newItem(Material.TRIPWIRE, Lang._(NBTEditor.class, "bos.inv.nothing"));
	
	private HashMap<Integer, ItemStack> _placeholders;
	private boolean _allowBos;
	
	protected final static ItemStack createPlaceholder(Material material, String name) {
		return UtilsMc.newItem(material, name, Lang._(NBTEditor.class, "bos.inv.pholder"));
	}
	
	protected final static ItemStack createPlaceholder(Material material, String name, String lore) {
		return UtilsMc.newItem(material, name, lore, Lang._(NBTEditor.class, "bos.inv.pholder"));
	}
	
	public IInventoryForBos(Player owner, int size, String title, HashMap<Integer, ItemStack> placeholders) {
		this(owner, size, title, placeholders, false);
	}
	
	public IInventoryForBos(Player owner, int size, String title, HashMap<Integer, ItemStack> placeholders, boolean allowBos) {
		super(owner, size, title);
		_placeholders = placeholders;
		_allowBos = allowBos;
		for (Entry<Integer, ItemStack> entry : _placeholders.entrySet()) {
			_inventory.setItem(entry.getKey(), entry.getValue());
		}
	}
	
	private boolean isPlaceholder(int slot) {
		ItemStack item = _inventory.getItem(slot);
		return (item != null && item.equals(_placeholders.get(slot)));
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
		if (slot > 0 && slot < getInventory().getSize() && isPlaceholder(slot)) {
			event.setCurrentItem(new ItemStack(Material.AIR));
		}
		if (!_allowBos && BookOfSouls.isValidBook(event.getCurrentItem())) {
			event.setCancelled(true);
		}
	}

}
