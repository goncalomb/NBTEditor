package com.goncalomb.bukkit.nbteditor.commands;

import org.bukkit.command.CommandSender;

import com.goncalomb.bukkit.betterplugin.BetterCommand;
import com.goncalomb.bukkit.betterplugin.BetterSubCommandType;
import com.goncalomb.bukkit.lang.Lang;

public class CommandNBTTile extends BetterCommand {
	
	public CommandNBTTile() {
		super("nbttile", "nbteditor.tile");
		setAlises("nbtt");
		setDescription(Lang._("nbt.cmds.nbtt.description"));
	}
	
	@SubCommand(args = "beacon effect", type = BetterSubCommandType.PLAYER_ONLY, minargs = 2, usage = "<primary|secundary> <effect>")
	public boolean beaconEffectCommand(CommandSender sender, String[] args) {
		return true;
	}
	
	@SubCommand(args = "beacon levels", type = BetterSubCommandType.PLAYER_ONLY, minargs = 1, usage = "<levels>")
	public boolean beaconLevelsCommand(CommandSender sender, String[] args) {
		return true;
	}
	
	@SubCommand(args = "setrecord", type = BetterSubCommandType.PLAYER_ONLY)
	public boolean setRecordCommand(CommandSender sender, String[] args) {
		return true;
	}
	
}
