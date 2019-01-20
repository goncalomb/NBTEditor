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

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.command.MyCommand;
import com.goncalomb.bukkit.mylib.command.MyCommandException;
import com.goncalomb.bukkit.mylib.utils.Utils;
import com.goncalomb.bukkit.nbteditor.ItemStorage;

public class CommandItemStorage extends MyCommand {

	public CommandItemStorage() {
		super("itemstorage", "is");
	}

	private static void validateName(String name) throws MyCommandException {
		if (!ItemStorage.isValidName(name)) {
			throw new MyCommandException("§cInvalid name. Use [0-9a-zA-Z_-], 64 characters max.");
		}
	}

	private static void checkItemExistance(String name) throws MyCommandException {
		if (!ItemStorage.existsItem(name)) {
			throw new MyCommandException("§cThat item does not exist.");
		}
	}

	@Command(args = "store", type = CommandType.PLAYER_ONLY, minargs = 1, usage = "<name>")
	public boolean command_store(CommandSender sender, String[] args) throws MyCommandException {
		ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
		if (item == null || item.getType() == Material.AIR) {
			sender.sendMessage("§cYou must be holding an item.");
		} else {
			validateName(args[0]);
			if (ItemStorage.addItem(item, args[0])) {
				sender.sendMessage("§aStored.");
			} else {
				sender.sendMessage("§cDuplicate name.");
			}
		}
		return true;
	}

	@Command(args = "get", type = CommandType.DEFAULT, minargs = 1, maxargs = 2, usage = "<name> [player]")
	public boolean command_get(CommandSender sender, String[] args) throws MyCommandException {
		validateName(args[0]);
		checkItemExistance(args[0]);
		Player player;
		if (args.length == 2) {
			player = CommandUtils.findPlayer(args[1]);
		} else if (sender instanceof Player) {
			player = (Player) sender;
		} else {
			sender.sendMessage("§cArgument 'player' missing!");
			return false;
		}
		CommandUtils.giveItem(player, ItemStorage.getItem(args[0]));
		if (!CommandType.CONSOLE_ONLY.isValidSender(sender)) {
			sender.sendMessage("§aDone.");
		}
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
	public boolean command_info(CommandSender sender, String[] args) throws MyCommandException {
		validateName(args[0]);
		checkItemExistance(args[0]);
		ItemUtils.sendItemStackInformation(ItemStorage.getItem(args[0]), sender);
		return true;
	}

	@TabComplete(args = "info")
	public List<String> tabcomplete_info(CommandSender sender, String[] args) {
		return Utils.getElementsWithPrefix(ItemStorage.listItems(), args[0]);
	}

	@Command(args = "update", type = CommandType.PLAYER_ONLY, minargs = 1, usage = "<name>")
	public boolean command_update(CommandSender sender, String[] args) throws MyCommandException {
		ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
		if (item == null || item.getType() == Material.AIR) {
			sender.sendMessage("§cYou must be holding an item.");
		} else {
			validateName(args[0]);
			checkItemExistance(args[0]);
			ItemStack storedItem = ItemStorage.getItem(args[0]);
			if (item.getType() != storedItem.getType()) {
				sender.sendMessage(MessageFormat.format("§cThe stored item is from a different type, §e{0}§c, cannot update. Remove it first.", storedItem.getType()));
				return true;
			}
			String itemName = item.getItemMeta().getDisplayName();
			String storedItemName = storedItem.getItemMeta().getDisplayName();
			itemName = (itemName == null ? "" : itemName);
			storedItemName = (storedItemName == null ? "" : storedItemName);
			if (!itemName.equals(storedItemName)) {
				sender.sendMessage(MessageFormat.format("§cThe stored item has a different name, §e\"§r{0}§r§e\"§c, cannot update. Remove it first.", storedItemName));
				return true;
			}
			ItemStorage.removeItem(args[0]);
			ItemStorage.addItem(item, args[0]);
			sender.sendMessage("§aUpdated.");
		}
		return true;
	}

	@TabComplete(args = "update")
	public List<String> tabcomplete_update(CommandSender sender, String[] args) {
		return Utils.getElementsWithPrefix(ItemStorage.listItems(), args[0]);
	}

	@Command(args = "remove", type = CommandType.PLAYER_ONLY, minargs = 1, usage = "<name>")
	public boolean command_remove(CommandSender sender, String[] args) throws MyCommandException {
		validateName(args[0]);
		checkItemExistance(args[0]);
		ItemStorage.removeItem(args[0]);
		sender.sendMessage("§aRemoved.");
		return true;
	}

	@TabComplete(args = "remove")
	public List<String> tabcomplete_remove(CommandSender sender, String[] args) {
		return Utils.getElementsWithPrefix(ItemStorage.listItems(), args[0]);
	}

	@Command(args = "list", type = CommandType.DEFAULT)
	public boolean command_list(CommandSender sender, String[] args) throws MyCommandException {
		sender.sendMessage("§7Stored items: " + StringUtils.join(ItemStorage.listItems(), ", "));
		return true;
	}

	@Command(args = "open", type = CommandType.PLAYER_ONLY)
	public boolean command_open(CommandSender sender, String[] args) throws MyCommandException {
		(new InventoryForItemStorage((Player) sender)).openInventory((Player) sender, getOwner());
		return true;
	}

}
