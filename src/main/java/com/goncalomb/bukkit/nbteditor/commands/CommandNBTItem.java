/*
 * Copyright (C) 2013, 2014 - Gonçalo Baltazar <http://goncalomb.com>
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
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommand;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandException;
import com.goncalomb.bukkit.bkglib.utils.Utils;
import com.goncalomb.bukkit.bkglib.utils.UtilsMc;
import com.goncalomb.bukkit.nbteditor.NBTEditor;
import com.goncalomb.bukkit.nbteditor.nbt.attributes.AttributeType;
import com.goncalomb.bukkit.nbteditor.nbt.attributes.ItemModifier;

public class CommandNBTItem extends BKgCommand {
	
	public CommandNBTItem() {
		super("nbtitem", "nbti");
	}
	
	@Command(args = "info", type = CommandType.PLAYER_ONLY)
	public boolean infoCommand(CommandSender sender, String[] args) throws BKgCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		ItemUtils.sendItemStackInformation(item.item, sender);
		return true;
	}
	
	@Command(args = "name", type = CommandType.PLAYER_ONLY, maxargs = Integer.MAX_VALUE, usage = "[name]")
	public boolean nameCommand(CommandSender sender, String[] args) throws BKgCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		item.meta.setDisplayName(args.length == 0 ? null : UtilsMc.parseColors(StringUtils.join(args, " ")));
		item.save();
		sender.sendMessage(args.length == 0 ? Lang._(NBTEditor.class, "commands.nbtitem.name-removed") : Lang._(NBTEditor.class, "commands.nbtitem.renamed"));
		return true;
	}
	
	@Command(args = "lore add", type = CommandType.PLAYER_ONLY, minargs = 1, maxargs = Integer.MAX_VALUE, usage = "<lore>")
	public boolean lore_addCommand(CommandSender sender, String[] args) throws BKgCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		List<String> lores = item.meta.getLore();
		if (lores == null) {
			lores = new ArrayList<String>();
		}
		lores.add(UtilsMc.parseColors(StringUtils.join(args, " ")));
		item.meta.setLore(lores);
		item.save();
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtitem.lore-added"));
		return true;
	}
	
	@Command(args = "lore del", type = CommandType.PLAYER_ONLY, minargs = 1, usage = "<index>")
	public boolean lore_delCommand(CommandSender sender, String[] args) throws BKgCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		List<String> lores = item.meta.getLore();
		int index = Utils.parseInt(args[0], -1);
		if (index < 1) {
			sender.sendMessage(Lang._(NBTEditor.class, "invalid-index"));
		} else if (lores == null || index > lores.size()) {
			sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtitem.lore-nop", index));
		} else {
			lores.remove(index - 1);
			item.meta.setLore(lores);
			item.save();
			sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtitem.lore-removed"));
		}
		return true;
	}
	
	@Command(args = "lore delall", type = CommandType.PLAYER_ONLY)
	public boolean lore_delallCommand(CommandSender sender, String[] args) throws BKgCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		item.meta.setLore(null);
		item.save();
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtitem.lore-cleared"));
		return true;
	}
	
	@Command(args = "mod add", type = CommandType.PLAYER_ONLY, maxargs = Integer.MAX_VALUE, usage = "<attribute> <operation> <amount> [name ...]")
	public boolean mod_add(CommandSender sender, String[] args) throws BKgCommandException {
		if (args.length >= 3) {
			HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
			AttributeType attributeType = AttributeType.getByName(args[0]);
			if (attributeType == null) {
				sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtitem.mod-invalid-attr"));
			} else {
				int operation = Utils.parseInt(args[1], 2, 0, -1);
				if (operation == -1) {
					sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtitem.mod-invalid-op"));
					return true;
				} else {
					double amount;
					try {
						amount = Double.parseDouble(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtitem.mod-invalid-amount"));
						return true;
					}
					String name = "Modifier";
					if (args.length > 3) {
						name = StringUtils.join(args, " ", 3, args.length);
					}
					List<ItemModifier> modifiers = ItemModifier.getItemStackModifiers(item.item);
					modifiers.add(new ItemModifier(attributeType, name, amount, operation));
					ItemModifier.setItemStackModifiers(item.item, modifiers);
					sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtitem.mod-added"));
					return true;
				}
			}
		}
		sender.sendMessage(Lang._(NBTEditor.class, "attributes-prefix") + StringUtils.join(AttributeType.values(), ", "));
		return false;
	}
	
	@TabComplete(args = "mod add")
	public List<String> tab_mod_add(CommandSender sender, String[] args) {
		if (args.length == 1) {
			return Utils.getElementsWithPrefixGeneric(Arrays.asList(AttributeType.values()), args[0], true);
		} else if (args.length == 2) {
			return Utils.getElementsWithPrefix(Arrays.asList(new String[] { "0", "1", "2" }), args[1]);
		}
		return null;
	}
	
	@Command(args = "mod del", type = CommandType.PLAYER_ONLY, minargs = 1, usage = "<index>")
	public boolean mod_delCommand(CommandSender sender, String[] args) throws BKgCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		List<ItemModifier> modifiers = ItemModifier.getItemStackModifiers(item.item);
		int index = Utils.parseInt(args[0], -1);
		if (index < 1) {
			sender.sendMessage(Lang._(NBTEditor.class, "invalid-index"));
		} else if (index > modifiers.size()) {
			sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtitem.mod-nop", index));
		} else {
			modifiers.remove(index - 1);
			ItemModifier.setItemStackModifiers(item.item, modifiers);
			sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtitem.mod-removed"));
		}
		return true;
	}
	
	@Command(args = "mod delall", type = CommandType.PLAYER_ONLY)
	public boolean mod_delallCommand(CommandSender sender, String[] args) throws BKgCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		ItemModifier.setItemStackModifiers(item.item, new ArrayList<ItemModifier>());
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtitem.mod-cleared"));
		return true;
	}
	
}