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
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.nbteditor.NBTEditor;
import com.goncalomb.bukkit.nbteditor.nbt.EntityNBT;

public final class InventoryForRiding extends IInventoryForBos {
	
	private static HashMap<Integer, ItemStack> _placeholders = new HashMap<Integer, ItemStack>();
	
	static {
		_placeholders.put(53, createPlaceholder(Material.PAPER, Lang._(NBTEditor.class, "bos.riding.pholder"), Lang._(NBTEditor.class, "bos.riding.pholder-lore")));
	}
	
	private BookOfSouls _bos;
	
	public InventoryForRiding(BookOfSouls bos, Player owner) {
		super(owner, 54, Lang._(NBTEditor.class, "bos.riding.title"), _placeholders, true);
		_bos = bos;
		Inventory inv = getInventory();
		int i = 0;
		EntityNBT entityNBT = bos.getEntityNBT();
		while ((entityNBT = entityNBT.getRiding()) != null) {
			EntityNBT riding = entityNBT.clone();
			riding.setRiding((EntityNBT[]) null);
			inv.setItem(i++, (new BookOfSouls(riding)).getBook());
		}
	}
	
	@Override
	protected void inventoryClick(InventoryClickEvent event) {
		super.inventoryClick(event);
		ItemStack item = event.getCurrentItem();
		if (item != null && item.getType() != Material.AIR) {
			Player player = (Player) event.getWhoClicked();
			if (item.equals(_bos.getBook())) {
				event.setCancelled(true);
			} else if (!BookOfSouls.isValidBook(item)) {
				event.setCancelled(true);
				player.sendMessage(Lang._(NBTEditor.class, "bos.riding.only-bos"));
			} else {
				EntityNBT entityNbt = BookOfSouls.bookToEntityNBT(item);
				if (entityNbt == null) {
					player.sendMessage(Lang._(NBTEditor.class, "bos.corrupted"));
					event.setCancelled(true);
				} else if (entityNbt.getRiding() != null) {
					player.sendMessage(Lang._(NBTEditor.class, "bos.riding.has-riding"));
					event.setCancelled(true);
				}
			}
		}
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		List<EntityNBT> rides = new ArrayList<EntityNBT>(54);
		ItemStack[] items = getContents();
		for (ItemStack item : items) {
			if (BookOfSouls.isValidBook(item)) {
				rides.add(BookOfSouls.bookToEntityNBT(item));
			}
		}
		_bos.getEntityNBT().setRiding(rides.toArray(new EntityNBT[rides.size()]));
		_bos.saveBook();
		((Player)event.getPlayer()).sendMessage(Lang._(NBTEditor.class, "bos.riding.done"));
	}
	
}
