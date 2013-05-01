package com.goncalomb.bukkit.nbteditor.bos;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.material.MaterialData;

import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.PlayerDetails;
import com.goncalomb.bukkit.nbteditor.nbt.EntityNBT;

final class BookOfSoulsEmptyCI extends CustomItem {

	public BookOfSoulsEmptyCI() {
		super("bos-empty", ChatColor.AQUA + "Book of Souls" + ChatColor.RESET + " - " + ChatColor.RED + "Empty", new MaterialData(Material.BOOK));
		setLore("§bThis is a empty Book of Souls.",
				"§bLeft-click on an existing entity to capture his soul.");
	}
	
	@Override
	public void onInteractEntity(final PlayerInteractEntityEvent event, PlayerDetails details) {
		System.out.println("sadasdasds");
		if (EntityNBT.isValidType(event.getRightClicked().getType())) {
			details.consumeItem();
			Bukkit.getScheduler().runTask(getPlugin(), new Runnable() {
				@Override
				public void run() {
					event.getPlayer().getInventory().addItem((new BookOfSouls(EntityNBT.fromEntity(event.getRightClicked()))).getBook());
				}
			});
			event.setCancelled(true);
		}
	}
	
}
