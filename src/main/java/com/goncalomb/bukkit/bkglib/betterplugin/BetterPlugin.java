package com.goncalomb.bukkit.bkglib.betterplugin;

import java.io.File;
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
	private static File _gmbConfigFolder;
	
	private List<BetterCommand> _commands = new ArrayList<BetterCommand>();
	//private File _betterConfigFolder;
	
	public static File getGmbConfigFolder() {
		return _gmbConfigFolder;
	}
	
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
	public final void onLoad() {
		if (_gmbConfigFolder == null) {
			_gmbConfigFolder = new File(getDataFolder().getParentFile(), "gmbConfig");
		}
		//_betterConfigFolder = new File(_gmbConfigFolder, getName());
	}
	
	@Override
	public final void onEnable() {
		Lang.registerPlugin(this);
		onBetterEnable();
	}
	
	public abstract void onBetterEnable();
	
	@Override
	public final void onDisable() {
		Collection<Command> commands = _commandMap.getCommands();
		for (BetterCommand command : _commands) {
			while (commands.remove(command._internalCommand));
		}
		getLogger().info(getName() + " has been disabled.");
		Lang.unregisterPlugin(this);
	}
}
