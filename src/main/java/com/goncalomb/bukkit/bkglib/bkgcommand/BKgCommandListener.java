package com.goncalomb.bukkit.bkglib.bkgcommand;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.goncalomb.bukkit.bkglib.Lang;

public interface BKgCommandListener {
	
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
	
}
