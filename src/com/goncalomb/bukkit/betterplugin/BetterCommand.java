package com.goncalomb.bukkit.betterplugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public abstract class BetterCommand extends SubCommand {
	
	InternalCommand _internalCommand;
	Plugin _plugin;
	
	public BetterCommand(String name) {
		this(name, null);
	}
	
	public BetterCommand(String name, String permission) {
		_base = this;
		_internalCommand = new InternalCommand(this, name);
		_internalCommand.setPermission(permission);
		_internalCommand.setPermissionMessage(Lang._("common.commands.no-perm"));
		
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method method : methods) {
			Command config = method.getAnnotation(Command.class);
			if (config != null) {
				Class<?>[] params = method.getParameterTypes();
				if (params.length == 2 && method.getReturnType() == boolean.class && params[0] == CommandSender.class && params[1] == String[].class) {
					addSubCommand(config, method);
				}
			}
		}
	}
	
	public void setDescription(String description) {
		_internalCommand.setDescription(description);
	}	
	
	public void setAlises(String... aliases) {
		_internalCommand.setAliases(Arrays.asList(aliases));
	}
	
	public void setAlises(List<String> aliases) {
		_internalCommand.setAliases(aliases);
	}
	
	public Plugin getPlugin() {
		return _plugin;
	}
	
	boolean invokeMethod(Method method, CommandSender sender, String[] args) {
		try {
			return (Boolean) method.invoke(this, sender, args);
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof BetterCommandException) {
				sender.sendMessage(e.getCause().getMessage());
				return true;
			} else {
				throw new RuntimeException(e.getCause());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
