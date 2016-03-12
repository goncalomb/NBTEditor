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

package com.goncalomb.bukkit.nbteditor.commands;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;

import com.goncalomb.bukkit.mylib.namemaps.EnchantmentsMap;
import com.goncalomb.bukkit.mylib.namemaps.PotionEffectsMap;
import com.goncalomb.bukkit.nbteditor.nbt.attributes.ItemModifier;

public class ItemUtils {

	public static void sendItemStackInformation(ItemStack stack, CommandSender sender) {
		ItemMeta meta = stack.getItemMeta();
		sender.sendMessage("§eItem information:");
		sender.sendMessage("  §2Type: §a" + stack.getType() + ":" + stack.getDurability() + " (" + stack.getTypeId() + ":" + stack.getDurability() + ")");
		sender.sendMessage("  §2Amount: §a" + stack.getAmount());
		String name = meta.getDisplayName();
		if (name != null) {
			sender.sendMessage("  §2Name: §r" + name);
		}
		List<String> lore = meta.getLore();
		if (lore != null && lore.size() > 0) {
			sender.sendMessage("§eLore:");
			for (String line : lore) {
				sender.sendMessage("  " + line);
			}
		}
		if (meta instanceof BookMeta && stack.getType() == Material.WRITTEN_BOOK) {
			sender.sendMessage("§eBook information:");
			sender.sendMessage("  §2Title: §r" + ((BookMeta) meta).getTitle());
			sender.sendMessage("  §2Autor: §r" + ((BookMeta) meta).getAuthor());
			sender.sendMessage("  §2Page #: §r" + ((BookMeta) meta).getPageCount());
		} else if (meta instanceof PotionMeta) {
			Collection<PotionEffect> effects = ((PotionMeta) meta).getCustomEffects();
			if (effects.isEmpty()) {
				effects = Potion.fromItemStack(stack).getEffects();
			}
			sender.sendMessage("§ePotion effects:");
			for (PotionEffect effect : effects) {
				sender.sendMessage("  §a" + PotionEffectsMap.getName(effect.getType()) + " " + (effect.getAmplifier() + 1) + " (" + (effect.getDuration()/20f) + "s)");
			}
		} else if (meta instanceof EnchantmentStorageMeta) {
			Map<Enchantment, Integer> enchants = ((EnchantmentStorageMeta) meta).getStoredEnchants();
			if (enchants.size() > 0) {
				sender.sendMessage("§eStored Enchantments:");
				for (Entry<Enchantment, Integer> entry : enchants.entrySet()) {
					sender.sendMessage("  §a" + EnchantmentsMap.getName(entry.getKey()) + " " + entry.getValue());
				}
			}
		} else if (meta instanceof LeatherArmorMeta) {
			sender.sendMessage("§eLeather armor color:");
			Color color = ((LeatherArmorMeta) meta).getColor();
			sender.sendMessage("  §a" + String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue()));
		}
		Map<Enchantment, Integer> enchants = meta.getEnchants();
		if (enchants.size() > 0) {
			sender.sendMessage("§eEnchantments:");
			for (Entry<Enchantment, Integer> entry : enchants.entrySet()) {
				sender.sendMessage("  §a" + EnchantmentsMap.getName(entry.getKey()) + " " + entry.getValue());
			}
		}
		List<ItemModifier> modifiers = ItemModifier.getItemStackModifiers(stack);
		if (modifiers.size() > 0) {
			sender.sendMessage("§eModifiers:");
			for (ItemModifier modifier : modifiers) {
				sender.sendMessage("  §a" + modifier.getName());
				sender.sendMessage("    §2Attribute: §a" + modifier.getAttributeType() + " §2Operation: §a" + modifier.getOperation());
				sender.sendMessage("    §2Amount: §a" + modifier.getAmount());
			}
		}
	}

	private ItemUtils() { }

}
