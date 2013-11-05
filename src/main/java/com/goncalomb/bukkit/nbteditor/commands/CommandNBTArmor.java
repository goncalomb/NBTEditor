package com.goncalomb.bukkit.nbteditor.commands;

import java.awt.Color;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.goncalomb.bukkit.bkglib.betterplugin.BetterCommand;
import com.goncalomb.bukkit.bkglib.betterplugin.BetterCommandException;
import com.goncalomb.bukkit.bkglib.betterplugin.BetterCommandType;
import com.goncalomb.bukkit.bkglib.betterplugin.Lang;

public class CommandNBTArmor extends BetterCommand {
	
	public CommandNBTArmor() {
		super("nbtarmor", "nbteditor.armor");
		setAlises("nbta");
		setDescription(Lang._("nbt.cmds.nbta.description"));
	}
	
	@Command(args = "", type = BetterCommandType.PLAYER_ONLY, maxargs = 1, usage = "<color>")
	public boolean potionCommand(CommandSender sender, String[] args) throws BetterCommandException {
		if (args.length > 0) {
			HandItemWrapper.LeatherArmor item = new HandItemWrapper.LeatherArmor((Player) sender);
			if (!args[0].startsWith("#")) {
				args[0] = "#" + args[0];
			}
			if (args[0].length() == 7) {
				try {
					Color color = Color.decode(args[0]);
					item.meta.setColor(org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()));
					item.save();
					sender.sendMessage(Lang._("nbt.cmds.nbta.ok"));
					return true;
				} catch (NumberFormatException e) {
				}
			}
			sender.sendMessage(Lang._("nbt.cmds.nbta.nop"));
		}
		sender.sendMessage(Lang._("nbt.cmds.nbta.info"));
		return false;
	}
}
