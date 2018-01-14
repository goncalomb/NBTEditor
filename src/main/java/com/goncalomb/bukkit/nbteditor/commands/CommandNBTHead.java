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

import org.bukkit.command.CommandSender;

import com.goncalomb.bukkit.mylib.command.MyCommand;
import com.goncalomb.bukkit.mylib.command.MyCommandException;

@Deprecated
public class CommandNBTHead extends MyCommand {

	public CommandNBTHead() {
		super("nbthead", "nbth");
	}

	@Command(args = "", type = CommandType.PLAYER_ONLY, maxargs = Integer.MAX_VALUE)
	public boolean _Command(CommandSender sender, String[] args) throws MyCommandException {
		sender.sendMessage("§eCOMMAND REMOVED in NBTEditor 3.0.");
		sender.sendMessage("§7Use '§b/give <your-name> minecraft:skull 1 3§7'");
		sender.sendMessage("§7  to spawn a player head.");
		sender.sendMessage("§7Then '§b/nbtitem var Owner <player-name>§7'");
		sender.sendMessage("§7  to set the head owner/skin.");
		return true;
	}

}
