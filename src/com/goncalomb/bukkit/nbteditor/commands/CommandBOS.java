package com.goncalomb.bukkit.nbteditor.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.goncalomb.bukkit.nbteditor.bos.BookOfSouls;
import com.goncalomb.bukkit.nbteditor.nbt.EntityNBT;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTVariable;
import com.goncalomb.bukkit.UtilsMc;
import com.goncalomb.bukkit.betterplugin.BetterCommand;
import com.goncalomb.bukkit.betterplugin.BetterCommandException;
import com.goncalomb.bukkit.betterplugin.BetterSubCommandType;
import com.goncalomb.bukkit.lang.Lang;

public class CommandBOS extends BetterCommand {
	
	public CommandBOS() {
		super("bookofsouls", "nbteditor.bookofsouls");
		setAlises("bos");
		setDescription(Lang._("nbt.cmds.bos.description"));
	}
	
	static BookOfSouls getBos(Player player) throws BetterCommandException {
		return getBos(player, false);
	}
	
	static BookOfSouls getBos(Player player, boolean nullIfMissing) throws BetterCommandException {
		ItemStack item = player.getItemInHand();
		if (BookOfSouls.isValidBook(item)) {
			BookOfSouls bos = new BookOfSouls(item);
			if (bos.isValid()) {
				return bos;
			}
			throw new BetterCommandException(Lang._("nbt.bos.corrupted"));
		} else if (!nullIfMissing) {
			throw new BetterCommandException(Lang._("nbt.cmds.bos.holding"));
		}
		return null;
	}
	
	@SubCommand(args = "get", type = BetterSubCommandType.PLAYER_ONLY, maxargs = 1, usage = "<entity>")
	public boolean getCommand(CommandSender sender, String[] args) {
		if (args.length == 1) {
			EntityType entityType = EntityType.fromName(args[0]);
			if (entityType != null && EntityNBT.isValidType(entityType)) {
				PlayerInventory inv = ((Player) sender).getInventory();
				if (inv.firstEmpty() == -1) {
					sender.sendMessage(Lang._("general.inventory-full"));
					return true;
				}
				BookOfSouls bos = new BookOfSouls(entityType);
				inv.addItem(bos.getBook());
				sender.sendMessage(Lang._("nbt.cmds.bos.give"));
				if (entityType == EntityType.ENDERMAN) {
					sender.sendMessage(ChatColor.YELLOW + "(Enderman's carring block id is limited to 127 due to a minecraft bug)");
				}
				return true;
			}
			sender.sendMessage(Lang._("nbt.invalid-entity"));
		}
		sender.sendMessage(Lang._("nbt.entities-sufix") + UtilsMc.entityTypeArrayToString(EntityNBT.getValidEntityTypes()));
		return false;
	}
	
	@SubCommand(args = "var", type = BetterSubCommandType.PLAYER_ONLY, minargs = 1, maxargs = 2, usage = "<variable> [value]")
	public boolean varCommand(CommandSender sender, String[] args) throws BetterCommandException {
		BookOfSouls bos = getBos((Player) sender);
		NBTVariable variable = bos.getEntityNBT().getVariable(args[0]);
		if (variable != null) {
			if(args.length == 2) {
				if (variable.setValue(args[1])) {
					bos.saveBook();
					sender.sendMessage(Lang._("nbt.variable.updated"));
					return true;
				} else {
					sender.sendMessage(Lang._format("nbt.variable.invalid-format", args[0]));
				}
			}
			sender.sendMessage(ChatColor.YELLOW + variable.getFormat());
		} else {
			sender.sendMessage(Lang._format("nbt.cmds.bos.no-variable", args[0]));
		}
		return true;
	}
	
	@SubCommand(args = "clearvar", type = BetterSubCommandType.PLAYER_ONLY, minargs = 1, usage = "<variable>")
	public boolean clearvarCommand(CommandSender sender, String[] args) throws BetterCommandException {
		BookOfSouls bos = getBos((Player) sender);
		NBTVariable variable = bos.getEntityNBT().getVariable(args[0]);
		if (variable != null) {
			variable.clear();
			bos.saveBook();
			sender.sendMessage(Lang._("nbt.variable.cleared"));
		} else {
			sender.sendMessage(Lang._format("nbt.variable.invalid-format", args[0]));
		}
		return true;
	}
	
	@SubCommand(args = "items", type = BetterSubCommandType.PLAYER_ONLY)
	public boolean itemsCommand(CommandSender sender, String[] args) throws BetterCommandException {
		Player player = (Player) sender;
		BookOfSouls bos = getBos(player);
		if (!bos.openMobInventory(player)) {
			if (!bos.openDroppedItemInventory(player)) {
				player.sendMessage(Lang._("nbt.cmds.bos.no-inventory"));
			}
		}
		return true;
	}
	
	@SubCommand(args = "offers", type = BetterSubCommandType.PLAYER_ONLY)
	public boolean offersCommand(CommandSender sender, String[] args) throws BetterCommandException {
		Player player = (Player) sender;
		if (!getBos(player).openOffersInventory(player)) {
			player.sendMessage(Lang._("nbt.cmds.bos.no-villager"));
		} else {
			player.sendMessage(Lang._("nbt.cmds.bos.villager-info"));
		}
		return true;
	}
	
	@SubCommand(args = "dropchance", type = BetterSubCommandType.PLAYER_ONLY, maxargs = 5, usage = "[<head> <chest> <legs> <feet> <hand>]")
	public boolean dropchanceCommand(CommandSender sender, String[] args) throws BetterCommandException {
		Player player = (Player) sender;
		if (args.length == 0) {
			BookOfSouls bos = getBos(player);
			if (!bos.clearMobDropChance()) {
				player.sendMessage(Lang._("nbt.cmds.bos.no-mob"));
			} else {
				bos.saveBook();
				player.sendMessage(Lang._("nbt.cmds.bos.drop-chance.cleared"));
			}
			return true;
		} else if (args.length == 5) {

			float head = 0f, chest = 0f, legs = 0f, feet = 0f, hand = 0f;
			boolean invalid = false;
			try {
				head = Float.parseFloat(args[0]);
				chest = Float.parseFloat(args[1]);
				legs = Float.parseFloat(args[2]);
				feet = Float.parseFloat(args[3]);
				hand = Float.parseFloat(args[4]);
			} catch (NumberFormatException e) {
				invalid = true;
			}
			if (invalid || head < 0 || head > 1 || chest < 0 || chest > 1|| legs < 0 || legs > 1 || feet < 0 || feet > 1 || hand < 0 || hand > 1) {
				player.sendMessage(Lang._("nbt.cmds.bos.drop-chance.invalid"));
				return true;
			}
			
			BookOfSouls bos = getBos(player);
			if (!bos.setMobDropChance(head, chest, legs, feet, hand)) {
				player.sendMessage(Lang._("nbt.cmds.bos.no-mob"));
			} else {
				bos.saveBook();
				player.sendMessage(Lang._("nbt.cmds.bos.drop-chance.set"));
			}
			return true;
		}
		return false;
	}
	
}