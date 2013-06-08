package com.goncalomb.bukkit.betterplugin;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

public enum BetterSubCommandType {
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
		return Lang._("common.commands.invalid-sender." + this.toString());
	}
}
