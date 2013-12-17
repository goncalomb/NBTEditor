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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.material.MaterialData;

import com.goncalomb.bukkit.customitemsapi.api.CustomItem;
import com.goncalomb.bukkit.customitemsapi.api.PlayerDetails;
import com.goncalomb.bukkit.nbteditor.nbt.EntityNBT;

final class BookOfSoulsEmptyCI extends CustomItem {

	public BookOfSoulsEmptyCI() {
		super("bos-empty", ChatColor.GREEN + "Book of Souls" + ChatColor.RESET + " - " + ChatColor.RED + "Empty", new MaterialData(Material.BOOK));
		setLore("§bThis is a empty Book of Souls.",
				"§bLeft-click on an existing entity to capture his soul.");
	}
	
	@Override
	public void onInteractEntity(final PlayerInteractEntityEvent event, PlayerDetails details) {
		if (EntityNBT.isValidType(event.getRightClicked().getType())) {
			details.consumeItem();
			Bukkit.getScheduler().runTask(getPlugin(), new Runnable() {
				@Override
				public void run() {
					event.getPlayer().getInventory().addItem((new BookOfSouls(EntityNBT.fromEntity(event.getRightClicked()))).getBook());
				}
			});
			event.setCancelled(true);
		}
	}
	
}
