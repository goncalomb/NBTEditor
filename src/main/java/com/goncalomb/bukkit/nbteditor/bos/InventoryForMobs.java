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

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.nbteditor.nbt.MobNBT;

@Deprecated
public final class InventoryForMobs extends InventoryForEquippable<MobNBT> {

	private static ItemStack potionPlaceholder = createPlaceholder(Material.GLASS_BOTTLE, "§6Effects", "§bPotion here to apply the effects.");

	public InventoryForMobs(BookOfSouls bos, Player owner) {
		super(bos, owner);
		ItemStack potion = _entityNbt.getEffectsAsPotion();
		if (potion != null) {
			setItem(8, potion);
		} else {
			setPlaceholder(8, potionPlaceholder);
		}
		// XXX: remove this alert, implement default potion fallback on NBTUtils
		owner.sendMessage("§eWhen setting effects for Mobs you must use a custom potion. §nNormal potions don't work.");
		owner.sendMessage("§eGrab any potion and use '/nbtpotion' to edit.");
	}

	@Override
	protected void inventoryClick(InventoryClickEvent event) {
		super.inventoryClick(event);
		int slot = event.getRawSlot();
		boolean isShift = event.isShiftClick();
		ItemStack itemToCheck = null;
		if (isShift && slot > 8 && event.getInventory().firstEmpty() == 8) {
			itemToCheck = event.getCurrentItem();
		} else if (slot == 8 && !isShift && event.getCursor().getType() != Material.AIR) {
			itemToCheck = event.getCursor();
		}
		if (itemToCheck != null) {
			Material type = itemToCheck.getType();
			if (type != Material.POTION && type != Material.SPLASH_POTION && type != Material.LINGERING_POTION) {
				((Player)event.getWhoClicked()).sendMessage("§cThat must be a potion!");
				event.setCancelled(true);
			}
		}
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		_entityNbt.setEffectsFromPotion(getItem(8));
		super.inventoryClose(event);
	}

}
