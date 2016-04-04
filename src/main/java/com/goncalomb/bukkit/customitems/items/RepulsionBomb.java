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

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public final class RepulsionBomb extends RadiusBomb {

	private double _force;

	public RepulsionBomb() {
		super("repulsion-bomb", ChatColor.YELLOW + "Repulsion Bomb", Material.COAL);
		setLore("§bLeft-click to throw the bomb.",
				"§bIt will explode after a few seconds.");
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
		world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 2f, 2f);
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
