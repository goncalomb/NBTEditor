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

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;

import com.goncalomb.bukkit.customitems.api.PlayerDetails;
import com.goncalomb.bukkit.mylib.utils.UtilsMc;

public final class TreeVaporizer extends GenericSuperAxe {

	public TreeVaporizer() {
		super("tree-vaporizer", ChatColor.GREEN + "Tree Vaporizer");
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event, PlayerDetails details) {
		Block root = event.getBlock();
		if (isLog(root.getType())) {
			// Find the blocks
			Set<Block> blocks = getTreeBlocks(root);
			if (blocks.size() > 0) {
				root.getWorld().playSound(root.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 0.5f, 1);
			}
			// Destroy them.
			for (Block block : blocks) {
				block.breakNaturally();
			}
			// Apply durability.
			if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
				UtilsMc.offsetItemStackDamage(details.getItem(), blocks.size());
			}
		}
	}

}
