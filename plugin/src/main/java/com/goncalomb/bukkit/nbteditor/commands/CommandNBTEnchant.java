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

package com.goncalomb.bukkit.nbteditor.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import com.goncalomb.bukkit.mylib.command.MyCommand;
import com.goncalomb.bukkit.mylib.command.MyCommandException;
import com.goncalomb.bukkit.mylib.namemaps.EnchantmentsMap;
import com.goncalomb.bukkit.mylib.utils.Utils;

public class CommandNBTEnchant extends MyCommand {

	public CommandNBTEnchant() {
		super("nbtenchant", "nbte");
	}

	@Command(args = "", type = CommandType.PLAYER_ONLY, maxargs = 2, usage = "<enchantment> [level]")
	public boolean enchantCommand(CommandSender sender, String[] args) throws MyCommandException {
		if (args.length > 0) {
			HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
			Enchantment enchant = EnchantmentsMap.getByName(args[0]);
			if (enchant == null) {
				sender.sendMessage("§cInvalid enchantment.");
			} else {
				int level = enchant.getMaxLevel();
				if (args.length == 2) {
					level = CommandUtils.parseInt(args[1], Short.MAX_VALUE, 0);
				}
				if(level == 0) {
					if (item.item.getType() == Material.ENCHANTED_BOOK) {
						((EnchantmentStorageMeta) item.meta).removeStoredEnchant(enchant);
					} else {
						item.meta.removeEnchant(enchant);
					}
					sender.sendMessage("§aEnchantment removed.");
				} else {
					if (item.item.getType() == Material.ENCHANTED_BOOK) {
						((EnchantmentStorageMeta) item.meta).addStoredEnchant(enchant, level, true);
					} else {
						item.meta.addEnchant(enchant, level, true);
					}
					sender.sendMessage("§aEnchantment added.");
				}
				item.save();
				return true;
			}
		}
		sender.sendMessage("§7Enchantments: " + EnchantmentsMap.getNamesAsString());
		sender.sendMessage("§eUse level = 0 to remove enchantments.");
		return false;
	}

	@TabComplete(args = "")
	public List<String> tab(CommandSender sender, String[] args) {
		if (args.length == 1) {
			List<String> names = new ArrayList<String>(EnchantmentsMap.getNames());
			names.add("clear");
			return Utils.getElementsWithPrefix(names, args[0]);
		}
		return null;
	}

	@Command(args = "clear", type = CommandType.PLAYER_ONLY)
	public boolean enchant_clearCommand(CommandSender sender, String[] args) throws MyCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		for (Enchantment ench : item.meta.getEnchants().keySet()) {
			item.meta.removeEnchant(ench);
		}
		item.save();
		sender.sendMessage("§aAll enchantments removed.");
		return true;
	}

}