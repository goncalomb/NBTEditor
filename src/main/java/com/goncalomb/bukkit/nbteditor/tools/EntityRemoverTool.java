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

package com.goncalomb.bukkit.nbteditor.tools;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.PlayerDetails;

public final class EntityRemoverTool extends CustomItem {

	private int _radius;

	public EntityRemoverTool() {
		super("entity-remover", ChatColor.AQUA + "Entity Remover", Material.BLAZE_ROD);
		setLore(ChatColor.YELLOW + "Right-click an entity to remove it.",
				ChatColor.YELLOW + "Right-click while sneeking to remove",
				ChatColor.YELLOW + "all entities in a 3 block radius.");
		setDefaultConfig("radius", 3);
	}

	@Override
	protected void applyConfig(ConfigurationSection section) {
		super.applyConfig(section);
		_radius = section.getInt("radius", 3);
	}

	@Override
	public void onInteractEntity(PlayerInteractEntityEvent event, PlayerDetails details) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		if (entity.getType() != EntityType.PLAYER) {
			event.getRightClicked().remove();
			event.setCancelled(true);
		} else {
			player.sendMessage(ChatColor.RED + "You cannot remove players!");
		}
	}

	@Override
	public void onRightClick(PlayerInteractEvent event, PlayerDetails details) {
		Player player = event.getPlayer();
		if (player.isSneaking()) {
			Location location = event.getPlayer().getLocation();
			int i = 0;
			for (Entity entity : location.getWorld().getNearbyEntities(location, _radius, _radius, _radius)) {
				if (entity.getType() != EntityType.PLAYER) {
					entity.remove();
					i++;
				}
			}
			if (i == 1) {
				player.sendMessage(ChatColor.GREEN + "Removed " + i + " entity.");
			} else if (i != 0) {
				player.sendMessage(ChatColor.GREEN + "Removed " + i + " entities.");
			} else {
				player.sendMessage(ChatColor.YELLOW + "No entities on a " + _radius + " block radius.");
			}
		}
	}

	@Override
	public void onDrop(PlayerDropItemEvent event) {
		event.getItemDrop().remove();
	}

}
