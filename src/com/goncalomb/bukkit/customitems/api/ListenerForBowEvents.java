package com.goncalomb.bukkit.customitems.api;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;

final class ListenerForBowEvents extends CustomItemListener<CustomBow> {
	
	private CustomItemManager _manager;
	
	public ListenerForBowEvents(CustomItemManager manager) {
		_manager = manager;
	}
	
	@Override
	public boolean put(CustomBow customItem) {
		try {
			if (isOverriden(customItem, CustomBow.class, "onShootBow", EntityShootBowEvent.class, DelayedPlayerDetails.class)
					|| isOverriden(customItem, CustomBow.class, "onProjectileHit", ProjectileHitEvent.class,  DelayedPlayerDetails.class)
					|| isOverriden(customItem, CustomBow.class, "onProjectileDamageEntity", EntityDamageByEntityEvent.class, DelayedPlayerDetails.class)) {
				return super.put(customItem);
			}
		} catch (NoSuchMethodException e) {
			throw new Error(e);
		}
		return false;
	}
	
	@EventHandler
	private void entityShootBow(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			CustomBow customBow = get(event.getBow());
			if (verifyCustomItem(customBow, player, false)) {
				DelayedPlayerDetails details = new DelayedPlayerDetails(event.getBow(), player);
				customBow.onShootBow(event, details);
				if (!event.isCancelled() && event.getProjectile() instanceof Projectile) {
					details.lock();
					event.getProjectile().setMetadata("CustomItem-bow", new FixedMetadataValue(_manager._plugin, new Object[] { customBow, details }));
				}
			}
		}
	}
	
	@EventHandler
	private void entityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		if (damager.hasMetadata("CustomItem-bow")) {
			Object[] data = (Object[]) damager.getMetadata("CustomItem-bow").get(0).value();
			((CustomBow) data[0]).onProjectileDamageEntity(event, (DelayedPlayerDetails) data[1]);
		}
	}
	
	@EventHandler
	private void projectileHit(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		if (projectile.hasMetadata("CustomItem-bow")) {
			Object[] data = (Object[]) projectile.getMetadata("CustomItem-bow").get(0).value();
			((CustomBow) data[0]).onProjectileHit(event, (DelayedPlayerDetails) data[1]);
		}
	}

}
