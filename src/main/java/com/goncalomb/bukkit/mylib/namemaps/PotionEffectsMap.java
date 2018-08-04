/*
 * Copyright (C) 2013-2018 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.mylib.namemaps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.potion.PotionEffectType;

public final class PotionEffectsMap {

	private final static NamingMap<PotionEffectType> _potionEffects = new NamingMap<PotionEffectType>();
	private final static List<String> _potionEffectNames;
	private final static String _potionEffectNamesAsString;

	static {
		_potionEffects.put("Speed", PotionEffectType.SPEED);
		_potionEffects.put("Slowness", PotionEffectType.SLOW);
		_potionEffects.put("Haste", PotionEffectType.FAST_DIGGING);
		_potionEffects.put("MiningFatigue", PotionEffectType.SLOW_DIGGING);
		_potionEffects.put("Strength", PotionEffectType.INCREASE_DAMAGE);
		_potionEffects.put("InstantHealth", PotionEffectType.HEAL);
		_potionEffects.put("InstantDamage", PotionEffectType.HARM);
		_potionEffects.put("JumpBoost", PotionEffectType.JUMP);
		_potionEffects.put("Nausea", PotionEffectType.CONFUSION);
		_potionEffects.put("Regeneration", PotionEffectType.REGENERATION);
		_potionEffects.put("Resistance", PotionEffectType.DAMAGE_RESISTANCE);
		_potionEffects.put("FireResistance", PotionEffectType.FIRE_RESISTANCE);
		_potionEffects.put("WaterBreathing", PotionEffectType.WATER_BREATHING);
		_potionEffects.put("Invisibility", PotionEffectType.INVISIBILITY);
		_potionEffects.put("Blindness", PotionEffectType.BLINDNESS);
		_potionEffects.put("NightVision", PotionEffectType.NIGHT_VISION);
		_potionEffects.put("Hunger", PotionEffectType.HUNGER);
		_potionEffects.put("Weakness", PotionEffectType.WEAKNESS);
		_potionEffects.put("Poison", PotionEffectType.POISON);
		_potionEffects.put("Wither", PotionEffectType.WITHER);
		_potionEffects.put("HealthBoost", PotionEffectType.HEALTH_BOOST);
		_potionEffects.put("Absorption", PotionEffectType.ABSORPTION);
		_potionEffects.put("Saturation", PotionEffectType.SATURATION);
		_potionEffects.put("Glowing", PotionEffectType.GLOWING);
		_potionEffects.put("Levitation", PotionEffectType.LEVITATION);
		_potionEffects.put("Luck", PotionEffectType.LUCK);
		_potionEffects.put("BadLuck", PotionEffectType.UNLUCK);
		_potionEffects.put("SlowFalling", PotionEffectType.SLOW_FALLING);
		_potionEffects.put("DolphinsGrace", PotionEffectType.DOLPHINS_GRACE);
		_potionEffects.put("ConduitPower", PotionEffectType.CONDUIT_POWER);

		List<String> potionEffectNames = new ArrayList<String>(_potionEffects.names());
		Collections.sort(potionEffectNames, String.CASE_INSENSITIVE_ORDER);
		_potionEffectNames = Collections.unmodifiableList(potionEffectNames);

		_potionEffectNamesAsString = StringUtils.join(_potionEffectNames, ", ");
	}

	public static PotionEffectType getByName(String name) {
		return _potionEffects.getByName(name);
	}

	public static String getName(PotionEffectType effect) {
		return _potionEffects.getName(effect);
	}

	public static List<String> getNames() {
		return _potionEffectNames;
	}

	public static String getNamesAsString() {
		return _potionEffectNamesAsString;
	}

	private PotionEffectsMap() { }

}
