/*
 * Copyright (C) 2014 - Gonçalo Baltazar <http://goncalomb.com>
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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommand;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandException;
import com.goncalomb.bukkit.bkglib.utils.Utils;
import com.goncalomb.bukkit.nbteditor.ItemStorage;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public class CommandItemStorage extends BKgCommand {

	public CommandItemStorage() {
		super("itemstorage", "is");
	}
	
	private static void validateName(String name) throws BKgCommandException {
		if (!ItemStorage.isValidName(name)) {
			throw new BKgCommandException(Lang._(NBTEditor.class, "commands.itemstorage.invalid-name"));
		}
	}
	
	private static void checkItemExistance(String name) throws BKgCommandException {
		if (!ItemStorage.existsItem(name)) {
			throw new BKgCommandException(Lang._(NBTEditor.class, "commands.itemstorage.not-found"));
		}
	}
	
	@Command(args = "store", type = CommandType.PLAYER_ONLY, minargs = 1, usage = "<name>")
	public boolean command_store(CommandSender sender, String[] args) throws BKgCommandException {
		ItemStack item = ((Player) sender).getItemInHand();
		if (item == null || item.getType() == Material.AIR) {
			sender.sendMessage(Lang._(NBTEditor.class, "commands.itemstorage.not-holding"));
		} else {
			validateName(args[0]);
			if (ItemStorage.addItem(item, args[0])) {
				sender.sendMessage(Lang._(NBTEditor.class, "commands.itemstorage.stored"));
			} else {
				sender.sendMessage(Lang._(NBTEditor.class, "commands.itemstorage.found"));
			}
		}
		return true;
	}
	
	@Command(args = "get", type = CommandType.DEFAULT, minargs = 1, maxargs = 2, usage = "<name> [player]")
	public boolean command_get(CommandSender sender, String[] args) throws BKgCommandException {
		validateName(args[0]);
		checkItemExistance(args[0]);
		Player player;
		if (args.length == 2) {
			player = CommandUtils.findPlayer(args[1]);
		} else if (sender instanceof Player) {
			player = (Player) sender;
		} else {
			sender.sendMessage(Lang._(NBTEditor.class, "commands.itemstorage.no-player"));
			return false;
		}
		CommandUtils.giveItem(player, ItemStorage.getItem(args[0]));
		sender.sendMessage(Lang._(NBTEditor.class, "commands.itemstorage.got"));
		return true;
	}
	
	@TabComplete(args = "get")
	public List<String> tabcomplete_get(CommandSender sender, String[] args) {
		if (args.length == 1) {
			return Utils.getElementsWithPrefix(ItemStorage.listItems(), args[0]);
		}
		return CommandUtils.playerTabComplete(sender, args[1]);
	}
	
	@Command(args = "info", type = CommandType.DEFAULT, minargs = 1, usage = "<name>")
	public boolean command_info(CommandSender sender, String[] args) throws BKgCommandException {
		validateName(args[0]);
		checkItemExistance(args[0]);
		ItemUtils.sendItemStackInformation(ItemStorage.getItem(args[0]), sender);
		return true;
	}
	
	@TabComplete(args = "info")
	public List<String> tabcomplete_info(CommandSender sender, String[] args) {
		return Utils.getElementsWithPrefix(ItemStorage.listItems(), args[0]);
	}
	
	@Command(args = "update", type = CommandType.DEFAULT, minargs = 1, usage = "<name>")
	public boolean command_update(CommandSender sender, String[] args) throws BKgCommandException {
		ItemStack item = ((Player) sender).getItemInHand();
		if (item == null || item.getType() == Material.AIR) {
			sender.sendMessage(Lang._(NBTEditor.class, "commands.itemstorage.not-holding"));
		} else {
			validateName(args[0]);
			checkItemExistance(args[0]);
			ItemStack storedItem = ItemStorage.getItem(args[0]);
			if (item.getType() != storedItem.getType()) {
				sender.sendMessage(Lang._(NBTEditor.class, "commands.itemstorage.different-type", storedItem.getType()));
				return true;
			}
			String itemName = item.getItemMeta().getDisplayName();
			String storedItemName = storedItem.getItemMeta().getDisplayName();
			itemName = (itemName == null ? "" : itemName);
			storedItemName = (storedItemName == null ? "" : storedItemName);
			if (!itemName.equals(storedItemName)) {
				sender.sendMessage(Lang._(NBTEditor.class, "commands.itemstorage.different-name", storedItemName));
				return true;
			}
			ItemStorage.removeItem(args[0]);
			ItemStorage.addItem(item, args[0]);
			sender.sendMessage(Lang._(NBTEditor.class, "commands.itemstorage.updated"));
		}
		return true;
	}
	
	@TabComplete(args = "update")
	public List<String> tabcomplete_update(CommandSender sender, String[] args) {
		return Utils.getElementsWithPrefix(ItemStorage.listItems(), args[0]);
	}
	
	@Command(args = "remove", type = CommandType.PLAYER_ONLY, minargs = 1, usage = "<name>")
	public boolean command_remove(CommandSender sender, String[] args) throws BKgCommandException {
		validateName(args[0]);
		checkItemExistance(args[0]);
		ItemStorage.removeItem(args[0]);
		sender.sendMessage(Lang._(NBTEditor.class, "commands.itemstorage.removed"));
		return true;
	}
	
	@TabComplete(args = "remove")
	public List<String> tabcomplete_remove(CommandSender sender, String[] args) {
		return Utils.getElementsWithPrefix(ItemStorage.listItems(), args[0]);
	}
	
	@Command(args = "list", type = CommandType.PLAYER_ONLY)
	public boolean command_list(CommandSender sender, String[] args) throws BKgCommandException {
		sender.sendMessage(Lang._(NBTEditor.class, "storeditems-prefix") + StringUtils.join(ItemStorage.listItems(), ", "));
		return true;
	}
	
}
