package com.goncalomb.bukkit.customitems.items;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public final class AntiMatterBomb extends GenericBomb {
	
	public AntiMatterBomb() {
		super("anti-matter-bomb", ChatColor.GREEN + "Anti-Matter Bomb", new MaterialData(Material.ENDER_PEARL), false);
		setLore("§c§k----- §c§lCaution! §r§c§k-----");
		setDefaultConfig("enabled", false);
		setDefaultConfig("fuse", 60);
	}
	
	@Override
	public void onExplode(Item item, Location location) {
		World world = location.getWorld();
		int radiusSquared = 25;
		final ArrayList<FallingBlock> fallingBlocks = new ArrayList<FallingBlock>();
		for (int x = -5; x < 5; ++x) {
			for (int y = -5; y < 5; ++y) {
				for (int z = -5; z < 5; ++z) {
					Vector v = new Vector(x, y, z);
					if (v.lengthSquared() <= radiusSquared) {
						Location loc = location.clone().add(v);
						Block block = loc.getBlock();
						if (!block.isEmpty() && !block.isLiquid() && block.getType() != Material.BEDROCK) {
							FallingBlock fallingBlock = world.spawnFallingBlock(loc, block.getTypeId(), block.getData());
							fallingBlock.setDropItem(false);
							fallingBlock.setVelocity(Vector.getRandom().multiply(2).subtract(new Vector(1, 1, 1)).normalize().multiply(2));
							fallingBlocks.add(fallingBlock);
							block.setType(Material.AIR);
						}
					}
				}
			}
		}
		Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
			@Override
			public void run() {
				for (FallingBlock fallingBlock : fallingBlocks) {
					Block b = fallingBlock.getLocation().getBlock();
					if (b.getTypeId() == fallingBlock.getBlockId() && b.getData() == fallingBlock.getBlockData()) {
						b.setType(Material.AIR);
					}
					fallingBlock.remove();
				}
			}
		}, 5*20);
		
	}
	
}
