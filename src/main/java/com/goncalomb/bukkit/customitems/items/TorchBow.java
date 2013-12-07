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

package com.goncalomb.bukkit.customitems.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.goncalomb.bukkit.customitems.api.CustomBow;
import com.goncalomb.bukkit.customitems.api.DelayedPlayerDetails;

public final class TorchBow extends CustomBow {
	
	public TorchBow() {
		super("torch-bow", ChatColor.YELLOW + "Torch Bow");
		setLore("§bLight the way with arrows.");
	}
	
	@Override
	public void onShootBow(EntityShootBowEvent event, DelayedPlayerDetails details) {
		event.getProjectile().setFireTicks(Integer.MAX_VALUE);
	}
	
	@Override
	public void onProjectileHit(ProjectileHitEvent event, DelayedPlayerDetails details) {
		event.getEntity().remove();
		Block b = event.getEntity().getLocation().getBlock();
		if (b.isEmpty()) {
			b.setType(Material.TORCH);
		}
	}
	
	@Override
	public void onProjectileDamageEntity(EntityDamageByEntityEvent event, DelayedPlayerDetails details) {
		event.setCancelled(true);
	}

}
