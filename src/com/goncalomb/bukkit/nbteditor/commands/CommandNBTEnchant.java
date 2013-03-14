package com.goncalomb.bukkit.nbteditor.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import com.goncalomb.bukkit.EnchantmentsMap;
import com.goncalomb.bukkit.Utils;
import com.goncalomb.bukkit.betterplugin.BetterCommand;
import com.goncalomb.bukkit.betterplugin.BetterCommandException;
import com.goncalomb.bukkit.betterplugin.BetterSubCommandType;
import com.goncalomb.bukkit.lang.Lang;

public class CommandNBTEnchant extends BetterCommand {
	
	public CommandNBTEnchant() {
		super("nbtenchant", "nbteditor.enchant");
		setAlises("nbte");
		setDescription(Lang._("nbt.cmds.nbte.description"));
	}
	
	@SubCommand(args = "", type = BetterSubCommandType.PLAYER_ONLY, maxargs = 2, usage = "<enchantment> [level]")
	public boolean enchantCommand(CommandSender sender, String[] args) throws BetterCommandException {
		if (args.length > 0) {
			HandItemWrapper.Item item = new HandItemWrapper.Item((Player) sender);
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
		sender.sendMessage(Lang._("nbt.enchants-sufix") + EnchantmentsMap.getStringList());
		sender.sendMessage(Lang._("nbt.cmds.nbte.info"));
		return false;
	}
	
}