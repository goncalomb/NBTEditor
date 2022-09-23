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

package com.goncalomb.bukkit.mylib.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

final class InternalCommand extends Command {

	private MyCommand _command;

	public InternalCommand(MyCommand command, String name) {
		super(name);
		_command = command;
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (getOwner().isEnabled()) {
			if (!testPermission(sender)) {
				return true;
			}
			_command.execute(sender, label, args, 0);
		} else {
			sender.sendMessage("Nop!");
		}
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
		if (getOwner().isEnabled()) {
			return _command.tabComplete(sender, args, 0);
		}
		return null;
	}

	Plugin getOwner() {
		return _command.getOwner();
	}

	MyCommand getCommand() {
		return _command;
	}

}
