package com.goncalomb.bukkit.nbteditor.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.goncalomb.bukkit.Utils;
import com.goncalomb.bukkit.UtilsMc;
import com.goncalomb.bukkit.betterplugin.BetterCommand;
import com.goncalomb.bukkit.betterplugin.BetterCommandException;
import com.goncalomb.bukkit.betterplugin.BetterSubCommandType;
import com.goncalomb.bukkit.lang.Lang;

public class CommandNBTItem extends BetterCommand {
	
	public CommandNBTItem() {
		super("nbtitem", "nbteditor.item");
		setAlises("nbti");
		setDescription(Lang._("nbt.cmds.nbti.description"));
	}
	
	@SubCommand(args = "name", type = BetterSubCommandType.PLAYER_ONLY, maxargs = Integer.MAX_VALUE, usage = "<new-name>")
	public boolean nameCommand(CommandSender sender, String[] args) throws BetterCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		item.meta.setDisplayName(args.length == 0 ? null : UtilsMc.parseColors(StringUtils.join(args, " ")));
		item.save();
		sender.sendMessage(args.length == 0 ? Lang._("nbt.cmds.nbti.name-removed") : Lang._("nbt.cmds.nbti.renamed"));
		return true;
	}
	
	@SubCommand(args = "lore add", type = BetterSubCommandType.PLAYER_ONLY, minargs = 1, maxargs = Integer.MAX_VALUE, usage = "<new-lore>")
	public boolean lore_addCommand(CommandSender sender, String[] args) throws BetterCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		List<String> lores = item.meta.getLore();
		if (lores == null) {
			lores = new ArrayList<String>();
		}
		lores.add(UtilsMc.parseColors(StringUtils.join(args, " ")));
		item.meta.setLore(lores);
		item.save();
		sender.sendMessage(Lang._("nbt.cmds.nbti.lore-added"));
		return true;
	}
	
	@SubCommand(args = "lore del", type = BetterSubCommandType.PLAYER_ONLY, minargs = 1, usage = "<index>")
	public boolean lore_delCommand(CommandSender sender, String[] args) throws BetterCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		List<String> lores = item.meta.getLore();
		int index = Utils.parseInt(args[0], -1);
		if (index < 1) {
			sender.sendMessage(Lang._("nbt.invalid-index"));
		} else if (lores == null || index > lores.size()) {
			sender.sendMessage(Lang._format("nbt.cmds.nbti.lore-nop", index));
		} else {
			lores.remove(index - 1);
			item.meta.setLore(lores);
			item.save();
			sender.sendMessage(Lang._("nbt.cmds.nbti.lore-removed"));
		}
		return true;
	}
	
	@SubCommand(args = "lore delall", type = BetterSubCommandType.PLAYER_ONLY)
	public boolean lore_delallCommand(CommandSender sender, String[] args) throws BetterCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		item.meta.setLore(null);
		item.save();
		sender.sendMessage(Lang._("nbt.cmds.nbti.lore-cleared"));
		return true;
	}
}