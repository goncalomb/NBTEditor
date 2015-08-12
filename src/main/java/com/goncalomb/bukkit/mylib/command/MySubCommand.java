/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
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

import com.goncalomb.bukkit.mylib.command.MyCommand.Command;
import com.goncalomb.bukkit.mylib.command.MyCommand.CommandType;

class MySubCommand {
	
	private Permission _perm;
	private MyCommand _command;
	private Method _exeMethod = null;
	private Method _tabMethod = null;
	private CommandType _type = CommandType.DEFAULT;
	private String _usage;
	private int _minArgs;
	private int _maxArgs;
	private LinkedHashMap<String, MySubCommand> _subCommands = new LinkedHashMap<String, MySubCommand>();
	
	public MySubCommand() {
		if (this instanceof MyCommand) {
			_command = (MyCommand) this;
		}
	}
	
	private MySubCommand(MyCommand command) {
		_command = command;
	}
	
	void setupPermissions(String name, Permission parent) {
		String permName = parent.getName();
		if (permName.endsWith(".*")) {
			permName = permName.substring(0, permName.length() - 2);
		}
		_perm = new Permission(permName + "." + name);
		_perm.addParent(parent, true);
		for (Entry<String, MySubCommand> entry : _subCommands.entrySet()) {
			entry.getValue().setupPermissions(entry.getKey(), _perm);
		}
		Bukkit.getPluginManager().addPermission(_perm);
		parent.recalculatePermissibles();
	}
	
	void removePermissions() {
		for (MySubCommand command : _subCommands.values()) {
			command.removePermissions();
		}
		Bukkit.getPluginManager().removePermission(_perm);
	}
	
	boolean addSubCommand(String[] args, int argsIndex, Command config, MyCommand command, Method exeMethod, Method tabMethod) {
		if (args.length == argsIndex) {
			if (_exeMethod == null) {
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
			MySubCommand subCommand = _subCommands.get(args[argsIndex]);
			if (subCommand == null) {
				subCommand = new MySubCommand(command);
				_subCommands.put(args[argsIndex], subCommand);
			}
			return subCommand.addSubCommand(args, argsIndex + 1, config, command, exeMethod, tabMethod);
		}
	}
	
	void execute(CommandSender sender, String label, String[] args, int argsIndex) {
		// Find sub-command.
		if (argsIndex < args.length) {
			MySubCommand subCommand = _subCommands.get(args[argsIndex].toLowerCase());
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
					sender.sendMessage("§cYou don't have permission to use that command!");
					return;
				}
			} else {
				sender.sendMessage(_type.INVALID_MESSAGE);
				return;
			}
		}
		// Missing arguments or failed command, let's send usage and sub-commands!
		String prefix = "/" + label + " " + (argsIndex > 0 ? StringUtils.join(args, ' ', 0, argsIndex).toLowerCase() + " " : "");
		boolean sentUsage = false;
		if (_exeMethod != null && sender.hasPermission(_perm)) {
			sender.sendMessage(ChatColor.RESET + prefix + _usage);
			sentUsage = true;
		}
		if (sendAllSubCommands(sender, this, prefix) == 0 && !sentUsage) {
			sender.sendMessage("§cYou don't have permission to use that command!");
		}
	}
	
	private static int sendAllSubCommands(CommandSender sender, MySubCommand command, String prefix) {
		int i = 0;
		for (Entry<String, MySubCommand> subCommandEntry : command._subCommands.entrySet()) {
			MySubCommand subCommand = subCommandEntry.getValue();
			if (subCommand._type.isValidSender(sender) && sender.hasPermission(subCommand._perm)) {
				String newPrefix = prefix + subCommandEntry.getKey() + " ";
				if (subCommand._exeMethod != null) {
					sender.sendMessage(ChatColor.GRAY + newPrefix + subCommand._usage);
					i++;
				}
				i += sendAllSubCommands(sender, subCommand, newPrefix);
			}
		}
		return i;
	}
	
	List<String> tabComplete(CommandSender sender, String[] args, int argsIndex) {
		// Find sub-command.
		if (argsIndex < args.length) {
			MySubCommand subCommand = _subCommands.get(args[argsIndex].toLowerCase());
			if (subCommand != null) {
				return subCommand.tabComplete(sender, args, argsIndex + 1);
			}
		}
		// Sub-command not found or no more arguments, let's try to run this one.
		int argsLeft = args.length - argsIndex;
		if (_tabMethod != null) {
			if (argsLeft >= _minArgs && argsLeft <= _maxArgs && _type.isValidSender(sender) && sender.hasPermission(_perm)) {
				return invokeTabMethod(sender, Arrays.copyOfRange(args, argsIndex,args.length));
			}
			return null;
		}
		// Tab completion not found, send all sub-commands.
		if (argsLeft == 1) {
			ArrayList<String> allowedCommands = new ArrayList<String>();
			String arg = args[args.length - 1].toLowerCase();
			for (Entry<String, MySubCommand> command : _subCommands.entrySet()) {
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
			return (Boolean) _exeMethod.invoke(_command, sender, args);
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof MyCommandException) {
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
			return (List<String>) _tabMethod.invoke(_command, sender, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
