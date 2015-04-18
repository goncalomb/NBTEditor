/*
 * Copyright (C) 2013, 2014 - Gonçalo Baltazar <http://goncalomb.com>
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

package com.goncalomb.bukkit.customitemsapi.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitTask;

import com.goncalomb.bukkit.bkglib.utils.UtilsMc;

public abstract class CustomFirework extends CustomItem {
	
	protected CustomFirework(String slug, String name) {
		super(slug, name, new MaterialData(Material.FIREWORK));
	}
	
	@Override
	public final void onRightClick(PlayerInteractEvent event, PlayerDetails details) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Location loc = event.getClickedBlock().getLocation();
			if (fire(loc.add(UtilsMc.faceToDelta(event.getBlockFace())), details, null) != null) {
				details.consumeItem();
			}
			event.setCancelled(true);
		}
	}
	
	@Override
	public void onDispense(BlockDispenseEvent event, DispenserDetails details) {
		event.setCancelled(true);
	}
	
	protected final Firework fire(Location location, IConsumableDetails details, Object userObject) {
		final Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
		FireworkMeta meta = firework.getFireworkMeta();
		final FireworkPlayerDetails fDetails = FireworkPlayerDetails.fromConsumableDetails(details, firework, userObject);
		if (!onFire(fDetails, meta)) {
			firework.remove();
			return null;
		}
		firework.setFireworkMeta(meta);
		
		final BukkitTask[] task = new BukkitTask[1];
		task[0] = Bukkit.getScheduler().runTaskTimer(getPlugin(), new Runnable() {
			@Override
			public void run() {
				if (firework.isDead()) {
					onExplode(fDetails);
					task[0].cancel();
				}
				firework.setTicksLived(Integer.MAX_VALUE);
			}
		}, 10 * (1 + meta.getPower()), 2);
		return firework;
	}
	
	public boolean onFire(FireworkPlayerDetails details, FireworkMeta meta) { return true; };
	
	public void onExplode(FireworkPlayerDetails details) { };
	
}
