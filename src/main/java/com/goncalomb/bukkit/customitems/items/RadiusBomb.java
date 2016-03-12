/*
 * Copyright (C) 2013-2016 Gonçalo Baltazar <me@goncalomb.com>
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

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public abstract class RadiusBomb extends GenericBomb {

	private int _radius;

	protected RadiusBomb(String slug, String name, MaterialData material) {
		super(slug, name, material);
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
