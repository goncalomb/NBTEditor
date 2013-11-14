package com.goncalomb.bukkit.nbteditor.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandListener;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public class CommandNBTHead implements BKgCommandListener {
	
	@Command(args = "nbthead", type = CommandType.PLAYER_ONLY, minargs = 1, usage = "<player-name>")
	public boolean headCommand(CommandSender sender, String[] args) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		if (meta.setOwner(args[0])) {
			head.setItemMeta(meta);
			if (((Player) sender).getInventory().addItem(head).size() != 0) {
				sender.sendMessage(Lang._(null, "inventory-full"));
			} else {
				sender.sendMessage(Lang._(NBTEditor.class, "commands.nbthead.done"));
			}
		} else {
			sender.sendMessage(Lang._(NBTEditor.class, "commands.nbthead.invalid"));
		}
		return true;
	}
}
