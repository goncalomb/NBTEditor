package com.goncalomb.bukkit.betterplugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import com.goncalomb.bukkit.lang.Lang;
import com.goncalomb.bukkit.reflect.BukkitReflect;

public abstract class BetterPlugin extends JavaPlugin {
	
	private List<BetterCommand> _commands = new ArrayList<BetterCommand>();
	private SimpleCommandMap _commandMap;
	
	public void registerCommand(BetterCommand command) {
		command._plugin = this;
		_commandMap.register(this.getName(), command._internalCommand);
		_commands.add(command);
	}
	
	@Override
	public void onEnable() {
		_commandMap = BukkitReflect.getCommandMap();
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
