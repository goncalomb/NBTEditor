package com.goncalomb.bukkit.betterplugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.goncalomb.bukkit.betterplugin.BetterCommand.SubCommand;

class BetterSubCommand {	
	
	BetterCommand _main;
	private Method _method = null;
	private BetterSubCommandType _type = BetterSubCommandType.DEFAULT;
	private String _usage = "";
	private int _minArgs = 0;
	private int _maxArgs = 0;
	private LinkedHashMap<String, BetterSubCommand> _subCommands = new LinkedHashMap<String, BetterSubCommand>();
	
	protected BetterSubCommand() { }
	
	private BetterSubCommand(BetterCommand main) {
		_main = main;
	}
	
	void addSubCommand(String[] args, int argsIndex, SubCommand config, Method method) {
		if (argsIndex == args.length) {
			_method = method;
			_type = config.type();
			_usage = config.usage();
			_minArgs = (config.minargs() >= 0 ? config.minargs() : 0);
			_maxArgs = (config.maxargs() >= 0 ? config.maxargs() : 0);
			if (_minArgs > _maxArgs) {
				_maxArgs = _minArgs;
			}
			return;
		} else {
			BetterSubCommand subCommand = _subCommands.get(args[argsIndex]);
			if (subCommand == null) {
				subCommand = new BetterSubCommand(_main);
				_subCommands.put(args[argsIndex], subCommand);
			}
			subCommand.addSubCommand(args, argsIndex + 1, config, method);
		}
	}
	
	private void invokeThis(CommandSender sender, String label, String[] args, int argsIndex) {
		if (_method != null) {
			if (_type.isValidSender(sender)) {
				int argsLeft = args.length - argsIndex;
				if (argsLeft >= _minArgs && argsLeft <= _maxArgs) {
					String[] argsCopy = Arrays.copyOfRange(args, argsIndex, args.length);
					if (_main.invokeMethod(_method, sender, argsCopy)) {
						return;
					}
				}
			} else {
				sender.sendMessage(_type.getInvalidSenderMessage());
				return;
			}
		}
		// Send usage and sub-commands.
		String argsJoined = "/" + label + " " + (argsIndex > 0 ? StringUtils.join(args, ' ', 0, argsIndex) + " " : "");
		if (_method != null) {
			sender.sendMessage(argsJoined + _usage);
		}
		printAllSubCommands(sender, this, argsJoined);
	}
	
	private static void printAllSubCommands(CommandSender sender, BetterSubCommand command, String prefix) {
		for (Entry<String, BetterSubCommand> subCommandEntry : command._subCommands.entrySet()) {
			BetterSubCommand subCommand = subCommandEntry.getValue();
			if (subCommand._type.isValidSender(sender)) {
				String newPrefix = prefix + subCommandEntry.getKey() + " ";
				if (subCommand._method != null) {
					sender.sendMessage(newPrefix + subCommand._usage);
				}
				printAllSubCommands(sender, subCommand, newPrefix);
			}
		}
	}
	
	void invokeSubCommand(CommandSender sender, String label, String[] args, int argsIndex) {
		if (argsIndex == args.length) {
			invokeThis(sender, label, args, argsIndex);
		} else {
			BetterSubCommand subCommand = _subCommands.get(args[argsIndex].toLowerCase());
			if (subCommand != null) {
				subCommand.invokeSubCommand(sender, label, args, argsIndex + 1);
			} else {
				invokeThis(sender, label, args, argsIndex);
			}
		}
	}
	
	BetterSubCommand getSubCommand(String[] args, int argsIndex) {
		if (argsIndex == args.length - 1) {
			return this;
		} else {
			BetterSubCommand commandEx = _subCommands.get(args[argsIndex]);
			if (commandEx != null) {
				return commandEx.getSubCommand(args, argsIndex + 1);
			}
		}
		return null;
	}
	
	List<String> getSubCommandNames(CommandSender sender, String startWith) {
		ArrayList<String> allowedCommands = new ArrayList<String>();
		for (Entry<String, BetterSubCommand> command : _subCommands.entrySet()) {
			String name = command.getKey();
			if (name.startsWith(startWith) && command.getValue()._type.isValidSender(sender)) {
				allowedCommands.add(name);
			}
		}
		return allowedCommands;
	}
	
}
