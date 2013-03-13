package com.goncalomb.bukkit.customitems.items;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public abstract class RadiusBomb extends GenericBomb {
	
	private int _radius;
	
	protected RadiusBomb(String slug, String name, MaterialData material, boolean triggerOnDrop) {
		super(slug, name, material, triggerOnDrop);
	}
	
	protected int getRadius() {
		return _radius;
	}
	
	@Override
	public void applyConfig(ConfigurationSection section) {
		super.applyConfig(section);
		_radius = section.getInt("radius", 8);
	}
	
	@Override
	public void onExplode(Item item, Location location) {
		Vector locV = item.getLocation().toVector();
		for (Entity entity : item.getNearbyEntities(_radius, _radius, _radius)) {
			if (entity instanceof LivingEntity) {
				Vector delta = ((LivingEntity) entity).getEyeLocation().toVector().subtract(locV);
				double factor = 1 - delta.length()/_radius;
				if (factor > 0) {
					affectEntity(item, location, (LivingEntity) entity, delta, factor);
				}
			}
		}
	}
	
	public abstract void affectEntity(Item item, Location location, LivingEntity entity, Vector delta, double factor);

}
