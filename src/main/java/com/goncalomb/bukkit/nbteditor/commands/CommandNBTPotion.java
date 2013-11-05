package com.goncalomb.bukkit.nbteditor.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.goncalomb.bukkit.bkglib.PotionEffectsMap;
import com.goncalomb.bukkit.bkglib.Utils;
import com.goncalomb.bukkit.bkglib.UtilsMc;
import com.goncalomb.bukkit.bkglib.betterplugin.BetterCommand;
import com.goncalomb.bukkit.bkglib.betterplugin.BetterCommandException;
import com.goncalomb.bukkit.bkglib.betterplugin.BetterCommandType;
import com.goncalomb.bukkit.bkglib.betterplugin.Lang;

public class CommandNBTPotion extends BetterCommand {
	
	public CommandNBTPotion() {
		super("nbtpotion", "nbteditor.potion");
		setAlises("nbtp");
		setDescription(Lang._("nbt.cmds.nbtp.description"));
	}
	
	@Command(args = "", type = BetterCommandType.PLAYER_ONLY, maxargs = 3, usage = "<effect> [level] [duration]")
	public boolean potionCommand(CommandSender sender, String[] args) throws BetterCommandException {
		if (args.length > 0) {
			HandItemWrapper.Potion item = new HandItemWrapper.Potion((Player) sender);
			PotionEffectType effect = PotionEffectsMap.getByName(args[0]);
			if (effect == null) {
				sender.sendMessage(Lang._("nbt.cmds.nbtp.invalid-effect"));
			} else {
				int level = 1;
				if (args.length >= 2) {
					level = Utils.parseInt(args[1], Short.MAX_VALUE, 0, -1);
					if (level == -1) {
						sender.sendMessage(Lang._("nbt.invalid-level"));
						return true;
					}
				}
				int duration = (effect == PotionEffectType.HARM || effect == PotionEffectType.HEAL ? 0 : 600);
				if (args.length == 3) {
					duration = UtilsMc.parseTickDuration(args[2]);
					if (duration == -1) {
						sender.sendMessage(Lang._("common.invalid-duration"));
						return true;
					}
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
					sender.sendMessage(Lang._("nbt.cmds.nbtp.removed"));
				} else {
					item.meta.addCustomEffect(new PotionEffect(effect, duration, level - 1), true);
					sender.sendMessage(Lang._("nbt.cmds.nbtp.added"));
				}
				item.save();
				return true;
			}
		}
		sender.sendMessage(Lang._("nbt.effects-prefix") + PotionEffectsMap.getStringList());
		sender.sendMessage(Lang._("nbt.cmds.nbtp.info"));
		return false;
	}
}
