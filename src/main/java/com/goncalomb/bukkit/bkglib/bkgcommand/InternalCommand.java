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
