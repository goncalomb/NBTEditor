/*
 * Copyright (C) 2013 - Gonçalo Baltazar <http://goncalomb.com>
 *
 * This file is part of BKgLib.
 *
 * BKgLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BKgLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BKgLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.bkglib.bkgcommand;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

final class InternalCommand extends Command {
	
	private BKgCommand _command;
	
	public InternalCommand(BKgCommand command, String name) {
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
	
	BKgCommand getCommand() {
		return _command;
	}
	
}
