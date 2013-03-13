package com.goncalomb.bukkit.customitems.items;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.goncalomb.bukkit.lang.Lang;

public final class RepulsionBomb extends RadiusBomb {
	
	private double _force;
	
	public RepulsionBomb() {
		super("repulsion-bomb", ChatColor.YELLOW + "Repulsion Bomb", new MaterialData(Material.COAL), true);
		setLore(Lang._list("citems.item-lores.repulsion-bomb"));
		setDefaultConfig("fuse", 45);
		setDefaultConfig("radius", 11);
		setDefaultConfig("force", 1.8d);
	}
	
	@Override
	public void applyConfig(ConfigurationSection section) {
		super.applyConfig(section);
		_force = section.getDouble("force");
	}
	
	@Override
	public void onTrigger(Item item) {
		item.setFireTicks(50);
	}
	
	@Override
	public void onExplode(Item item, Location location) {
		World world = location.getWorld();
		world.playSound(location, Sound.EXPLODE, 2f, 2f);
		world.playEffect(location, Effect.ENDER_SIGNAL, 0);
		world.playEffect(location, Effect.STEP_SOUND, Material.OBSIDIAN.getId());
		super.onExplode(item, location);
	}

	@Override
	public void affectEntity(Item item, Location location, LivingEntity entity, Vector delta, double factor) {
		entity.setVelocity(delta.normalize().multiply(_force*factor));
		((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (int) (20*factor + 10), 0));
	}

}
