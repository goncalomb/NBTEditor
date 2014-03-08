/*
 * Copyright (C) 2013 - Gonçalo Baltazar <http://goncalomb.com>
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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommand;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandException;
import com.goncalomb.bukkit.bkglib.namemaps.PotionEffectsMap;
import com.goncalomb.bukkit.bkglib.utils.Utils;
import com.goncalomb.bukkit.bkglib.utils.UtilsMc;
import com.goncalomb.bukkit.nbteditor.NBTEditor;
import com.goncalomb.bukkit.nbteditor.nbt.BeaconNBTWrapper;
import com.goncalomb.bukkit.nbteditor.nbt.JukeboxNBTWrapper;
import com.goncalomb.bukkit.nbteditor.nbt.TileNBTWrapper;

public class CommandNBTTile extends BKgCommand {
	
	public CommandNBTTile() {
		super("nbttile", "nbtt");
	}
	
	private static BeaconNBTWrapper getBeacon(Player player) throws BKgCommandException {
		Block block = UtilsMc.getTargetBlock(player, 5);
		if (block.getType() != Material.BEACON) {
			throw new BKgCommandException(Lang._(NBTEditor.class, "commands.nbttile.beacon.no-sight"));
		}
		return new BeaconNBTWrapper(block);
	}
	
	private static JukeboxNBTWrapper getJukebox(Player player) throws BKgCommandException {
		Block block = UtilsMc.getTargetBlock(player, 5);
		if (block.getType() != Material.JUKEBOX) {
			throw new BKgCommandException(Lang._(NBTEditor.class, "commands.nbttile.jukebox.no-sight"));
		}
		return new JukeboxNBTWrapper(block);
	}
	
	@Command(args = "beacon", type = CommandType.PLAYER_ONLY, minargs = 0, maxargs = 2, usage = "<primary|secondary> <effect>")
	public boolean beaconEffectCommand(CommandSender sender, String[] args) throws BKgCommandException {
		if (args.length == 2 && (args[0].equalsIgnoreCase("primary") || args[0].equalsIgnoreCase("secondary"))) {
			BeaconNBTWrapper beacon = getBeacon((Player) sender);
			PotionEffectType effect = null;
			boolean clear = args[1].equalsIgnoreCase("clear");
			if (!clear) {
				effect = PotionEffectsMap.getByName(args[1]);
				if (effect == null) {
					sender.sendMessage(Lang._(NBTEditor.class, "commands.nbttile.beacon.invalid-effect"));
				}
			}
			if (clear || effect != null) {
				if (args[0].equalsIgnoreCase("primary")) {
					beacon.setPrimary(effect);
				} else {
					beacon.setSecondary(effect);
				}
				beacon.save();
				sender.sendMessage(Lang._(NBTEditor.class, (clear ? "commands.nbttile.beacon.cleared" : "commands.nbttile.beacon.set"), args[0].toLowerCase()));
				return true;
			}
		}
		sender.sendMessage(Lang._(NBTEditor.class, "effects-prefix") + PotionEffectsMap.getNamesAsString());
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbttile.beacon.info"));
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
	public boolean setRecordCommand(CommandSender sender, String[] args) throws BKgCommandException {
		JukeboxNBTWrapper jukebox = getJukebox((Player) sender);
		ItemStack item = ((Player) sender).getItemInHand();
		jukebox.setRecord(item);
		jukebox.save();
		if (item == null || item.getType() == Material.AIR) {
			sender.sendMessage(Lang._(NBTEditor.class, "commands.nbttile.jukebox.cleared"));
		} else {
			sender.sendMessage(Lang._(NBTEditor.class, "commands.nbttile.jukebox.set"));
		}
		return true;
	}
	
	@Command(args = "name", type = CommandType.PLAYER_ONLY, maxargs = Integer.MAX_VALUE, usage = "[name]")
	public boolean nameCommand(CommandSender sender, String[] args) throws BKgCommandException {
		Block block = UtilsMc.getTargetBlock((Player) sender, 5);
		if (TileNBTWrapper.allowsCustomName(block.getType())) {
			TileNBTWrapper tile = new TileNBTWrapper(block);
			tile.setCustomName(args.length == 0 ? null : UtilsMc.parseColors(StringUtils.join(args, " ")));
			tile.save();
			sender.sendMessage(args.length == 0 ? Lang._(NBTEditor.class, "commands.nbttile.name.cleared") : Lang._(NBTEditor.class, "commands.nbttile.name.set"));
		} else {
			sender.sendMessage(Lang._(NBTEditor.class, "commands.nbttile.name.no-sight"));
		}
		return true;
	}
	
	@Command(args = "command-colors", type = CommandType.PLAYER_ONLY)
	public boolean colorsCommand(CommandSender sender, String[] args) throws BKgCommandException {
		Block block = UtilsMc.getTargetBlock((Player) sender, 5);
		if (block.getType() != Material.COMMAND) {
			throw new BKgCommandException(Lang._(NBTEditor.class, "command-block.no-sight"));
		}
		CommandBlock commandBlock = (CommandBlock) block.getState();
		commandBlock.setCommand(UtilsMc.parseColors(commandBlock.getCommand()));
		commandBlock.update();
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtbook.colors"));
		return true;
	}
	
	@Command(args = "sign", type = CommandType.PLAYER_ONLY, minargs = 2, maxargs = Integer.MAX_VALUE, usage = "<line> [content ...]")
	public boolean signCommand(CommandSender sender, String[] args) throws BKgCommandException {
		Block block = UtilsMc.getTargetBlock((Player) sender, 5);
		if (block.getType() != Material.SIGN_POST) {
			throw new BKgCommandException(Lang._(NBTEditor.class, "commands.nbttile.sign.no-sight"));
		}
		int line = CommandUtils.parseInt(args[0], 4, 1);
		Sign sign = (Sign) block.getState();
		sign.setLine(line - 1, UtilsMc.parseColors(StringUtils.join(args, " ", 1, args.length)));
		sign.update();
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbttile.line-set"));
		return true;
	}
	
}
