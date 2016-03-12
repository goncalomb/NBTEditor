/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
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

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.namemaps.EntityTypeMap;
import com.goncalomb.bukkit.nbteditor.nbt.EquippableNBT;

public class InventoryForEquippable<T extends EquippableNBT> extends InventoryForBos<T> {

	private static ItemStack[] placeholders = new ItemStack[] {
		createPlaceholder(Material.PAPER, "§6Head Equipment"),
		createPlaceholder(Material.PAPER, "§6Chest Equipment"),
		createPlaceholder(Material.PAPER, "§6Legs Equipment"),
		createPlaceholder(Material.PAPER, "§6Feet Equipment"),
		createPlaceholder(Material.PAPER, "§6Hand Item")
	};

	public InventoryForEquippable(BookOfSouls bos, Player owner) {
		super(bos, owner, 9, "Inventory" + " - " + ChatColor.BLACK + EntityTypeMap.getName(bos.getEntityNBT().getEntityType()));
		ItemStack[] equip = _entityNbt.getEquipment();
		for (int i = 0; i < 5; ++i) {
			if (equip[i] != null && equip[i].getType() != Material.AIR) {
				setItem(4 - i, equip[i]);
			} else {
				setPlaceholder(4 - i, placeholders[4 - i]);
			}
		}
		setItem(5, ITEM_FILLER);
		setItem(6, ITEM_FILLER);
		setItem(7, ITEM_FILLER);
		setItem(8, ITEM_FILLER);
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		_entityNbt.setEquipment(getItem(4), getItem(3), getItem(2), getItem(1), getItem(0));
		_bos.saveBook();
		((Player) event.getPlayer()).sendMessage("§aItems set.");
	}

}
