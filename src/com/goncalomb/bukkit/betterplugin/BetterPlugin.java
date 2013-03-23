package com.goncalomb.bukkit.betterplugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;


public abstract class BetterPlugin extends JavaPlugin {

	private static SimpleCommandMap _commandMap;
	private List<BetterCommand> _commands = new ArrayList<BetterCommand>();
	
	protected BetterPlugin() {
		if (_commandMap == null) {
			Server server = Bukkit.getServer();
			Class<?> craftServerClass = server.getClass();
			try {
				Method getCommandMap = craftServerClass.getMethod("getCommandMap");
				_commandMap = (SimpleCommandMap) getCommandMap.invoke(server);
			} catch (Exception e) {
				throw new RuntimeException("Error while initializing the BetterPlugin class. This Plugin is not compatible with this version of Bukkit.");
			}
		}
	}
	
	public void registerCommand(BetterCommand command) {
		command._plugin = this;
		_commandMap.register(this.getName(), command._internalCommand);
		_commands.add(command);
	}
	
	@Override
	public void onEnable() {
		Lang.registerPlugin(this);
	}
	
	@Override
	public void onDisable() {
		Collection<Command> commands = _commandMap.getCommands();
		for (BetterCommand command : _commands) {
			while (commands.remove(command._internalCommand));
		}
	}
}
