/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
 *
 * This file is part of NBTEditor.
 *
 * NBTEditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NBTEditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NBTEditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.nbteditor.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.goncalomb.bukkit.mylib.command.MyCommand;
import com.goncalomb.bukkit.mylib.command.MyCommandException;
import com.goncalomb.bukkit.mylib.namemaps.PotionEffectsMap;
import com.goncalomb.bukkit.mylib.utils.Utils;

public class CommandNBTPotion extends MyCommand {
	
	public CommandNBTPotion() {
		super("nbtpotion", "nbtp");
	}
	
	@Command(args = "", type = CommandType.PLAYER_ONLY, maxargs = 3, usage = "<effect> [level] [duration]")
	public boolean potionCommand(CommandSender sender, String[] args) throws MyCommandException {
		if (args.length > 0) {
			HandItemWrapper.Potion item = new HandItemWrapper.Potion((Player) sender);
			PotionEffectType effect = PotionEffectsMap.getByName(args[0]);
			if (effect == null) {
				sender.sendMessage("§cInvalid potion effect!");
			} else {
				int level = 1;
				if (args.length >= 2) {
					level = CommandUtils.parseInt(args[1], Short.MAX_VALUE, 0);
				}
				int duration = (effect == PotionEffectType.HARM || effect == PotionEffectType.HEAL ? 0 : 600);
				if (args.length == 3) {
					duration = CommandUtils.parseTickDuration(args[2]);
				}
				if(level == 0) {
					// .removeCustomEffect(...); This is bugged, it should use .equals(...) to compare effects.
					// So we remove all effects and add them again.
					List<PotionEffect> effects = item.meta.getCustomEffects();
					item.meta.clearCustomEffects();
					for (PotionEffect eff : effects) {
						if (!eff.getType().equals(effect)) {
							item.meta.addCustomEffect(eff, true);
						}
					}
					sender.sendMessage("§aPotion effect removed.");
				} else {
					item.meta.addCustomEffect(new PotionEffect(effect, duration, level - 1), true);
					sender.sendMessage("§aPotion effect added.");
				}
				item.save();
				return true;
			}
		}
		sender.sendMessage("§7Effects:" + PotionEffectsMap.getNamesAsString());
		sender.sendMessage("§eUse level = 0 to remove potion effects.");
		return false;
	}
	
	@TabComplete(args = "")
	public List<String> tab(CommandSender sender, String[] args) {
		return (args.length == 1 ? Utils.getElementsWithPrefix(PotionEffectsMap.getNames(), args[0]) : null);
	}
}
