package com.goncalomb.bukkit.betterplugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
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
	
	public BetterCommand(String name) {
		this(name, null);
	}
	
	public BetterCommand(String name, String permission) {
		_main = this;
		_internalCommand = new InternalCommand(this, name);
		_permission = permission;
		
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method method : methods) {
			SubCommand config = method.getAnnotation(SubCommand.class);
			if (config != null) {
				Class<?>[] params = method.getParameterTypes();
				if (params.length == 2 && method.getReturnType() == boolean.class && params[0] == CommandSender.class && params[1] == String[].class) {
					addSubCommand(Utils.split(config.args().toLowerCase(), SplitType.WHITE_SPACES), 0, config, method);
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
	
	public boolean hasPermission(CommandSender sender) {
		return (_permission == null || sender.hasPermission(_permission));
	}
	
	public Plugin getPlugin() {
		return _plugin;
	}
	
	boolean invokeMethod(Method method, CommandSender sender, String[] args) {
		try {
			if (!(Boolean) method.invoke(this, sender, args)) {
				return false;
			}
			return true;
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
