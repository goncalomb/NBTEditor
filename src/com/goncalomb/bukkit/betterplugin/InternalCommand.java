package com.goncalomb.bukkit.betterplugin;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


final class InternalCommand extends Command {
	
	private BetterCommand _command;
	
	public InternalCommand(BetterCommand command, String name) {
		super(name);
		_command = command;
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (_command._plugin != null && _command._plugin.isEnabled()) {
			if (!testPermission(sender)) {
				return true;
			}
			_command.execute(sender, label, args);
		} else {
			sender.sendMessage("Nop!");
		}
		return true;
	}
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
		if (_command._plugin != null && _command._plugin.isEnabled() && testPermissionSilent(sender)) {
			return _command.tabComplete(sender, args);
		}
		return null;
	}
	
}
