package com.goncalomb.bukkit.nbteditor.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import com.goncalomb.bukkit.bkglib.EnchantmentsMap;
import com.goncalomb.bukkit.bkglib.Utils;
import com.goncalomb.bukkit.bkglib.betterplugin.BetterCommand;
import com.goncalomb.bukkit.bkglib.betterplugin.BetterCommandException;
import com.goncalomb.bukkit.bkglib.betterplugin.BetterCommandType;
import com.goncalomb.bukkit.bkglib.betterplugin.Lang;
import com.goncalomb.bukkit.bkglib.reflect.NBTUtils;

public class CommandNBTEnchant extends BetterCommand {
	
	public CommandNBTEnchant() {
		super("nbtenchant", "nbteditor.enchant");
		setAlises("nbte");
		setDescription(Lang._("nbt.cmds.nbte.description"));
	}
	
	@Command(args = "", type = BetterCommandType.PLAYER_ONLY, maxargs = 2, usage = "<enchantment> [level]")
	public boolean enchantCommand(CommandSender sender, String[] args) throws BetterCommandException {
		if (args.length > 0) {
			HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
			if (args[0].equalsIgnoreCase("glow")) {
				NBTUtils.setItemStackFakeEnchantment(item.item);
				sender.sendMessage(Lang._("nbt.cmds.nbte.glow"));
				return true;
			}
			Enchantment enchant = EnchantmentsMap.getByName(args[0]);
			if (enchant == null) {
				sender.sendMessage(Lang._("nbt.cmds.nbte.invalid-enchant"));
			} else {
				int level = enchant.getMaxLevel();
				if (args.length == 2) {
					level = Utils.parseInt(args[1], Short.MAX_VALUE, 0, -1);
					if (level == -1) {
						sender.sendMessage(Lang._("nbt.invalid-level"));
						return true;
					}
				}
				if(level == 0) {
					if (item.item.getType() == Material.ENCHANTED_BOOK) {
						((EnchantmentStorageMeta) item.meta).removeEnchant(enchant);
					} else {
						item.meta.removeEnchant(enchant);
					}
					sender.sendMessage(Lang._("nbt.cmds.nbte.removed"));
				} else {
					if (item.item.getType() == Material.ENCHANTED_BOOK) {
						((EnchantmentStorageMeta) item.meta).addStoredEnchant(enchant, level, true);
					} else {
						item.meta.addEnchant(enchant, level, true);
					}
					sender.sendMessage(Lang._("nbt.cmds.nbte.added"));
				}
				item.save();
				return true;
			}
		}
		sender.sendMessage(Lang._("nbt.enchants-prefix") + EnchantmentsMap.getStringList());
		sender.sendMessage(Lang._("nbt.cmds.nbte.info"));
		return false;
	}
	
	@Command(args = "clear", type = BetterCommandType.PLAYER_ONLY)
	public boolean enchant_clearCommand(CommandSender sender, String[] args) throws BetterCommandException {
		HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
		for (Enchantment ench : item.meta.getEnchants().keySet()) {
			item.meta.removeEnchant(ench);
		}
		item.save();
		sender.sendMessage(Lang._("nbt.cmds.nbte.cleared"));
		return true;
	}
	
}