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
import org.bukkit.enchantments.Enchantment;

public final class EnchantmentsMap {

	private final static NamingMap<Enchantment> _enchantments = new NamingMap<Enchantment>();
	private final static List<String> _enchantmentNames;
	private final static String _enchantmentNamesAsString;

	static {
		_enchantments.put("Protection", Enchantment.PROTECTION_ENVIRONMENTAL);
		_enchantments.put("FireProtection", Enchantment.PROTECTION_FIRE);
		_enchantments.put("FeatherFalling", Enchantment.PROTECTION_FALL);
		_enchantments.put("BlastProtection", Enchantment.PROTECTION_EXPLOSIONS);
		_enchantments.put("ProjectileProtection", Enchantment.PROTECTION_PROJECTILE);
		_enchantments.put("Respiration", Enchantment.OXYGEN);
		_enchantments.put("AquaAffinity", Enchantment.WATER_WORKER);
		_enchantments.put("Thorns", Enchantment.THORNS);
		_enchantments.put("DepthStrider", Enchantment.DEPTH_STRIDER);
		_enchantments.put("FrostWalker", Enchantment.FROST_WALKER);
		_enchantments.put("CurseOfBinding", Enchantment.BINDING_CURSE);
		_enchantments.put("Sharpness", Enchantment.DAMAGE_ALL);
		_enchantments.put("Smite", Enchantment.DAMAGE_UNDEAD);
		_enchantments.put("BaneOfArthropods", Enchantment.DAMAGE_ARTHROPODS);
		_enchantments.put("Knockback", Enchantment.KNOCKBACK);
		_enchantments.put("FireAspect", Enchantment.FIRE_ASPECT);
		_enchantments.put("Looting", Enchantment.LOOT_BONUS_MOBS);
		_enchantments.put("Efficiency", Enchantment.DIG_SPEED);
		_enchantments.put("SilkTouch", Enchantment.SILK_TOUCH);
		_enchantments.put("Unbreaking", Enchantment.DURABILITY);
		_enchantments.put("Fortune", Enchantment.LOOT_BONUS_BLOCKS);
		_enchantments.put("Power", Enchantment.ARROW_DAMAGE);
		_enchantments.put("Punch", Enchantment.ARROW_KNOCKBACK);
		_enchantments.put("Flame", Enchantment.ARROW_FIRE);
		_enchantments.put("Infinity", Enchantment.ARROW_INFINITE);
		_enchantments.put("LuckOfTheSea", Enchantment.LUCK);
		_enchantments.put("Lure", Enchantment.LURE);
		_enchantments.put("Mending", Enchantment.MENDING);
		_enchantments.put("CurseOfVanishing", Enchantment.VANISHING_CURSE);
		_enchantments.put("SweepingEdge", Enchantment.SWEEPING_EDGE);
		_enchantments.put("Impaling", Enchantment.IMPALING);
		_enchantments.put("Riptide", Enchantment.RIPTIDE);
		_enchantments.put("Channeling", Enchantment.CHANNELING);
		_enchantments.put("Loyalty", Enchantment.LOYALTY);

		List<String> enchantmentNames = new ArrayList<String>(_enchantments.names());
		Collections.sort(enchantmentNames, String.CASE_INSENSITIVE_ORDER);
		_enchantmentNames = Collections.unmodifiableList(enchantmentNames);

		_enchantmentNamesAsString = StringUtils.join(_enchantmentNames, ", ");
	}

	public static Enchantment getByName(String name) {
		return _enchantments.getByName(name);
	}

	public static String getName(Enchantment enchantment) {
		return _enchantments.getName(enchantment);
	}

	public static List<String> getNames() {
		return _enchantmentNames;
	}

	public static String getNamesAsString() {
		return _enchantmentNamesAsString;
	}

	private EnchantmentsMap() { }

}
