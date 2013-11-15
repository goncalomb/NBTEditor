package com.goncalomb.bukkit.nbteditor.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandException;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandListener;
import com.goncalomb.bukkit.bkglib.namemaps.PotionEffectsMap;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public class CommandNBTPotion implements BKgCommandListener {
	
	@Command(args = "nbtpotion", type = CommandType.PLAYER_ONLY, maxargs = 3, usage = "<effect> [level] [duration]")
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
