package com.goncalomb.bukkit.nbteditor.commands;

import java.awt.Color;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandException;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandListener;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public class CommandNBTArmor implements BKgCommandListener {
	
	@Command(args = "nbtarmor", type = CommandType.PLAYER_ONLY, maxargs = 1, usage = "<color>")
	public boolean potionCommand(CommandSender sender, String[] args) throws BKgCommandException {
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
					sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtarmor.ok"));
					return true;
				} catch (NumberFormatException e) {
				}
			}
			sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtarmor.nop"));
		}
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtarmor.info"));
		return false;
	}
}
