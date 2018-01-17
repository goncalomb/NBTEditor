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

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.goncalomb.bukkit.mylib.command.MyCommandException;
import com.goncalomb.bukkit.mylib.command.MyCommandManager;
import com.goncalomb.bukkit.mylib.reflect.BukkitReflect;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;
import com.goncalomb.bukkit.mylib.utils.UtilsMc;
import com.goncalomb.bukkit.nbteditor.nbt.TileNBTWrapper;

public class CommandNBTTile extends AbstractNBTCommand<TileNBTWrapper> {

	public CommandNBTTile() {
		super("nbttile", "nbtt");
	}

	@Override
	protected TileNBTWrapper getWrapper(Player player) throws MyCommandException {
		Block block = UtilsMc.getTargetBlock(player, 5);
		if (block.getType() == Material.AIR) {
			throw new MyCommandException("§cNo tile in sight!");
		}
		try {
			return new TileNBTWrapper(block);
		} catch (RuntimeException e) {
			throw new MyCommandException("§cCannot edit that tile!");
		}
	}

	@Command(args = "copy", type = CommandType.PLAYER_ONLY)
	public boolean copy_Command(CommandSender sender, String[] args) throws MyCommandException {
		Player player = (Player) sender;
		TileNBTWrapper tile = getWrapper(player);
		player.setMetadata("NBTEditor-TileCopyPaste", new FixedMetadataValue(getOwner(), tile));
		sender.sendMessage("§aTile copied.");
		return true;
	}

	@Command(args = "paste", type = CommandType.PLAYER_ONLY)
	public boolean paste_Command(CommandSender sender, String[] args) throws MyCommandException {
		Player player = (Player) sender;
		if (player.hasMetadata("NBTEditor-TileCopyPaste")) {
			TileNBTWrapper tile = getWrapper(player);
			if (tile.cloneFrom((TileNBTWrapper) player.getMetadata("NBTEditor-TileCopyPaste").get(0).value())) {
				sender.sendMessage("§aTile pasted.");
				tile.save();
			} else {
				sender.sendMessage("§cTile must be of the same type!");
			}
		} else {
			sender.sendMessage("§cYou must copy a tile first!");
		}
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
