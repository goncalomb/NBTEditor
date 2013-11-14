package com.goncalomb.bukkit.bkglib.bkgcommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandListener.Command;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandListener.CommandType;

class BKgSubCommand {
	
	private Permission _perm;
	private BKgCommandListener _listener;
	private Method _exeMethod = null;
	private Method _tabMethod = null;
	private CommandType _type = CommandType.DEFAULT;
	private String _usage;
	private int _minArgs;
	private int _maxArgs;
	private LinkedHashMap<String, BKgSubCommand> _subCommands = new LinkedHashMap<String, BKgSubCommand>();
	
	protected BKgSubCommand(String name, Permission parent) {
		String permName = parent.getName();
		if (permName.endsWith(".*")) {
			permName = permName.substring(0, permName.length() - 2);
		}
		_perm = new Permission(permName + "." + name);
		_perm.addParent(parent, true);
		Bukkit.getPluginManager().addPermission(_perm);
		parent.recalculatePermissibles();
	}
	
	protected boolean addSubCommand(String[] args, int argsIndex, Command config, BKgCommandListener listener, Method exeMethod, Method tabMethod) {
		if (args.length == argsIndex) {
			if (_exeMethod == null) {
				_listener = listener;
				_exeMethod = exeMethod;
				_tabMethod = tabMethod;
				_type = config.type();
				_usage = config.usage();
				_minArgs = (config.minargs() >= 0 ? config.minargs() : 0);
				_maxArgs = (config.maxargs() >= 0 ? config.maxargs() : 0);
				if (_minArgs > _maxArgs) {
					_maxArgs = _minArgs;
				}
				return true;
			}
			return false;
		} else {
			BKgSubCommand subCommand = _subCommands.get(args[argsIndex]);
			if (subCommand == null) {
				subCommand = new BKgSubCommand(args[argsIndex], _perm);
				_subCommands.put(args[argsIndex], subCommand);
			}
			return subCommand.addSubCommand(args, argsIndex + 1, config, listener, exeMethod, tabMethod);
		}
	}
	
	void execute(CommandSender sender, String label, String[] args, int argsIndex) {
		// Find sub-command.
		if (argsIndex < args.length) {
			BKgSubCommand subCommand = _subCommands.get(args[argsIndex].toLowerCase());
			if (subCommand != null) {
				subCommand.execute(sender, label, args, argsIndex + 1);
				return;
			}
		}
		// Sub-command not found or no more arguments, let's try to run this one.
		if (_exeMethod != null) {
			if (_type.isValidSender(sender)) {
				if (sender.hasPermission(_perm)) {
					int argsLeft = args.length - argsIndex;
					if (argsLeft >= _minArgs && argsLeft <= _maxArgs) {
						if (invokeExeMethod(sender, Arrays.copyOfRange(args, argsIndex,args.length))) {
							return;
						}
					}
				} else {
					sender.sendMessage(Lang._(null, "commands.no-perm"));
					return;
				}
			} else {
				sender.sendMessage(_type.getInvalidSenderMessage());
				return;
			}
		}
		// Missing arguments or failed command, let's send usage and sub-commands!
		String prefix = "/" + label + " " + (argsIndex > 0 ? StringUtils.join(args, ' ', 0, argsIndex).toLowerCase() + " " : "");
		if (_exeMethod != null && sender.hasPermission(_perm)) {
			sender.sendMessage(ChatColor.RESET + prefix + _usage);
		}
		sendAllSubCommands(sender, this, prefix);
	}
	
	private static void sendAllSubCommands(CommandSender sender, BKgSubCommand command, String prefix) {
		for (Entry<String, BKgSubCommand> subCommandEntry : command._subCommands.entrySet()) {
			BKgSubCommand subCommand = subCommandEntry.getValue();
			if (subCommand._type.isValidSender(sender) && sender.hasPermission(subCommand._perm)) {
				String newPrefix = prefix + subCommandEntry.getKey() + " ";
				if (subCommand._exeMethod != null) {
					sender.sendMessage(ChatColor.GRAY + newPrefix + subCommand._usage);
				}
				sendAllSubCommands(sender, subCommand, newPrefix);
			}
		}
	}
	
	
	List<String> tabComplete(CommandSender sender, String[] args, int argsIndex) {
		// Find sub-command.
		if (argsIndex < args.length) {
			BKgSubCommand subCommand = _subCommands.get(args[argsIndex].toLowerCase());
			if (subCommand != null) {
				return subCommand.tabComplete(sender, args, argsIndex + 1);
			}
		}
		// Sub-command not found or no more arguments, let's try to run this one.
		int argsLeft = args.length - argsIndex;
		if (_tabMethod != null) {
			if (argsLeft <= _maxArgs && _type.isValidSender(sender) && sender.hasPermission(_perm)) {
				return invokeTabMethod(sender, Arrays.copyOfRange(args, argsIndex,args.length));
			}
			return null;
		}
		// Tab completion not found, send all sub-commands.
		if (argsLeft == 1) {
			ArrayList<String> allowedCommands = new ArrayList<String>();
			String arg = args[args.length - 1].toLowerCase();
			for (Entry<String, BKgSubCommand> command : _subCommands.entrySet()) {
				String name = command.getKey();
				if (name.startsWith(arg) && command.getValue()._type.isValidSender(sender)) {
					allowedCommands.add(name);
				}
			}
			return allowedCommands;
		}
		return null;
	}
	
	private boolean invokeExeMethod(CommandSender sender, String[] args) {
		try {
			return (Boolean) _exeMethod.invoke(_listener, sender, args);
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof BKgCommandException) {
				sender.sendMessage(e.getCause().getMessage());
				return true;
			} else {
				throw new RuntimeException(e.getCause());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<String> invokeTabMethod(CommandSender sender, String[] args) {
		try {
			return (List<String>) _tabMethod.invoke(_listener, sender, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void removePermission() {
		for (BKgSubCommand command : _subCommands.values()) {
			command.removePermission();
		}
		Bukkit.getPluginManager().removePermission(_perm);
	}
	
}
