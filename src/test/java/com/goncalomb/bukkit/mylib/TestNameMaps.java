/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.mylib;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;
import org.junit.Assert;
import org.junit.Test;

import com.goncalomb.bukkit.mylib.namemaps.EnchantmentsMap;
import com.goncalomb.bukkit.mylib.namemaps.PotionEffectsMap;

public class TestNameMaps {
	
	@Test
	public void testEnchantmentsMap() throws IllegalAccessException {
		// Find all bukkit enchantments.
		HashMap<Enchantment, Field> bukkitEnchantments = new HashMap<Enchantment, Field>();
		for (Field field : Enchantment.class.getFields()) {
			if (field.getType() == Enchantment.class && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
				bukkitEnchantments.put((Enchantment) field.get(null), field);
			}
		}
		// Get registered enchantment names.
		List<String> names = EnchantmentsMap.getNames();
		if (bukkitEnchantments.size() < names.size()) {
			Assert.fail("Error finding Enchantment fields.");
		}
		// Remove all registered enchantments.
		for (String name : names) {
			bukkitEnchantments.remove(EnchantmentsMap.getByName(name));
		}
		// Now we should have 0 enchantments, all enchantments are registered.
		int size = bukkitEnchantments.size();
		if (size > 1) {
			Assert.fail(bukkitEnchantments.values().iterator().next().getName() + " and " + (size - 1) + " other(s) are not registered on EnchantmentsMap.");
		} else if (size > 0) {
			Assert.fail(bukkitEnchantments.values().iterator().next().getName() + " is not registered on EnchantmentsMap.");
		}
	}
	
	@Test
	public void testPotionEffectsMap() throws IllegalAccessException {
		// Find all bukkit effects.
		HashMap<PotionEffectType, Field> bukkitPotionEffects = new HashMap<PotionEffectType, Field>();
		for (Field field : PotionEffectType.class.getFields()) {
			if (field.getType() == PotionEffectType.class && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
				bukkitPotionEffects.put((PotionEffectType) field.get(null), field);
			}
		}
		// Get registered effect names.
		List<String> names = PotionEffectsMap.getNames();
		if (bukkitPotionEffects.size() < names.size()) {
			Assert.fail("Error finding PotionEffectType fields.");
		}
		// Remove all registered effects.
		for (String name : names) {
			bukkitPotionEffects.remove(PotionEffectsMap.getByName(name));
		}
		// Now we should have 0 effects, all effects are registered.
		int size = bukkitPotionEffects.size();
		if (size > 1) {
			Assert.fail(bukkitPotionEffects.values().iterator().next().getName() + " and " + (size - 1) + " other(s) are not registered on PotionEffectsMap.");
		} else if (size > 0) {
			Assert.fail(bukkitPotionEffects.values().iterator().next().getName() + " in not registered on PotionEffectsMap.");
		}
	}
	
}
