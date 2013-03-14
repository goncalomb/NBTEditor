package com.goncalomb.bukkit.customitems.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.goncalomb.bukkit.Utils;
import com.goncalomb.bukkit.betterplugin.BetterCommand;
import com.goncalomb.bukkit.betterplugin.BetterCommandException;
import com.goncalomb.bukkit.betterplugin.BetterSubCommandType;
import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.CustomItemManager;
import com.goncalomb.bukkit.lang.Lang;

public final class CommandCustomItem extends BetterCommand {
	
	private CustomItemManager _manager;
	
	public CommandCustomItem(CustomItemManager manager) {
		super("customitem", "customitems.customitem");
		_manager = manager;
		setAlises("citem");
		setDescription(Lang._("citems.cmds.citem.description"));
	}
	
	public void giveCustomItem(Player player, String slug, String amount) throws BetterCommandException {
		CustomItem customItem = _manager.getCustomItem(slug);
		int intAmount = (amount == null ? 1 : Utils.parseInt(amount, -1));
		if (customItem == null) {
			throw new BetterCommandException(Lang._("citems.cmds.citem.no-item"));
		} else if (intAmount < 1) {
			throw new BetterCommandException(Lang._("general.invalid-amount"));
		} else {
			ItemStack item = customItem.getItem();
			if (item == null) {
				throw new BetterCommandException(Lang._("citems.cmds.citem.invalid"));
			} else {
				item.setAmount(intAmount);
				if (player.getInventory().addItem(item).size() > 0) {
					throw new BetterCommandException(Lang._("general.inventory-full"));
				} else {
					player.sendMessage(Lang._("citems.cmds.citem.ok"));
				}
			}
		}
	}
	
	@SubCommand(args = "get", type = BetterSubCommandType.PLAYER_ONLY, minargs = 1, maxargs = 2, usage = "<item> [amount]")
	public boolean getCommand(CommandSender sender, String[] args) throws BetterCommandException {
		giveCustomItem((Player) sender, args[0], (args.length == 2 ? args[1] : null));
		return true;
	}
	
	@SubCommand(args = "give", type = BetterSubCommandType.DEFAULT, minargs = 2, maxargs = 3, usage = "<player> <item> [amount]")
	public boolean giveCommand(CommandSender sender, String[] args) throws BetterCommandException {
		Player player = Bukkit.getPlayer(args[0]);
		if (player == null) {
			throw new BetterCommandException("Player not found!");
		}
		giveCustomItem(player, args[1], (args.length == 3 ? args[2] : null));
		return true;
	}
	
	@SubCommand(args = "list", type = BetterSubCommandType.DEFAULT)
	public boolean listCommand(CommandSender sender, String[] args) {
		World world = (sender instanceof Player ? ((Player) sender).getWorld() : null);
		for (Plugin plugin : _manager.getOwningPlugins()) {
			StringBuilder sb = new StringBuilder("" + ChatColor.GOLD + ChatColor.ITALIC + plugin.getName() + ":");
			for (CustomItem customItem : _manager.getCustomItems(plugin)) {
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
