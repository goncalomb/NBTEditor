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

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.nbteditor.NBTEditor;
import com.goncalomb.bukkit.nbteditor.nbt.VillagerNBT;
import com.goncalomb.bukkit.nbteditor.nbt.VillagerNBTOffer;

public final class InventoryForVillagers extends IInventoryForBos {
	
	private static HashMap<Integer, ItemStack> _placeholders = new HashMap<Integer, ItemStack>();
	
	static {
		_placeholders.put(0, createPlaceholder(Material.PAPER, Lang._(NBTEditor.class, "bos.offers.pholder.buy1")));
		_placeholders.put(9, createPlaceholder(Material.PAPER, Lang._(NBTEditor.class, "bos.offers.pholder.buy2"), Lang._(NBTEditor.class, "bos.offers.pholder.optional")));
		_placeholders.put(18, createPlaceholder(Material.PAPER, Lang._(NBTEditor.class, "bos.offers.pholder.sell")));
	}
	
	private BookOfSouls _bos;
	
	public InventoryForVillagers(BookOfSouls bos, Player owner) {
		super(owner, 27, Lang._(NBTEditor.class, "bos.offers.title"), _placeholders);
		_bos = bos;
		Inventory inv = getInventory();
		VillagerNBT villager = (VillagerNBT) _bos.getEntityNBT();
		int i = 0;
		for (VillagerNBTOffer offer : villager.getOffers()) {
			inv.setItem(i, offer.getBuyA());
			inv.setItem(9 + i, offer.getBuyB());
			inv.setItem(18 + i, offer.getSell());
			if (++i == 9) {
				break;
			}
		}
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		VillagerNBT villager = (VillagerNBT) _bos.getEntityNBT();
		ItemStack[] items = getContents();
		villager.clearOffers();
		boolean invalidOffer = false;
		for (int i = 0; i < 9; ++i) {
			if (items[i] != null && items[18 + i] != null) {
				villager.addOffer(new VillagerNBTOffer(items[i], items[9 + i], items[18 + i], Integer.MAX_VALUE));
			} else if (items[i] != null || items[9 + i] != null || items[18 + i] != null) {
				invalidOffer = true;
			}
		}
		_bos.saveBook();

		Player player = (Player)event.getPlayer();
		if (invalidOffer) {
			player.sendMessage(Lang._(NBTEditor.class, "bos.offers.invalid"));
		}
		player.sendMessage(Lang._(NBTEditor.class, "bos.offers.done"));
	}

}
