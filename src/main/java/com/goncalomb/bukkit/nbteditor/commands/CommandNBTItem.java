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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.command.MyCommandException;
import com.goncalomb.bukkit.mylib.command.MyCommandManager;
import com.goncalomb.bukkit.mylib.reflect.BukkitReflect;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;
import com.goncalomb.bukkit.mylib.utils.Utils;
import com.goncalomb.bukkit.mylib.utils.UtilsMc;
import com.goncalomb.bukkit.nbteditor.ItemStackNBTWrapper;
import com.goncalomb.bukkit.nbteditor.nbt.attributes.AttributeType;
import com.goncalomb.bukkit.nbteditor.nbt.attributes.ItemModifier;

public class CommandNBTItem  extends AbstractNBTCommand<ItemStackNBTWrapper> {

	private static List<String> _modifierSlots = Arrays.asList(new String[] { "any", "mainhand", "offhand", "feet", "legs", "chest", "head" });

	public CommandNBTItem() {
		super("nbtitem", "nbti");
	}

	@Override
	protected ItemStackNBTWrapper getWrapper(Player player) throws MyCommandException {
		return new ItemStackNBTWrapper(player.getInventory().getItemInMainHand());
	}

	@Override
	@Command(args = "info", type = CommandType.PLAYER_ONLY)
	public boolean info_Command(CommandSender sender, String[] args) throws MyCommandException {
		return super.info_Command(sender, args);
	}

	@Override
	@Command(args = "var", type = CommandType.PLAYER_ONLY, minargs = 1, maxargs = Integer.MAX_VALUE, usage = "<variable> ...")
	public boolean var_Command(CommandSender sender, String[] args) throws MyCommandException {
		return super.var_Command(sender, args);
	}

	@Override
	@TabComplete(args = "var")
	public List<String> var_TabComplete(CommandSender sender, String[] args) {
		return super.var_TabComplete(sender, args);
	}

	@Override
	@Command(args = "clearvar", type = CommandType.PLAYER_ONLY, minargs = 1, usage = "<variable>")
	public boolean clearvar_Command(CommandSender sender, String[] args) throws MyCommandException {
		return super.clearvar_Command(sender, args);
	}

	@Override
	@TabComplete(args = "clearvar")
	public List<String> clearvar_TabComplete(CommandSender sender, String[] args) {
		return super.clearvar_TabComplete(sender, args);
	}

/*
	@Command(args = "info", type = CommandType.PLAYER_ONLY)
	public boolean infoCommand(CommandSender sender, String[] args) throws MyCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		ItemUtils.sendItemStackInformation(item.item, sender);
		return true;
	}

	@Command(args = "name", type = CommandType.PLAYER_ONLY, maxargs = Integer.MAX_VALUE, usage = "[name ...]")
	public boolean nameCommand(CommandSender sender, String[] args) throws MyCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		item.meta.setDisplayName(args.length == 0 ? null : UtilsMc.parseColors(StringUtils.join(args, " ")));
		item.save();
		sender.sendMessage(args.length == 0 ? "§aItem name removed." : "§aItem renamed.");
		return true;
	}

	@Command(args = "lore add", type = CommandType.PLAYER_ONLY, minargs = 1, maxargs = Integer.MAX_VALUE, usage = "<lore ...>")
	public boolean lore_addCommand(CommandSender sender, String[] args) throws MyCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		List<String> lores = item.meta.getLore();
		if (lores == null) {
			lores = new ArrayList<String>();
		}
		lores.add(UtilsMc.parseColors(StringUtils.join(args, " ")));
		item.meta.setLore(lores);
		item.save();
		sender.sendMessage("§aItem lore line added.");
		return true;
	}

	@Command(args = "lore set", type = CommandType.PLAYER_ONLY, minargs = 2, maxargs = Integer.MAX_VALUE, usage = "<index> <lore ...>")
	public boolean lore_setCommand(CommandSender sender, String[] args) throws MyCommandException{
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player)sender);
		List<String> loreList = item.meta.getLore();
		if(loreList == null){
			loreList = new ArrayList<String>();
		}
		Integer index = Utils.parseInt(args[0], -1);
		if (index < 1) {
			sender.sendMessage("§cInvalid index. The index is an integer greater than 0.");
		} else if (loreList.size() >= index) {
			loreList.set(index - 1, UtilsMc.parseColors(StringUtils.join(args, " ", 1, args.length)));
			item.meta.setLore(loreList);
			item.save();
			sender.sendMessage("§aItem lore set.");
		} else {
			sender.sendMessage(MessageFormat.format("§cLore line with index {0} doesn''t exist!", index));
		}
		return true;
	}

	@Command(args = "lore del", type = CommandType.PLAYER_ONLY, minargs = 1, usage = "<index>")
	public boolean lore_delCommand(CommandSender sender, String[] args) throws MyCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		List<String> lores = item.meta.getLore();
		int index = Utils.parseInt(args[0], -1);
		if (index < 1) {
			sender.sendMessage("§cInvalid index. The index is an integer greater than 0.");
		} else if (lores == null || index > lores.size()) {
			sender.sendMessage(MessageFormat.format("§cLore line with index {0} doesn''t exist!", index));
		} else {
			lores.remove(index - 1);
			item.meta.setLore(lores);
			item.save();
			sender.sendMessage("§aItem lore line removed.");
		}
		return true;
	}

	@Command(args = "lore delall", type = CommandType.PLAYER_ONLY)
	public boolean lore_delallCommand(CommandSender sender, String[] args) throws MyCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		item.meta.setLore(null);
		item.save();
		sender.sendMessage("§aItem lore cleared.");
		return true;
	}
*/
	@Command(args = "mod add", type = CommandType.PLAYER_ONLY, maxargs = Integer.MAX_VALUE, usage = "<attribute> <operation> <amount> [slot] [name ...]")
	public boolean mod_add(CommandSender sender, String[] args) throws MyCommandException {
		if (args.length >= 3) {
			HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
			AttributeType attributeType = AttributeType.getByName(args[0]);
			if (attributeType == null) {
				sender.sendMessage("§cInvalid attribute!");
			} else {
				int operation = Utils.parseInt(args[1], 2, 0, -1);
				if (operation == -1) {
					sender.sendMessage("§cOperation must be 0, 1 or 2!");
					return true;
				} else {
					double amount;
					try {
						amount = Double.parseDouble(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage("§cAmount must be a number!");
						return true;
					}
					String slot = null;
					if (args.length > 3 && !args[3].equals("any")) {
						slot = args[3];
						if (!_modifierSlots.contains(slot)) {
							sender.sendMessage("§cInvalid modifier slot!");
							return true;
						}
					}
					String name = "Modifier";
					if (args.length > 4) {
						name = StringUtils.join(args, " ", 4, args.length);
					}
					List<ItemModifier> modifiers = ItemModifier.getItemStackModifiers(item.item);
					modifiers.add(new ItemModifier(attributeType, name, amount, operation, slot));
					ItemModifier.setItemStackModifiers(item.item, modifiers);
					sender.sendMessage("§aItem modifier added.");
					return true;
				}
			}
		}
		sender.sendMessage("§7Attributes: " + StringUtils.join(AttributeType.values(), ", "));
		return false;
	}

	@TabComplete(args = "mod add")
	public List<String> tab_mod_add(CommandSender sender, String[] args) {
		if (args.length == 1) {
			return Utils.getElementsWithPrefixGeneric(Arrays.asList(AttributeType.values()), args[0], true);
		} else if (args.length == 2) {
			return Utils.getElementsWithPrefix(Arrays.asList(new String[] { "0", "1", "2" }), args[1]);
		} else if (args.length == 4) {
			return Utils.getElementsWithPrefix(_modifierSlots, args[3]);
		}
		return null;
	}

	@Command(args = "mod del", type = CommandType.PLAYER_ONLY, minargs = 1, usage = "<index>")
	public boolean mod_delCommand(CommandSender sender, String[] args) throws MyCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		List<ItemModifier> modifiers = ItemModifier.getItemStackModifiers(item.item);
		int index = Utils.parseInt(args[0], -1);
		if (index < 1) {
			sender.sendMessage("§cInvalid index. The index is an integer greater than 0.");
		} else if (index > modifiers.size()) {
			sender.sendMessage(MessageFormat.format("§cModifier with index {0} doesn''t exist!", index));
		} else {
			modifiers.remove(index - 1);
			ItemModifier.setItemStackModifiers(item.item, modifiers);
			sender.sendMessage("§aItem modifier removed.");
		}
		return true;
	}

	@Command(args = "mod delall", type = CommandType.PLAYER_ONLY)
	public boolean mod_delallCommand(CommandSender sender, String[] args) throws MyCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		ItemModifier.setItemStackModifiers(item.item, new ArrayList<ItemModifier>());
		sender.sendMessage("§aModifiers cleared.");
		return true;
	}

	@Command(args = "tocommand", type = CommandType.PLAYER_ONLY)
	public boolean tocommandCommand(CommandSender sender, String[] args) throws MyCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		Block block = UtilsMc.getTargetBlock((Player) sender, 5);
		BlockState state = block.getState();
		if (state instanceof CommandBlock) {
			String command = "give";
			if (!MyCommandManager.isVanillaCommand(command)) {
				sender.sendMessage(MessageFormat.format("§7Non-vanilla /{0} command detected, using /minecraft:{0}.", command));
				command = "minecraft:" + command;
			}
			command = "/" + command + " @p " + BukkitReflect.getMaterialName(item.item.getType()) + " " + item.item.getAmount() + " " + item.item.getDurability() + " " + NBTUtils.getItemStackTag(item.item).toString();
			// We spare 50 characters of space so people can change the player.
			if (command.length() > 32767 - 50) {
				sender.sendMessage("§cItem too complex!");
				return true;
			}
			((CommandBlock) state).setCommand(command);
			state.update();
			sender.sendMessage("§aCommand set.");
			return true;
		}
		sender.sendMessage("§cNo Command Block in sight!");
		return true;
	}

}