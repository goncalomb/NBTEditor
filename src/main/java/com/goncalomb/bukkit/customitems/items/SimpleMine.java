/*
 * Copyright (C) 2013-2016 Gonçalo Baltazar <me@goncalomb.com>
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

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.PlayerDetails;

public final class SimpleMine extends CustomItem {

	public SimpleMine() {
		super("simple-mine", ChatColor.GREEN + "Mine", new MaterialData(Material.FLOWER_POT_ITEM));
		setLore("§bDrop it and walk away.");
	}

	@Override
	public void onRightClick(PlayerInteractEvent event, PlayerDetails details) {
		event.setCancelled(true);
	}

	@Override
	public void onPickup(PlayerPickupItemEvent event) {
		event.setCancelled(true);
		Item item = event.getItem();
		ItemStack stack = item.getItemStack();
		Location loc = item.getLocation();
		item.remove();
		loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 4f + 0.2f*stack.getAmount(), false, false);
	}

	@Override
	public void onDrop(PlayerDropItemEvent event) {
		event.getItemDrop().getItemStack().addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 0);
	}

}
