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

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.goncalomb.bukkit.nbteditor.nbt.MinecartContainerNBT;

public class InventoryForMinecartContainer extends InventoryForBos<MinecartContainerNBT> {

	public InventoryForMinecartContainer(BookOfSouls bos, Player owner) {
		super(bos, owner, ((((MinecartContainerNBT) bos.getEntityNBT()).getInventorySize() + 8)/9)*9, "Minecart Inventory");
		_entityNbt.setItemsToInventory(_inventory);
		for (int i = _entityNbt.getInventorySize(), l = _inventory.getSize(); i < l; i++) {
			setItem(i, ITEM_FILLER);
		}
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		_entityNbt.setItemsFromInventory(_inventory);
		_bos.saveBook();
		((Player) event.getPlayer()).sendMessage("§aItems set.");
	}

}
