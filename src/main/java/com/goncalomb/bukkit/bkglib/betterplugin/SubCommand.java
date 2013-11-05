package com.goncalomb.bukkit.bkglib.betterplugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class SubCommand {	
	
	@Retention(RetentionPolicy.RUNTIME)
	protected @interface Command {
		String args();
		BetterCommandType type() default BetterCommandType.DEFAULT;
		String usage() default "";
		int minargs() default 0;
		int maxargs() default 0;
	}
	
	protected BetterCommand _base;
	private Method _method = null;
	private BetterCommandType _type = BetterCommandType.DEFAULT;
	private String _usage;
	private int _minArgs;
	private int _maxArgs;
	private LinkedHashMap<String, SubCommand> _subCommands = new LinkedHashMap<String, SubCommand>();
	
	protected SubCommand() { }
	
	private SubCommand(BetterCommand base) {
		_base = base;
	}
	
	protected void addSubCommand(Command config, Method method) {
		String[] argsArray = config.args().trim().split("\\s+");
		argsArray = (argsArray.length == 1 && argsArray[0].isEmpty() ? new String[] {} : argsArray);
		addSubCommand(argsArray, 0, config, method);
	}
	
	private void addSubCommand(String[] args, int argsIndex, Command config, Method method) {
		if (args.length == argsIndex) {
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
			SubCommand subCommand = _subCommands.get(args[argsIndex]);
			if (subCommand == null) {
				subCommand = new SubCommand(_base);
				_subCommands.put(args[argsIndex], subCommand);
			}
			subCommand.addSubCommand(args, argsIndex + 1, config, method);
		}
	}
	
	void execute(CommandSender sender, String label, String[] args) {
		execute(sender, label, args, 0);
	}
	
	void execute(CommandSender sender, String label, String[] args, int argsIndex) {
		// Find sub-command.
		if (argsIndex < args.length) {
			SubCommand subCommand = _subCommands.get(args[argsIndex]);
			if (subCommand != null) {
				subCommand.execute(sender, label, args, argsIndex + 1);
				return;
			}
		}
		
		// Sub-command not found or no more arguments, let's try to run this one.
		if (_method != null) {
			if (_type.isValidSender(sender)) {
				int argsLeft = args.length - argsIndex;
				if (argsLeft >= _minArgs && argsLeft <= _maxArgs) {
					if (_base.invokeMethod(_method, sender, Arrays.copyOfRange(args, argsIndex,args.length))) {
						return;
					}
				}
			} else {
				sender.sendMessage(_type.getInvalidSenderMessage());
				return;
			}
		}
		
		// Missing arguments or failed command, let's send usage and sub-commands!
		String prefix = "/" + label + " " + (argsIndex > 0 ? StringUtils.join(args, ' ', 0, argsIndex) + " " : "");
		if (_method != null) {
			sender.sendMessage(ChatColor.RESET + prefix + _usage);
		}
		sendAllSubCommands(sender, this, prefix);
	}
	
	private static void sendAllSubCommands(CommandSender sender, SubCommand command, String prefix) {
		for (Entry<String, SubCommand> subCommandEntry : command._subCommands.entrySet()) {
			SubCommand subCommand = subCommandEntry.getValue();
			if (subCommand._type.isValidSender(sender)) {
				String newPrefix = prefix + subCommandEntry.getKey() + " ";
				if (subCommand._method != null) {
					sender.sendMessage(ChatColor.GRAY + newPrefix + subCommand._usage);
				}
				sendAllSubCommands(sender, subCommand, newPrefix);
			}
		}
	}
	
	List<String> tabComplete(CommandSender sender, String[] args) {
		SubCommand subCommand = getSubCommand(args, 0);
		if (subCommand != null) {
			ArrayList<String> allowedCommands = new ArrayList<String>();
			for (Entry<String, SubCommand> command : subCommand._subCommands.entrySet()) {
				String name = command.getKey();
				if (name.startsWith(args[args.length - 1]) && command.getValue()._type.isValidSender(sender)) {
					allowedCommands.add(name);
				}
			}
			return allowedCommands;
		}
		return null;
	}
	
	SubCommand getSubCommand(String[] args, int argsIndex) {
		if (argsIndex == args.length - 1) {
			return this;
		} else {
			SubCommand subCommand = _subCommands.get(args[argsIndex]);
			if (subCommand != null) {
				return subCommand.getSubCommand(args, argsIndex + 1);
			}
		}
		return null;
	}
	
}
