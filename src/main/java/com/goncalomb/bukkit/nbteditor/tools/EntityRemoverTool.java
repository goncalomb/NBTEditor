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

package com.goncalomb.bukkit.nbteditor.tools;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.material.MaterialData;

import com.goncalomb.bukkit.customitemsapi.api.CustomItem;
import com.goncalomb.bukkit.customitemsapi.api.PlayerDetails;

public final class EntityRemoverTool extends CustomItem {

	public EntityRemoverTool() {
		super("entity-remover", ChatColor.AQUA + "Entity Remover", new MaterialData(Material.BLAZE_ROD));
		setLore(ChatColor.YELLOW + "Right-click on entities remove them.");
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
	public void onDrop(PlayerDropItemEvent event) {
		event.getItemDrop().remove();
	}

}
