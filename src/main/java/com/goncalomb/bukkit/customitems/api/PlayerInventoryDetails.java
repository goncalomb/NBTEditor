/*
 * Copyright (C) 2013 - Gonçalo Baltazar <http://goncalomb.com>
 *
 * This file is part of CustomItemsAPI.
 *
 * CustomItemsAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CustomItemsAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CustomItemsAPI.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.customitems.api;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class PlayerInventoryDetails extends PlayerDetails {
	
	private int _slot;
	
	PlayerInventoryDetails(ItemStack item, Player player, int slot) {
		super(item, player);
		_slot = slot;
	}
	
	public boolean isArmor() {
		return (_slot >= _player.getInventory().getSize());
	}
	
	@Override
	public void consumeItem() {
		if (_player.getGameMode() == GameMode.CREATIVE) return;
		if (_item.getAmount() > 1) {
			_item.setAmount(_item.getAmount() - 1);
		} else /*if (!isArmor())*/ {
			_player.getInventory().clear(_slot);
		}/* else {
			PlayerInventory inv = _player.getInventory();
			int slotDiff = _slot - _player.getInventory().getSize();
			switch (slotDiff) {
			case 0: inv.setBoots(null); break;
			case 1: inv.setLeggings(null); break;
			case 2: inv.setChestplate(null); break;
			case 3: inv.setHelmet(null); break;
			}
		}*/
	}
	
}
