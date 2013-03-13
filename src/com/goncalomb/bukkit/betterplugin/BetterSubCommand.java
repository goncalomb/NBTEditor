package com.goncalomb.bukkit.betterplugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.goncalomb.bukkit.betterplugin.BetterCommand.SubCommand;
import com.goncalomb.bukkit.lang.Lang;

class BetterSubCommand {	
	
	Method _method;
	BetterSubCommandType _type;
	String _usage;
	int _minArgs;
	int _maxArgs;
	LinkedHashMap<String, BetterSubCommand> _subCommands;
	
	BetterSubCommand() {
		_method = null;
		_type = BetterSubCommandType.DEFAULT;
		_usage = "";
		_minArgs = 0;
		_maxArgs = 0;
		_subCommands = new LinkedHashMap<String, BetterSubCommand>();
	}
	
	void addSubCommand(String[] args, int argsIndex, SubCommand subCommand, Method method) {
		if (argsIndex == args.length) {
			_method = method;
			_type = subCommand.type();
			_usage = subCommand.usage();
			_minArgs = (subCommand.minargs() >= 0 ? subCommand.minargs() : 0);
			_maxArgs = (subCommand.maxargs() >= 0 ? subCommand.maxargs() : 0);
			if (_minArgs > _maxArgs) {
				_maxArgs = _minArgs;
			}
			return;
		} else {
			BetterSubCommand commandEx = _subCommands.get(args[argsIndex]);
			if (commandEx == null) {
				commandEx = new BetterSubCommand();
				_subCommands.put(args[argsIndex], commandEx);
			}
			commandEx.addSubCommand(args, argsIndex + 1, subCommand, method);
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
	
	String invokeThis(BetterCommand executor, CommandSender sender, String[] args, int argsIndex) {
		if (_method != null) {
			if (_type.isValidSender(sender)) {
				int remaningArgs = args.length - argsIndex;
				if (remaningArgs < _minArgs || remaningArgs > _maxArgs) {
					return _usage;
				}
				args = Arrays.copyOfRange(args, argsIndex, args.length);
				try {
					if (!(Boolean)_method.invoke(executor, sender, (Object[])args)) {
						return _usage;
					}
				} catch (InvocationTargetException e) {
					if (e.getCause() instanceof BetterCommandException) {
						sender.sendMessage(e.getCause().getMessage());
					} else {
						throw new RuntimeException(e.getCause());
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else {
				sender.sendMessage(Lang._("general.commands.invalid-sender"));
			}
			return null;
		}
		return "<" + StringUtils.join(getSubCommandNames(sender, ""), '/') + ">";
	}
	
	String invokeSubCommand(BetterCommand executor, CommandSender sender, String[] args, int argsIndex) {
		if (argsIndex == args.length) {
			return invokeThis(executor, sender, args, argsIndex);
		} else {
			BetterSubCommand commandEx = _subCommands.get(args[argsIndex]);
			if (commandEx != null) {
				String result = commandEx.invokeSubCommand(executor, sender, args, argsIndex + 1);
				if (result != null) {
					return args[argsIndex] + " " + result;
				}
			} else {
				return invokeThis(executor, sender, args, argsIndex);
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
