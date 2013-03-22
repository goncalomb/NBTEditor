package com.goncalomb.bukkit.nbteditor.commands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.UtilsMc;
import com.goncalomb.bukkit.betterplugin.BetterCommand;
import com.goncalomb.bukkit.betterplugin.BetterCommandException;
import com.goncalomb.bukkit.betterplugin.BetterSubCommandType;
import com.goncalomb.bukkit.lang.Lang;
import com.goncalomb.bukkit.nbteditor.nbt.JukeboxNBTWrapper;

public class CommandNBTTile extends BetterCommand {
	
	public CommandNBTTile() {
		super("nbttile", "nbteditor.tile");
		setAlises("nbtt");
		setDescription(Lang._("nbt.cmds.nbtt.description"));
	}
	
	private static JukeboxNBTWrapper getJukebox(Player player) throws BetterCommandException {
		Block block = UtilsMc.getTargetBlock(player, 5);
		if (block.getType() != Material.JUKEBOX) {
			throw new BetterCommandException(Lang._("nbt.cmds.nbtt.jukebox.no-sight"));
		}
		return new JukeboxNBTWrapper(block);
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
	public boolean setRecordCommand(CommandSender sender, String[] args) throws BetterCommandException {
		JukeboxNBTWrapper jukebox = getJukebox((Player) sender);
		ItemStack item = ((Player) sender).getItemInHand();
		jukebox.setRecord(item);
		jukebox.save();
		if (item == null || item.getType() == Material.AIR) {
			sender.sendMessage(Lang._("nbt.cmds.nbtt.jukebox.cleared"));
		} else {
			sender.sendMessage(Lang._("nbt.cmds.nbtt.jukebox.set"));
		}
		return true;
	}
	
}
