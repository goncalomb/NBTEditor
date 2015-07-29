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

package com.goncalomb.bukkit.customitemsapi.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dispenser;
import org.bukkit.plugin.Plugin;

import com.goncalomb.bukkit.mylib.utils.UtilsMc;

public final class DispenserDetails extends ItemDetails implements IConsumableDetails {
	
	private Plugin _plugin;
	private Block _block;
	private Location _location;
	
	DispenserDetails(BlockDispenseEvent event, Plugin plugin) {
		super(event.getItem());
		_block = event.getBlock();
		_plugin = plugin;
	}
	
	public Location getLocation() {
		if (_location == null) {
			BlockFace face = ((Dispenser) _block.getState().getData()).getFacing();
			_location = _block.getLocation().add(UtilsMc.faceToDelta(face, 0.2)).add(0, -0.3, 0);
		}
		return _location;
	}
	
	@Override
	public void consumeItem() {
		org.bukkit.block.Dispenser disp = (org.bukkit.block.Dispenser) _block.getState();
		final Inventory inv = disp.getInventory();
		Bukkit.getScheduler().runTask(_plugin, new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < inv.getSize(); ++i) {
					ItemStack item = inv.getItem(i);
					if (item != null && item.isSimilar(_item)) {
						if (item.getAmount() > 1) {
							item.setAmount(item.getAmount() - 1);
						} else {
							inv.clear(i);
						}
						return;
					}
				}
			}
		});
	}
	
}
