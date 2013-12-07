/*
 * Copyright (C) 2013 - Gonçalo Baltazar <http://goncalomb.com>
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

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommand;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandException;
import com.goncalomb.bukkit.bkglib.namemaps.PotionEffectsMap;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public class CommandNBTPotion extends BKgCommand {
	
	public CommandNBTPotion() {
		super("nbtpotion", "nbtp");
	}
	
	@Command(args = "", type = CommandType.PLAYER_ONLY, maxargs = 3, usage = "<effect> [level] [duration]")
	public boolean potionCommand(CommandSender sender, String[] args) throws BKgCommandException {
		if (args.length > 0) {
			HandItemWrapper.Potion item = new HandItemWrapper.Potion((Player) sender);
			PotionEffectType effect = PotionEffectsMap.getByName(args[0]);
			if (effect == null) {
				sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtpotion.invalid-effect"));
			} else {
				int level = 1;
				if (args.length >= 2) {
					level = CommandUtils.parseInt(args[1], Short.MAX_VALUE, 0);
				}
				int duration = (effect == PotionEffectType.HARM || effect == PotionEffectType.HEAL ? 0 : 600);
				if (args.length == 3) {
					duration = CommandUtils.parseTimeDuration(args[2]);
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
					sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtpotion.removed"));
				} else {
					item.meta.addCustomEffect(new PotionEffect(effect, duration, level - 1), true);
					sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtpotion.added"));
				}
				item.save();
				return true;
			}
		}
		sender.sendMessage(Lang._(NBTEditor.class, "effects-prefix") + PotionEffectsMap.getNamesAsString());
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtpotion.info"));
		return false;
	}
}
