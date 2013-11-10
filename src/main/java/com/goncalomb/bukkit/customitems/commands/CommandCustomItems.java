package com.goncalomb.bukkit.customitems.commands;

import java.util.ArrayList;
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
	
	public void giveCustomItem(Player player, String slug, String amount) throws BKgCommandException {
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
	public boolean getCommand(CommandSender sender, String[] args) throws BKgCommandException {
		giveCustomItem((Player) sender, args[0], (args.length == 2 ? args[1] : null));
		return true;
	}
	
	@CmdTab(args = "customitem get")
	public List<String> getCommand_TabComplete(CommandSender sender, String[] args) {
		if (args.length == 1) {
			ArrayList<String> names = new ArrayList<String>();
			CustomItemManager c = CustomItemManager.getInstance(CustomItemsAPI.getInstance());
			for (CustomItem citem : c.getCustomItems()) {
				String slug = citem.getSlug();
				if (slug.startsWith(args[0])) {
					names.add(slug);
				}
			}
			return names;
		}
		return null;
	}
	
	@Cmd(args = "customitem give", type = CmdType.DEFAULT, minargs = 2, maxargs = 3, usage = "<player> <item> [amount]")
	public boolean giveCommand(CommandSender sender, String[] args) throws BKgCommandException {
		Player player = Bukkit.getPlayer(args[0]);
		if (player == null) {
			throw new BKgCommandException(Lang._(null, "player-not-found.name", args[0]));
		}
		giveCustomItem(player, args[1], (args.length == 3 ? args[2] : null));
		return true;
	}
	
	@Cmd(args = "customitem list", type = CmdType.DEFAULT)
	public boolean listCommand(CommandSender sender, String[] args) {
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
