package com.goncalomb.bukkit.nbteditor.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.goncalomb.bukkit.PotionEffectsMap;
import com.goncalomb.bukkit.UtilsMc;
import com.goncalomb.bukkit.betterplugin.BetterCommand;
import com.goncalomb.bukkit.betterplugin.BetterCommandException;
import com.goncalomb.bukkit.betterplugin.BetterCommandType;
import com.goncalomb.bukkit.betterplugin.Lang;
import com.goncalomb.bukkit.nbteditor.nbt.BeaconNBTWrapper;
import com.goncalomb.bukkit.nbteditor.nbt.JukeboxNBTWrapper;
import com.goncalomb.bukkit.nbteditor.nbt.TileNBTWrapper;

public class CommandNBTTile extends BetterCommand {
	
	public CommandNBTTile() {
		super("nbttile", "nbteditor.tile");
		setAlises("nbtt");
		setDescription(Lang._("nbt.cmds.nbtt.description"));
	}
	
	private static BeaconNBTWrapper getBeacon(Player player) throws BetterCommandException {
		Block block = UtilsMc.getTargetBlock(player, 5);
		if (block.getType() != Material.BEACON) {
			throw new BetterCommandException(Lang._("nbt.cmds.nbtt.beacon.no-sight"));
		}
		return new BeaconNBTWrapper(block);
	}
	
	private static JukeboxNBTWrapper getJukebox(Player player) throws BetterCommandException {
		Block block = UtilsMc.getTargetBlock(player, 5);
		if (block.getType() != Material.JUKEBOX) {
			throw new BetterCommandException(Lang._("nbt.cmds.nbtt.jukebox.no-sight"));
		}
		return new JukeboxNBTWrapper(block);
	}
	
	@Command(args = "beacon", type = BetterCommandType.PLAYER_ONLY, minargs = 0, maxargs = 2, usage = "<primary|secondary> <effect>")
	public boolean beaconEffectCommand(CommandSender sender, String[] args) throws BetterCommandException {
		if (args.length == 2 && (args[0].equalsIgnoreCase("primary") || args[0].equalsIgnoreCase("secondary"))) {
			BeaconNBTWrapper beacon = getBeacon((Player) sender);
			PotionEffectType effect = null;
			boolean clear = args[1].equalsIgnoreCase("clear");
			if (!clear) {
				effect = PotionEffectsMap.getByName(args[1]);
				if (effect == null) {
					sender.sendMessage(Lang._("nbt.cmds.nbtt.beacon.invalid-effect"));
				}
			}
			if (clear || effect != null) {
				if (args[0].equalsIgnoreCase("primary")) {
					beacon.setPrimary(effect);
				} else {
					beacon.setSecondary(effect);
				}
				beacon.save();
				sender.sendMessage(Lang._format((clear ? "nbt.cmds.nbtt.beacon.cleared" : "nbt.cmds.nbtt.beacon.set"), args[0].toLowerCase()));
				return true;
			}
		}
		sender.sendMessage(Lang._("nbt.effects-prefix") + PotionEffectsMap.getStringList());
		sender.sendMessage(Lang._("nbt.cmds.nbtt.beacon.info"));
		return false;
	}
	
	@Command(args = "record", type = BetterCommandType.PLAYER_ONLY)
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
	
	@Command(args = "name", type = BetterCommandType.PLAYER_ONLY, maxargs = Integer.MAX_VALUE, usage = "[name]")
	public boolean nameCommand(CommandSender sender, String[] args) throws BetterCommandException {
		Block block = UtilsMc.getTargetBlock((Player) sender, 5);
		if (TileNBTWrapper.allowsCustomName(block.getType())) {
			TileNBTWrapper tile = new TileNBTWrapper(block);
			tile.setCustomName(args.length == 0 ? null : UtilsMc.parseColors(StringUtils.join(args, " ")));
			tile.save();
			sender.sendMessage(args.length == 0 ? Lang._("nbt.cmds.nbtt.name.cleared") : Lang._("nbt.cmds.nbtt.name.set"));
		} else {
			sender.sendMessage(Lang._("nbt.cmds.nbtt.name.no-sight"));
		}
		return true;
	}
	
}
