package com.goncalomb.bukkit.customitems.items;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import com.goncalomb.bukkit.customitems.api.PlayerDetails;

public final class FireBomb extends RadiusBomb {
	
	public FireBomb() {
		super("fire-bomb", ChatColor.RED + "Fire Bomb", new MaterialData(Material.FIREBALL), true);
		setLore("§bLeft-click or drop key to throw the bomb.",
				"§bThe bomb will explode after a few seconds.");
		setDefaultConfig("fuse", 40);
		setDefaultConfig("radius", 9);
	}
	
	@Override
	public void onRightClick(PlayerInteractEvent event, PlayerDetails details) {
		event.setCancelled(true);
	}
	
	@Override
	public void onTrigger(final Item item) {
		item.setFireTicks(50);
		int jumpFuse = getFuse() - 10;
		if (jumpFuse > 0) {
			Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
				@Override
				public void run() {
					if (!item.isDead()) {
						item.setVelocity(item.getVelocity().setY(0.5));
					}
				}
			}, jumpFuse);
		}
	}
	
	@Override
	public void onExplode(Item item, Location location) {
		World world = location.getWorld();
		world.playSound(location, Sound.EXPLODE, 2f, 1f);
		world.playEffect(location, Effect.ENDER_SIGNAL, 0);
		world.playEffect(location, Effect.STEP_SOUND, Material.FIRE.getId());
		super.onExplode(item, location);
	}

	@Override
	public void affectEntity(Item item, Location location, LivingEntity entity, Vector delta, double factor) {
		Random random = new Random();
		if (random.nextInt(2) == 0) {
			location.getWorld().spawnEntity(location, EntityType.SMALL_FIREBALL).setVelocity(delta.normalize());
		}
		if (random.nextInt(4) == 0) {
			LivingEntity livingEntity = ((LivingEntity) entity);
			int ticks = (int) (factor * 200);
			if (livingEntity.getFireTicks() < ticks) {
				livingEntity.setFireTicks(ticks);
			}
		}
	}
}
