package com.goncalomb.bukkit.nbteditor.bos;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.goncalomb.bukkit.UtilsMc;
import com.goncalomb.bukkit.betterplugin.Lang;
import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.DispenserDetails;
import com.goncalomb.bukkit.customitems.api.PlayerDetails;
import com.goncalomb.bukkit.nbteditor.nbt.MinecartSpawnerNBT;

final class BookOfSoulsCI extends CustomItem {

	public BookOfSoulsCI() {
		super("bos", ChatColor.AQUA + "Book of Souls", new MaterialData(Material.WRITTEN_BOOK));
	}
	
	@Override
	public void onLeftClick(PlayerInteractEvent event, PlayerDetails details) {
		Player player = event.getPlayer();
		if (!player.hasPermission("nbteditor.bookofsouls")) {
			player.sendMessage(Lang._("general.no-perm"));
			return;
		}
		
		BookOfSouls bos = BookOfSouls.getFromBook(event.getItem());
		if (bos == null) {
			player.sendMessage(Lang._("nbt.bos.corrupted"));
			return;
		}
		
		Location location = null;
		if (event.getAction() == Action.LEFT_CLICK_BLOCK && bos.getEntityNBT() instanceof MinecartSpawnerNBT) {
			Block block = event.getClickedBlock();
			if (block.getType() == Material.MOB_SPAWNER) {
				if (event.getPlayer().isSneaking()) {
					((MinecartSpawnerNBT) bos.getEntityNBT()).copyToSpawner(block);
					player.sendMessage(Lang._("nbt.bos.minecart-from"));
				} else {
					((MinecartSpawnerNBT) bos.getEntityNBT()).copyFromSpawner(block);
					bos.saveBook();
					player.sendMessage(Lang._("nbt.bos.minecart-to"));
				}
				event.setCancelled(true);
				return;
			}
			location = event.getClickedBlock().getLocation().add(UtilsMc.faceToDelta(event.getBlockFace(), 0.5));
		} else {
			Block block = UtilsMc.getTargetBlock(player);
			if (block.getType() != Material.AIR) {
				location = UtilsMc.airLocation(block.getLocation());
			}
		}
		
		if (location != null) {
			bos.getEntityNBT().spawnStack(location);
			event.setCancelled(true);
		} else {
			player.sendMessage(Lang._("general.no-sight"));
		}
		return;
	};
	
	@Override
    public void onDispense(BlockDispenseEvent event, DispenserDetails details) {
		BookOfSouls bos = BookOfSouls.getFromBook(event.getItem());
		if (bos != null) {
			bos.getEntityNBT().spawnStack(details.getLocation());
		}
		event.setCancelled(true);
    }
	
	@Override
	public ItemStack getItem() {
		return null;
	}
	
}
