package com.goncalomb.bukkit.bkglib.bkgcommand;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import com.goncalomb.bukkit.bkglib.BKgLib;
import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandListener.Cmd;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandListener.CmdTab;

public final class BKgCommand extends BKgSubCommand {
	
	private static HashMap<Plugin, Permission> _topPermissions = new HashMap<Plugin, Permission>();
	private static HashMap<String, BKgCommand> _commands = new HashMap<String, BKgCommand>();
	private InternalCommand _internalCommand;
	Plugin _owner;
	
	public static void register(SimpleCommandMap commandMap, BKgCommandListener listener, Plugin plugin) {
		HashMap<String, Method> tabMethods = new HashMap<String, Method>();
		Method[] methods = listener.getClass().getDeclaredMethods();
		// Find all tab completion methods.
		for (Method method : methods) {
			CmdTab config = method.getAnnotation(CmdTab.class);
			if (config != null) {
				// Verify parameter types.
				Class<?>[] params = method.getParameterTypes();
				if (params.length == 2 && method.getReturnType() == List.class && params[0] == CommandSender.class && params[1] == String[].class) {
					tabMethods.put(config.args().trim().toLowerCase(), method);
				} else {
					throw new RuntimeException("Invalid command tab completion method " + method.getName() + " on class " + listener.getClass().getName() + ".");
				}
			}
		}
		// Find all execution methods.
		for (Method method : methods) {
			Cmd config = method.getAnnotation(Cmd.class);
			if (config != null) {
				// Verify parameter types.
				Class<?>[] params = method.getParameterTypes();
				if (params.length == 2 && method.getReturnType() == boolean.class && params[0] == CommandSender.class && params[1] == String[].class) {
					String argsString = config.args().trim().toLowerCase();
					String[] args = argsString.split("\\s+");
					if (args.length > 0) {
						String cmdName = args[0];
						// Find command.
						BKgCommand command = _commands.get(cmdName);
						if (command == null) {
							// This is a new command, create and register.
							command = new BKgCommand(plugin, cmdName);
							commandMap.register(plugin.getName(), command._internalCommand);
							_commands.put(cmdName, command);
						} else {
							// Not a new command, check owner plugin.
							if (plugin != command._owner) {
								throw new RuntimeException("Command " + cmdName + " already registed by " + command._owner.getName() + ", cannot add new sub-commands.");
							}
						}
						// Register.
						if(!command.addSubCommand(args, 1, config, listener, method, tabMethods.remove(argsString))) {
							throw new RuntimeException("(Sub-)Command '" + argsString + "' already registed.");
						}
						continue;
					}
				}
				throw new RuntimeException("Invalid command execution method " + method.getName() + " on class " + listener.getClass().getName() + ".");
			}
		}
		if (tabMethods.size() > 0) {
			throw new RuntimeException("Tab completion method " + tabMethods.get(0).getName() + " on class " + listener.getClass().getName() + " has no execution method.");
		}
	}
	
	public static void setCommandAliases(SimpleCommandMap commandMap, String command, String... aliases) {
		BKgCommand cmd = _commands.get(command.trim().toLowerCase());
		if (cmd != null && cmd._internalCommand.getAliases().size() == 0) {
			// Re-register command (I'm not proud of this).
			cmd._internalCommand.unregister(commandMap);
			cmd._internalCommand.setAliases(Arrays.asList(aliases));
			commandMap.register("null", cmd._internalCommand);
		}
	}
	
	public static void unregisterAll(SimpleCommandMap commandMap, Plugin plugin) {
		Collection<Command> internalCommands = commandMap.getCommands();
		for (Iterator<BKgCommand> it = _commands.values().iterator(); it.hasNext(); ) {
			BKgCommand command = it.next();
			if (command._owner == plugin) {
				while(internalCommands.remove(command._internalCommand));
				command.removePermission();
				it.remove();
			}
		}
		_topPermissions.remove(plugin);
	}
	
	private BKgCommand(Plugin owner, String name) {
		super(name, BKgLib.getTopPermission(owner));
		_internalCommand = new InternalCommand(this, name);
		_internalCommand.setDescription(Lang._(owner.getClass(), "commands." + name + ".description"));
		_owner = owner;
	}
	
}
