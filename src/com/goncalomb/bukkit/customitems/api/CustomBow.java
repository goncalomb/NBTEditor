package com.goncalomb.bukkit.customitems.api;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.material.MaterialData;


public abstract class CustomBow extends CustomItem {
	
	protected CustomBow(String slug, String name) {
		super(slug, name, new MaterialData(Material.BOW));
	}
	
	public void onShootBow(EntityShootBowEvent event, DelayedPlayerDetails details) { }
	
	public void onProjectileHit(ProjectileHitEvent event, DelayedPlayerDetails details) { }
	
	public void onProjectileDamageEntity(EntityDamageByEntityEvent event, DelayedPlayerDetails details) { }
}
