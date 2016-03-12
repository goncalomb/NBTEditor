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

package com.goncalomb.bukkit.nbteditor.tools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.material.MaterialData;

import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.PlayerDetails;

public final class SuperLeadTool extends CustomItem {

	public SuperLeadTool() {
		super("super-lead", ChatColor.GOLD + "Super Lead", new MaterialData(Material.LEASH));
		setLore(ChatColor.YELLOW + "Right-click an entity to tie it.",
				ChatColor.YELLOW + "Then right-click another entity",
				ChatColor.YELLOW + "while sneaking to tie them together.");
	}

	private static List<LivingEntity> findLeashPrisoners(Entity holder) {
		List<LivingEntity> entities = new ArrayList<LivingEntity>();
		for (LivingEntity living : holder.getWorld().getEntitiesByClass(LivingEntity.class)) {
			if (living.isLeashed() && living.getLeashHolder().equals(holder)) {
				entities.add(living);
			}
		}
		return entities;
	}

	@Override
	public  void onInteractEntity(PlayerInteractEntityEvent event, PlayerDetails details) {
		Entity other = event.getRightClicked();
		if (event.getPlayer().isSneaking()) {
			for (LivingEntity living : findLeashPrisoners(event.getPlayer())) {
				if (!living.equals(other)) {
					living.setLeashHolder(other);
				}
			}
		} else if (other instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) other;
			if (living.isLeashed()) {
				living.setLeashHolder(null);
			} else {
				living.setLeashHolder(event.getPlayer());
			}
		} else {
			event.getPlayer().sendMessage(ChatColor.RED + "Not a valid entity!");
		}
		event.setCancelled(true);
	}

	@Override
	public void onDrop(PlayerDropItemEvent event) {
		event.getItemDrop().remove();
	}

}
