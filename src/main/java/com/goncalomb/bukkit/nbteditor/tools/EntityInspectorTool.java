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
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.material.MaterialData;

import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.PlayerDetails;
import com.goncalomb.bukkit.mylib.namemaps.EntityTypeMap;
import com.goncalomb.bukkit.nbteditor.nbt.EntityNBT;
import com.goncalomb.bukkit.nbteditor.nbt.VillagerNBT;
import com.goncalomb.bukkit.nbteditor.nbt.VillagerNBTOffer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTVariableContainer;

public final class EntityInspectorTool extends CustomItem {

	public EntityInspectorTool() {
		super("entity-inspector", ChatColor.AQUA + "Entity Inspector", new MaterialData(Material.STICK));
		setLore(ChatColor.YELLOW + "Right-click an entity to get their information.");
	}

	@Override
	public  void onInteractEntity(PlayerInteractEntityEvent event, PlayerDetails details) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		if (EntityNBT.isValidType(entity.getType())) {
			EntityNBT entityNBT = EntityNBT.fromEntity(entity);
			player.sendMessage(ChatColor.YELLOW + "Information about " + EntityTypeMap.getName(entity.getType()) + "");
			for (NBTVariableContainer vc : entityNBT.getAllVariables()) {
				player.sendMessage("" + ChatColor.LIGHT_PURPLE + ChatColor.ITALIC + vc.getName() + ":");
				for (NBTVariable var : vc) {
					String value = var.getValue();
					player.sendMessage("  " + ChatColor.AQUA + var.getName() + ": " + ChatColor.WHITE + (value != null ? value : ChatColor.ITALIC + "none"));
				}
			}
			player.sendMessage(ChatColor.YELLOW + "Extra information:");

			boolean extra = false;
			if (entityNBT instanceof VillagerNBT) {
				VillagerNBT villagerNBT = (VillagerNBT) entityNBT;
				player.sendMessage("" + ChatColor.LIGHT_PURPLE + ChatColor.ITALIC + "Trades done:");
				int i = 1;
				for (VillagerNBTOffer offer : villagerNBT.getOffers()) {
					player.sendMessage("  " + ChatColor.AQUA + "trade " + i + ": " + ChatColor.WHITE + offer.getUses());
					++i;
				}
				extra = true;
			}
			if (!extra) {
				player.sendMessage("none");
			}
			event.setCancelled(true);
		} else {
			player.sendMessage(ChatColor.RED + "Not a valid entity!");
		}
	}

	@Override
	public void onDrop(PlayerDropItemEvent event) {
		event.getItemDrop().remove();
	}

}
