package com.goncalomb.bukkit.customitems.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.goncalomb.bukkit.customitems.api.CustomFirework;
import com.goncalomb.bukkit.customitems.api.FireworkPlayerDetails;
import com.goncalomb.bukkit.customitems.api.PlayerDetails;

public final class EscapePlan extends CustomFirework {
	
	public EscapePlan() {
		super("escape-plan", ChatColor.YELLOW + "Escape Plan");
		setLore("§bSteve Co. Space Program!",
				"§bProvides a quick escape from your foes.",
				"§b... or just send 'em into spaaaace!",
				"§bUse on open areas.");
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent event, PlayerDetails details) {
		Entity entity = event.getEntity();
		if (entity instanceof LivingEntity) {
			details.consumeItem();
			fire(entity.getLocation(), details, entity);
		}
	}
	
	@Override
	public void onFire(FireworkPlayerDetails details, FireworkMeta meta) {
		if (details.getUserObject() == null) {
			details.setUserObject(details.getPlayer());
		}
		details.getFirework().setPassenger((LivingEntity) details.getUserObject());
		meta.setPower(2);
		meta.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).withFade(Color.WHITE).withFlicker().withTrail().build());
	}
	
	@Override
	public void onExplode(final FireworkPlayerDetails details) {
		final Vector v = details.getFirework().getVelocity().setY(0).normalize().multiply(7).setY(1);
		Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
			@Override
			public void run() {
				LivingEntity entity = (LivingEntity) details.getUserObject();
				entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 8*20, 4), true);
				entity.setVelocity(v);
			}
		}, 2);
	}
	
}
