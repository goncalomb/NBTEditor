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

package com.goncalomb.bukkit.customitems.items;

import java.text.MessageFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.PlayerDetails;
import com.goncalomb.bukkit.mylib.utils.UtilsMc;

public class KingsCrown extends CustomItem {

	private boolean _shouldBroadcastMessage = true;

	public KingsCrown() {
		super("kings-crown", ChatColor.GOLD + "King's Crown", Material.GOLDEN_HELMET);
		addEnchantment(Enchantment.PROTECTION_FALL, 4);
	}

	private boolean shouldBroadcastMessage() {
		if (_shouldBroadcastMessage) {
			_shouldBroadcastMessage = false;
			// Prevent too much spam.
			Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
				@Override
				public void run() {
					_shouldBroadcastMessage = true;
				}
			}, 200); // 10 seconds.
			return true;
		}
		return false;
	}

	@Override
	public void onPickup(EntityPickupItemEvent event) {
		Player player = ((Player) event.getEntity());
		PlayerInventory inv = player.getInventory();
		ItemStack lastHelmet = inv.getHelmet();
		if (lastHelmet == null || inv.addItem(lastHelmet).size() == 0) {
			inv.setHelmet(event.getItem().getItemStack());
			event.getItem().remove();
			event.setCancelled(true);
			if (shouldBroadcastMessage()) {
				UtilsMc.broadcastToWorld(player.getWorld(), MessageFormat.format("§6{0} §fhas been crown §6King§f!", player.getName(), getName()));
			}
		}
	}

	@Override
	public void onDrop(PlayerDropItemEvent event) {
		lostCrown(event.getPlayer());
	}

	@Override
	public void onDespawn(ItemDespawnEvent event) {
		if (shouldBroadcastMessage()) {
			UtilsMc.broadcastToWorld(event.getEntity().getWorld(), MessageFormat.format("The {0}§f has been lost forever!", getName()));
		}
	}

	@Override
	public void onPlayerDeath(PlayerDeathEvent event, PlayerDetails details) {
		lostCrown(event.getEntity());
	}

	private void lostCrown(Player player) {
		if (shouldBroadcastMessage()) {
			UtilsMc.broadcastToWorld(player.getWorld(), MessageFormat.format("§6{0} §fhas lost his crown!", player.getName(), getName()));
		}
	}

}
