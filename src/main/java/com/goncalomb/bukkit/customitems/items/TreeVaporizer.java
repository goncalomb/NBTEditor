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
