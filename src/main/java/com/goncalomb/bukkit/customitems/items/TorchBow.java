package com.goncalomb.bukkit.customitems.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.goncalomb.bukkit.customitems.api.CustomBow;
import com.goncalomb.bukkit.customitems.api.DelayedPlayerDetails;

public final class TorchBow extends CustomBow {
	
	public TorchBow() {
		super("torch-bow", ChatColor.YELLOW + "Torch Bow");
		setLore("§bLight the way with arrows.");
	}
	
	@Override
	public void onShootBow(EntityShootBowEvent event, DelayedPlayerDetails details) {
		event.getProjectile().setFireTicks(Integer.MAX_VALUE);
	}
	
	@Override
	public void onProjectileHit(ProjectileHitEvent event, DelayedPlayerDetails details) {
		event.getEntity().remove();
		Block b = event.getEntity().getLocation().getBlock();
		if (b.isEmpty()) {
			b.setType(Material.TORCH);
		}
	}
	
	@Override
	public void onProjectileDamageEntity(EntityDamageByEntityEvent event, DelayedPlayerDetails details) {
		event.setCancelled(true);
	}

}
