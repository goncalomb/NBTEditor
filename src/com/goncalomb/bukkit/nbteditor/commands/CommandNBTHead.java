package com.goncalomb.bukkit.nbteditor.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.goncalomb.bukkit.betterplugin.BetterCommand;
import com.goncalomb.bukkit.betterplugin.BetterSubCommandType;
import com.goncalomb.bukkit.lang.Lang;

public class CommandNBTHead extends BetterCommand {
	
	public CommandNBTHead() {
		super("nbthead", "nbteditor.head");
		setAlises("nbth");
		setDescription(Lang._("nbt.cmds.nbth.description"));
	}
	
	@SubCommand(args = "", type = BetterSubCommandType.PLAYER_ONLY, minargs = 1, usage = "<player-name>")
	public boolean headCommand(CommandSender sender, String[] args) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		if (meta.setOwner(args[0])) {
			head.setItemMeta(meta);
			if (((Player) sender).getInventory().addItem(head).size() != 0) {
				sender.sendMessage(Lang._("general.inventory-full"));
			} else {
				sender.sendMessage(Lang._("nbt.cmds.nbth.done"));
			}
		} else {
			sender.sendMessage(Lang._("nbt.cmds.nbth.invalid"));
		}
		return true;
	}
}
