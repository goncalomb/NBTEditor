package com.goncalomb.bukkit.betterplugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.goncalomb.bukkit.Utils;
import com.goncalomb.bukkit.Utils.SplitType;

public abstract class BetterCommand extends BetterSubCommand {
	
	@Retention(RetentionPolicy.RUNTIME)
	protected @interface SubCommand {
		String args();
		BetterSubCommandType type() default BetterSubCommandType.DEFAULT;
		String usage() default "";
		int minargs() default 0;
		int maxargs() default 0;
	}
	
	InternalCommand _internalCommand;
	private String _permission;
	Plugin _plugin;
	
	private static boolean isSubCommandMethod(Method method) {
		Class<?>[] params = method.getParameterTypes();
		return params.length == 2 && method.getReturnType() == boolean.class && params[0] == CommandSender.class && params[1] == String[].class;
	}
	
	public BetterCommand(String name) {
		this(name, null);
	}
	
	public BetterCommand(String name, String permission) {
		_internalCommand = new InternalCommand(this, name);
		_permission = permission;
		
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method method : methods) {
			SubCommand subCommand = method.getAnnotation(SubCommand.class);
			if (subCommand != null && isSubCommandMethod(method)) {
				addSubCommand(Utils.split(subCommand.args(), SplitType.WHITE_SPACES), 0, subCommand, method);
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
	
	public boolean hasPermission(CommandSender sender) {
		return (_permission == null || sender.hasPermission(_permission));
	}
	
}
