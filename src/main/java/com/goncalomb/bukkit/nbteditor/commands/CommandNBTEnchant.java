package com.goncalomb.bukkit.nbteditor.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommand;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandException;
import com.goncalomb.bukkit.bkglib.namemaps.EnchantmentsMap;
import com.goncalomb.bukkit.bkglib.reflect.NBTUtils;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public class CommandNBTEnchant extends BKgCommand {
	
	public CommandNBTEnchant() {
		super("nbtencahnt", "nbte");
	}
	
	@Command(args = "", type = CommandType.PLAYER_ONLY, maxargs = 2, usage = "<enchantment> [level]")
	public boolean enchantCommand(CommandSender sender, String[] args) throws BKgCommandException {
		if (args.length > 0) {
			HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
			if (args[0].equalsIgnoreCase("glow")) {
				NBTUtils.setItemStackFakeEnchantment(item.item);
				sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtenchant.glow"));
				return true;
			}
			Enchantment enchant = EnchantmentsMap.getByName(args[0]);
			if (enchant == null) {
				sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtenchant.invalid-enchant"));
			} else {
				int level = enchant.getMaxLevel();
				if (args.length == 2) {
					level = CommandUtils.parseInt(args[1], Short.MAX_VALUE, 0);
				}
				if(level == 0) {
					if (item.item.getType() == Material.ENCHANTED_BOOK) {
						((EnchantmentStorageMeta) item.meta).removeStoredEnchant(enchant);
					} else {
						item.meta.removeEnchant(enchant);
					}
					sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtenchant.removed"));
				} else {
					if (item.item.getType() == Material.ENCHANTED_BOOK) {
						((EnchantmentStorageMeta) item.meta).addStoredEnchant(enchant, level, true);
					} else {
						item.meta.addEnchant(enchant, level, true);
					}
					sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtenchant.added"));
				}
				item.save();
				return true;
			}
		}
		sender.sendMessage(Lang._(NBTEditor.class, "enchants-prefix") + EnchantmentsMap.getNamesAsString());
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtenchant.info"));
		return false;
	}
	
	@Command(args = "clear", type = CommandType.PLAYER_ONLY)
	public boolean enchant_clearCommand(CommandSender sender, String[] args) throws BKgCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		for (Enchantment ench : item.meta.getEnchants().keySet()) {
			item.meta.removeEnchant(ench);
		}
		item.save();
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtenchant.cleared"));
		return true;
	}
	
}