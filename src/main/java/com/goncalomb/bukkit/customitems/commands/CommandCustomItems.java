package com.goncalomb.bukkit.customitems.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandException;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandListener;
import com.goncalomb.bukkit.bkglib.utils.Utils;
import com.goncalomb.bukkit.customitems.CustomItemsAPI;
import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.CustomItemManager;

public final class CommandCustomItems implements BKgCommandListener {
	
	private List<String> getCustomItemNamesList(String prefix) {
		ArrayList<String> names = new ArrayList<String>();
		CustomItemManager c = CustomItemManager.getInstance(CustomItemsAPI.getInstance());
		for (CustomItem citem : c.getCustomItems()) {
			String slug = citem.getSlug();
			if (citem.isEnabled() && slug.startsWith(prefix)) {
				names.add(slug);
			}
		}
		Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
		return names;
	}
	
	private void giveCustomItem(Player player, String slug, String amount) throws BKgCommandException {
		CustomItem customItem = CustomItemManager.getInstance(CustomItemsAPI.getInstance()).getCustomItem(slug);
		int intAmount = (amount == null ? 1 : Utils.parseInt(amount, -1));
		if (customItem == null) {
			throw new BKgCommandException(Lang._(CustomItemsAPI.class, "commands.customitem.no-item"));
		} else if (intAmount < 1) {
			throw new BKgCommandException(Lang._(null, "invalid-amount"));
		} else {
			ItemStack item = customItem.getItem();
			if (item == null) {
				throw new BKgCommandException(Lang._(CustomItemsAPI.class, "commands.customitem.citem.invalid"));
			} else {
				item.setAmount(intAmount);
				if (player.getInventory().addItem(item).size() > 0) {
					throw new BKgCommandException(Lang._(null, "inventory-full"));
				} else {
					player.sendMessage(Lang._(CustomItemsAPI.class, "commands.customitem.ok"));
				}
			}
		}
	}
	
	@Cmd(args = "customitem get", type = CmdType.PLAYER_ONLY, minargs = 1, maxargs = 2, usage = "<item> [amount]")
	public boolean customitem_get(CommandSender sender, String[] args) throws BKgCommandException {
		giveCustomItem((Player) sender, args[0], (args.length == 2 ? args[1] : null));
		return true;
	}
	
	@CmdTab(args = "customitem get")
	public List<String> customitem_get_Tab(CommandSender sender, String[] args) {
		return (args.length == 1 ? getCustomItemNamesList(args[0]) : null);
	}
	
	@Cmd(args = "customitem give", type = CmdType.DEFAULT, minargs = 2, maxargs = 3, usage = "<player> <item> [amount]")
	public boolean customitem_give(CommandSender sender, String[] args) throws BKgCommandException {
		Player player = Bukkit.getPlayer(args[0]);
		if (player == null) {
			throw new BKgCommandException(Lang._(null, "player-not-found.name", args[0]));
		}
		giveCustomItem(player, args[1], (args.length == 3 ? args[2] : null));
		return true;
	}
	
	@CmdTab(args = "customitem give")
	public List<String> customitem_give_Tab(CommandSender sender, String[] args) {
		if (args.length == 1) {
			return CommandUtils.playerTabComplete(sender, args[0]);
		} else if (args.length == 2) {
			return getCustomItemNamesList(args[1]);
		}
		return null;
	}
	
	@Cmd(args = "customitem list", type = CmdType.DEFAULT)
	public boolean customitem_list(CommandSender sender, String[] args) {
		CustomItemManager manager = CustomItemManager.getInstance(CustomItemsAPI.getInstance());
		World world = (sender instanceof Player ? ((Player) sender).getWorld() : null);
		for (Plugin plugin : manager.getOwningPlugins()) {
			StringBuilder sb = new StringBuilder("" + ChatColor.GOLD + ChatColor.ITALIC + plugin.getName() + ":");
			for (CustomItem customItem : manager.getCustomItems(plugin)) {
				sb.append(" ");
				if (!customItem.isEnabled()) {
					sb.append(ChatColor.RED);
				} else if (world != null && !customItem.isValidWorld(world)) {
					sb.append(ChatColor.YELLOW);
				} else {
					sb.append(ChatColor.WHITE);
				}
				sb.append(customItem.getSlug());
			}
			sender.sendMessage(sb.toString());
		}
		return true;
	}
	
}
