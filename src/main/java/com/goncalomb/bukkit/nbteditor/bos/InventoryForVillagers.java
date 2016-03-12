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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.nbteditor.nbt.VillagerNBT;
import com.goncalomb.bukkit.nbteditor.nbt.VillagerNBTOffer;

public final class InventoryForVillagers extends InventoryForBos<VillagerNBT> {

	private static ItemStack[] placeholders = new ItemStack[] {
		createPlaceholder(Material.PAPER, "§6Buy item 1"),
		createPlaceholder(Material.PAPER, "§6Buy item 2", "§bThis is optional."),
		createPlaceholder(Material.PAPER, "§6Sell item")
	};

	private BookOfSouls _bos;
	private int _page;

	public InventoryForVillagers(BookOfSouls bos, int page, Player owner) {
		super(bos, owner, 27, "Villager Offers");
		_bos = bos;
		_page = Math.min(Math.max(page, 0), 9);
		List<VillagerNBTOffer> offers = _entityNbt.getOffers();
		int i = 0;
		for (int j = _page * 9, l = offers.size(); i < 9 && j < l ; ++i, ++j) {
			VillagerNBTOffer offer = offers.get(j);
			setItem(i, offer.getBuyA());
			setItem(9 + i, offer.getBuyB());
			setItem(18 + i, offer.getSell());
		}
		if (i == 0) {
			setPlaceholder(0, placeholders[0]);
			setPlaceholder(9, placeholders[1]);
			setPlaceholder(18, placeholders[2]);
		}
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		ItemStack[] items = getContents();
		List<VillagerNBTOffer> offers = _entityNbt.getOffers();
		_entityNbt.clearOffers();
		int j = 0;
		int k = _page * 9;
		for (int l = offers.size(); j < l && j < k; ++j) {
			_entityNbt.addOffer(offers.get(j));
		}
		ArrayList<ItemStack> extraItems = new ArrayList<ItemStack>();
		for (int i = 0; i < 9; ++i, ++j) {
			if (items[i] != null && items[18 + i] != null) {
				_entityNbt.addOffer(new VillagerNBTOffer(items[i], items[9 + i], items[18 + i], Integer.MAX_VALUE));
			} else {
				if (items[i] != null) {
					extraItems.add(items[i]);
				}
				if (items[9 + i] != null) {
					extraItems.add(items[9 + i]);
				}
				if (items[18 + i] != null) {
					extraItems.add(items[18 + i]);
				}
			}
		}
		for (int l = offers.size(); j < l; ++j) {
			_entityNbt.addOffer(offers.get(j));
		}
		_bos.saveBook();

		Player player = (Player) event.getPlayer();
		if (extraItems.size() > 0) {
			player.sendMessage("§eSome offers are invalid. The items were returned.");
			player.getInventory().addItem(extraItems.toArray(new ItemStack[extraItems.size()]));
		}
		player.sendMessage("§aVillager offers set.");
	}

}
