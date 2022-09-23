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

import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

import com.goncalomb.bukkit.customitems.api.PlayerDetails;
import com.goncalomb.bukkit.mylib.utils.UtilsMc;

public final class GravitationalAxe extends GenericSuperAxe {

	public GravitationalAxe() {
		super("gravitational-axe", ChatColor.GRAY + "Gravitational Axe");
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event, PlayerDetails details) {
		Block root = event.getBlock();
		if (isLog(root.getType())) {
			// Initialize some variables:
			World world = event.getPlayer().getWorld();
			Location location = root.getLocation();
			Random random = new Random();
			Vector vel = event.getPlayer().getLocation().toVector().subtract(location.toVector()).normalize().setY(0).multiply(0.2);
			// Find the blocks
			Set<Block> blocks = getTreeBlocks(root);
			if (blocks.size() > 0) {
				world.playSound(location, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 0.5f, 1);
			}
			double durability = blocks.size()*0.25;
			// Bring 'em down.
			for (Block block : blocks) {
				Material mat = block.getType();
				if (random.nextFloat() < 0.1f && isLog(mat)) {
					world.playSound(block.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 0.6f, 1);
					block.breakNaturally();
					durability += 1;
				} else if (random.nextFloat() < 0.4f && isLeaves(mat)) {
					world.playSound(block.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.5f, 1);
					block.breakNaturally();
					durability += 1;
				} else {
					FallingBlock fallingBlock = world.spawnFallingBlock(block.getLocation(), block.getBlockData());
					fallingBlock.setVelocity(vel.multiply(random.nextFloat()*0.2 + 0.9));
					if (isLeaves(mat)) {
						fallingBlock.setDropItem(false);
					}
					block.setType(Material.AIR);
				}
			}
			// Apply durability.
			if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
				UtilsMc.offsetItemStackDamage(details.getItem(), (int) durability);
			}
		}
	}

}
