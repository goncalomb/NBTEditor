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

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.utils.UtilsMc;
import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.DispenserDetails;
import com.goncalomb.bukkit.customitems.api.PlayerDetails;
import com.goncalomb.bukkit.nbteditor.NBTEditor;
import com.goncalomb.bukkit.nbteditor.nbt.FallingBlockNBT;
import com.goncalomb.bukkit.nbteditor.nbt.MinecartContainerNBT;
import com.goncalomb.bukkit.nbteditor.nbt.MinecartSpawnerNBT;

final class BookOfSoulsCI extends CustomItem {

	public BookOfSoulsCI() {
		super("bos", ChatColor.AQUA + "Book of Souls", new MaterialData(Material.WRITTEN_BOOK));
	}
	
	@Override
	public void onLeftClick(PlayerInteractEvent event, PlayerDetails details) {
		Player player = event.getPlayer();
		BookOfSouls bos = BookOfSouls.getFromBook(event.getItem());
		if (bos == null) {
			player.sendMessage(Lang._(NBTEditor.class, "bos.corrupted"));
			return;
		}
		
		Location location = null;
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();
			if (bos.getEntityNBT() instanceof MinecartSpawnerNBT && block.getType() == Material.MOB_SPAWNER) {
				if (event.getPlayer().isSneaking()) {
					((MinecartSpawnerNBT) bos.getEntityNBT()).copyToSpawner(block);
					player.sendMessage(Lang._(NBTEditor.class, "bos.minecart-from"));
				} else {
					((MinecartSpawnerNBT) bos.getEntityNBT()).copyFromSpawner(block);
					bos.saveBook();
					player.sendMessage(Lang._(NBTEditor.class, "bos.minecart-to"));
				}
				event.setCancelled(true);
				return;
			} else if (bos.getEntityNBT() instanceof MinecartContainerNBT && block.getType() == Material.CHEST) {
				if (event.getPlayer().isSneaking()) {
					((MinecartContainerNBT) bos.getEntityNBT()).copyToChest(block);
					player.sendMessage(Lang._(NBTEditor.class, "bos.chest-from"));
				} else {
					((MinecartContainerNBT) bos.getEntityNBT()).copyFromChest(block);
					bos.saveBook();
					player.sendMessage(Lang._(NBTEditor.class, "bos.chest-to"));
				}
				event.setCancelled(true);
				return;
			} else if (event.getPlayer().isSneaking() && bos.getEntityNBT() instanceof FallingBlockNBT) {
				((FallingBlockNBT) bos.getEntityNBT()).copyFromTileEntity(block);
				bos.saveBook();
				player.sendMessage(Lang._(NBTEditor.class, "bos.from-block"));
				event.setCancelled(true);
				return;
			}
			
			location = event.getClickedBlock().getLocation().add(UtilsMc.faceToDelta(event.getBlockFace(), 0.5));
		} else {
			Block block = UtilsMc.getTargetBlock(player);
			if (block.getType() != Material.AIR) {
				location = UtilsMc.airLocation(block.getLocation()).add(0.0, 0.5, 0.0);
			}
		}
		
		if (location != null) {
			bos.getEntityNBT().spawnStack(location);
			event.setCancelled(true);
		} else {
			player.sendMessage(Lang._(null, "no-sight"));
		}
		return;
	};
	
	@Override
    public void onDispense(BlockDispenseEvent event, DispenserDetails details) {
		BookOfSouls bos = BookOfSouls.getFromBook(event.getItem());
		if (bos != null) {
			bos.getEntityNBT().spawnStack(details.getLocation());
		}
		event.setCancelled(true);
    }
	
	@Override
	public ItemStack getItem() {
		return null;
	}
	
}
