package com.goncalomb.bukkit.bkglib.bkgcommand;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

import com.goncalomb.bukkit.bkglib.Lang;

public interface BKgCommandListener {
	
	public enum CmdType {
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
	public @interface Cmd {
		String args();
		CmdType type() default CmdType.DEFAULT;
		String usage() default "";
		int minargs() default 0;
		int maxargs() default 0;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface CmdTab {
		String args();
	}
	
}
