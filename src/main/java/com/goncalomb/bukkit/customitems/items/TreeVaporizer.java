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

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.customitems.api.PlayerDetails;

public final class TreeVaporizer extends GenericSuperAxe {

	public TreeVaporizer() {
		super("tree-vaporizer", ChatColor.GREEN + "Tree Vaporizer");
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event, PlayerDetails details) {
		Block root = event.getBlock();
		if (root.getType() == Material.LOG) {
			// Find the blocks
			Set<Block> blocks = getTreeBlocks(root);
			if (blocks.size() > 0) {
				root.getWorld().playSound(root.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.5f, 1);
			}
			// Destroy them.
			for (Block block : blocks) {
				block.breakNaturally();
			}
			// Apply durability.
			if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
				ItemStack item = details.getItem();
				item.setDurability((short) (item.getDurability() + blocks.size()));
			}
		}
	}
	
}
