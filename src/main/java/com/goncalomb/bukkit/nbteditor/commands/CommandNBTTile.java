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

package com.goncalomb.bukkit.nbteditor.commands;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.goncalomb.bukkit.mylib.command.MyCommand;
import com.goncalomb.bukkit.mylib.command.MyCommandException;
import com.goncalomb.bukkit.mylib.command.MyCommandManager;
import com.goncalomb.bukkit.mylib.namemaps.PotionEffectsMap;
import com.goncalomb.bukkit.mylib.reflect.BukkitReflect;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;
import com.goncalomb.bukkit.mylib.utils.Utils;
import com.goncalomb.bukkit.mylib.utils.UtilsMc;
import com.goncalomb.bukkit.nbteditor.nbt.BeaconNBTWrapper;
import com.goncalomb.bukkit.nbteditor.nbt.JukeboxNBTWrapper;
import com.goncalomb.bukkit.nbteditor.nbt.TileNBTWrapper;

public class CommandNBTTile extends MyCommand {
	
	public CommandNBTTile() {
		super("nbttile", "nbtt");
	}
	
	private static BeaconNBTWrapper getBeacon(Player player) throws MyCommandException {
		Block block = UtilsMc.getTargetBlock(player, 5);
		if (block.getType() != Material.BEACON) {
			throw new MyCommandException("§cNo beacon in sight!");
		}
		return new BeaconNBTWrapper(block);
	}
	
	private static JukeboxNBTWrapper getJukebox(Player player) throws MyCommandException {
		Block block = UtilsMc.getTargetBlock(player, 5);
		if (block.getType() != Material.JUKEBOX) {
			throw new MyCommandException("§cNo jukebox in sight!");
		}
		return new JukeboxNBTWrapper(block);
	}
	
	@Command(args = "beacon", type = CommandType.PLAYER_ONLY, minargs = 0, maxargs = 2, usage = "primary/secondary <effect>")
	public boolean beaconEffectCommand(CommandSender sender, String[] args) throws MyCommandException {
		if (args.length == 2 && (args[0].equalsIgnoreCase("primary") || args[0].equalsIgnoreCase("secondary"))) {
			BeaconNBTWrapper beacon = getBeacon((Player) sender);
			PotionEffectType effect = null;
			boolean clear = args[1].equalsIgnoreCase("clear");
			if (!clear) {
				effect = PotionEffectsMap.getByName(args[1]);
				if (effect == null) {
					sender.sendMessage("§cInvalid effect!");
				}
			}
			if (clear || effect != null) {
				if (args[0].equalsIgnoreCase("primary")) {
					beacon.setPrimary(effect);
				} else {
					beacon.setSecondary(effect);
				}
				beacon.save();
				sender.sendMessage(MessageFormat.format((clear ? "§aEffect cleared ({0})." : "§aEffect set ({0})."), args[0].toLowerCase()));
				return true;
			}
		}
		sender.sendMessage("§7Effects: " + PotionEffectsMap.getNamesAsString());
		sender.sendMessage("§eUse 'clear' as affect clear the effect.");
		return false;
	}
	
	@TabComplete(args = "beacon")
	public List<String> tab_beacon(CommandSender sender, String[] args) {
		if (args.length == 1) {
			return Utils.getElementsWithPrefix(Arrays.asList(new String[] { "primary", "secondary" }), args[0]);
		} else if (args.length == 2) {
			return Utils.getElementsWithPrefix(PotionEffectsMap.getNames(), args[1]);
		}
		return null;
	}
	
	@Command(args = "record", type = CommandType.PLAYER_ONLY)
	public boolean setRecordCommand(CommandSender sender, String[] args) throws MyCommandException {
		JukeboxNBTWrapper jukebox = getJukebox((Player) sender);
		ItemStack item = ((Player) sender).getItemInHand();
		jukebox.setRecord(item);
		jukebox.save();
		if (item == null || item.getType() == Material.AIR) {
			sender.sendMessage("§aRecord cleared.");
		} else {
			sender.sendMessage("§aRecord set.");
		}
		return true;
	}
	
	@Command(args = "name", type = CommandType.PLAYER_ONLY, maxargs = Integer.MAX_VALUE, usage = "[name ...]")
	public boolean nameCommand(CommandSender sender, String[] args) throws MyCommandException {
		Block block = UtilsMc.getTargetBlock((Player) sender, 5);
		if (TileNBTWrapper.allowsCustomName(block.getType())) {
			TileNBTWrapper tile = new TileNBTWrapper(block);
			tile.setCustomName(args.length == 0 ? null : UtilsMc.parseColors(StringUtils.join(args, " ")));
			tile.save();
			sender.sendMessage(args.length == 0 ? "§aName cleared." : "§aName set.");
		} else {
			sender.sendMessage("§cYou must be looking at a Chest, Furnace, Dispenser, Dropper, Hopper, Brewing Stand, Enchantment Table or Commmand Block!");
		}
		return true;
	}
	
	@Command(args = "command-colors", type = CommandType.PLAYER_ONLY)
	public boolean colorsCommand(CommandSender sender, String[] args) throws MyCommandException {
		Block block = UtilsMc.getTargetBlock((Player) sender, 5);
		if (block.getType() != Material.COMMAND) {
			throw new MyCommandException("§cNo Command Block in sight!");
		}
		CommandBlock commandBlock = (CommandBlock) block.getState();
		commandBlock.setCommand(UtilsMc.parseColors(commandBlock.getCommand()));
		commandBlock.update();
		sender.sendMessage("§aColor codes have been replaced.");
		return true;
	}
	
	@Command(args = "sign", type = CommandType.PLAYER_ONLY, minargs = 2, maxargs = Integer.MAX_VALUE, usage = "<line> [content ...]")
	public boolean signCommand(CommandSender sender, String[] args) throws MyCommandException {
		Block block = UtilsMc.getTargetBlock((Player) sender, 5);
		if (block.getType() != Material.SIGN_POST) {
			throw new MyCommandException("§cNo Sign in sight!");
		}
		int line = CommandUtils.parseInt(args[0], 4, 1);
		Sign sign = (Sign) block.getState();
		sign.setLine(line - 1, UtilsMc.parseColors(StringUtils.join(args, " ", 1, args.length)));
		sign.update();
		sender.sendMessage("§aLine set.");
		return true;
	}
	
	@Command(args = "tocommand", type = CommandType.PLAYER_ONLY)
	public boolean tocommandCommand(CommandSender sender, String[] args) throws MyCommandException {
		Block block = UtilsMc.getTargetBlock((Player) sender, 5);
		if (block == null || block.getType() == Material.AIR) {
			sender.sendMessage("§cNo block in sight!");
			return true;
		}
		String command = "setblock";
		if (!MyCommandManager.isVanillaCommand(command)) {
			sender.sendMessage(MessageFormat.format("§7Non-vanilla /{0} command detected, using /minecraft:{0}.", command));
			command = "minecraft:" + command;
		}
		command = "/" + command + " " + block.getX() + " " + block.getY() + " " + block.getZ() + " " + BukkitReflect.getMaterialName(block.getType()) + " " + block.getData() + " destroy";
		NBTTagCompound data = NBTUtils.getTileEntityNBTData(block);
		if (data != null) {
			data.remove("id");
			data.remove("x");
			data.remove("y");
			data.remove("z");
			command += " " + data.toString();
			// We spare 50 characters of space so people can change the position.
			if (command.length() > 32767 - 50) {
				sender.sendMessage("§cTile entity too complex!");
				return true;
			}
		}
		Block newBlock = block.getRelative(BlockFace.DOWN);
		newBlock.setType(Material.COMMAND);
		CommandBlock commandBlock = (CommandBlock) newBlock.getState();
		commandBlock.setCommand(command);
		commandBlock.update();
		sender.sendMessage("§aCommand block created below the tile.");
		return true;
	}
	
}
