package com.goncalomb.bukkit.bkglib.bkgcommand;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;

import com.goncalomb.bukkit.bkglib.BKgLib;
import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.utils.Utils;

public abstract class BKgCommand extends BKgSubCommand {
	
	public static final class CommandUtils {
		
		public static List<String> playerTabComplete(CommandSender sender, String prefix) {
			Player senderPlayer = null;
			if (sender instanceof Player) {
				senderPlayer = (Player) sender;
			}
	        ArrayList<String> players = new ArrayList<String>();
	        for (Player player : Bukkit.getOnlinePlayers()) {
	            String name = player.getName();
	            if ((senderPlayer == null || senderPlayer.canSee(player)) && StringUtil.startsWithIgnoreCase(name, prefix)) {
	            	players.add(name);
	            }
	        }
	        Collections.sort(players, String.CASE_INSENSITIVE_ORDER);
			return players;
		}
		
		public static Player findPlayer(String name) throws BKgCommandException {
			Player player = Bukkit.getPlayer(name);
			if (player == null) {
				throw new BKgCommandException(Lang._(null, "player-not-found.name", name));
			}
			return player;
		}
		
		public static PlayerInventory checkFullInventory(Player player) throws BKgCommandException {
			PlayerInventory inv = player.getInventory();
			if (inv.firstEmpty() == -1) {
				throw new BKgCommandException(Lang._(null, "inventory-full"));
			}
			return inv;
		}
		
		public static void giveItem(Player player, ItemStack item) throws BKgCommandException {
			if (player.getInventory().addItem(item).size() > 0) {
				throw new BKgCommandException(Lang._(null, "inventory-full"));
			}
		}
		
		public static int parseInt(String str) throws BKgCommandException {
			int i = Utils.parseInt(str, -1);
			if (i == -1) {
				throw new BKgCommandException(Lang._(null, "invalid-int", str));
			}
			return i;
		}
		
		public static int parseInt(String str, int max, int min) throws BKgCommandException {
			int i = Utils.parseInt(str, max, min, -1);
			if (i == -1) {
				throw new BKgCommandException(Lang._(null, "invalid-int.bounds", str, min, max));
			}
			return i;
		}
		
		public static int parseTimeDuration(String str) throws BKgCommandException {
			int i = Utils.parseTimeDuration(str);
			if (i == -1) {
				throw new BKgCommandException(Lang._(null, "invalid-duration"));
			}
			return i;
		}
		
		private CommandUtils() { }
	}
	
	public enum CommandType {
		DEFAULT, // All
		PLAYER_ONLY, // Player
		NO_PLAYER, // Console, Remote, Block
		CONSOLE_ONLY, // Console, Remote
		BLOCK_ONLY; // Block
		
		public boolean isValidSender(CommandSender sender) {
			switch (this) {
			case PLAYER_ONLY:
				return (sender instanceof Player);
			case NO_PLAYER:
				return (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender || sender instanceof BlockCommandSender);
			case CONSOLE_ONLY:
				return (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender);
			case BLOCK_ONLY:
				return (sender instanceof BlockCommandSender);
			case DEFAULT:
				return true;
			}
			return false;
		}
		
		public String getInvalidSenderMessage() {
			return Lang._(null, "commands.invalid-sender." + this.toString());
		}
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Command {
		String args();
		CommandType type() default CommandType.DEFAULT;
		String usage() default "";
		int minargs() default 0;
		int maxargs() default 0;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface TabComplete {
		String args();
	}
	
	//private static HashMap<String, BKgCommand> _commands = new HashMap<String, BKgCommand>();
	private InternalCommand _internalCommand;
	private Plugin _owner;
	
	public BKgCommand(String name, String ...aliases) {
		_internalCommand = new InternalCommand(this, name);
		_internalCommand.setAliases(Arrays.asList(aliases));
	}
	
	void setup(SimpleCommandMap commandMap, Plugin owner) {
		// Set the owner and permissions.
		_owner = owner;
		_internalCommand.setDescription(Lang._(owner.getClass(), "commands." + getName() + ".description"));
		setupPermissions(getName(), BKgLib.getRootPermission(owner));
		// Register the command with Bukkit.
		commandMap.register(owner.getName(), _internalCommand);
	}
	
	public final String getName() {
		return _internalCommand.getName();
	}
	
	public final Plugin getOwner() {
		return _owner;
	}
	
}
