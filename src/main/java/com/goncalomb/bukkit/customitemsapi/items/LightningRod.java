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

package com.goncalomb.bukkit.customitemsapi.items;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.material.MaterialData;

public final class LightningRod extends GenericBomb {
	
	public LightningRod() {
		super("lightning-rod", ChatColor.GRAY + "Lightning Rod", new MaterialData(Material.IRON_INGOT));
		setLore("§bLeft-click or drop key to throw the rod.",
				"§bLightning will strike after a few seconds.");
		setDefaultConfig("fuse", 40);
	}
	
	@Override
	public void onExplode(Item item, Location location) {
		item.getWorld().strikeLightning(location);
	}

}
