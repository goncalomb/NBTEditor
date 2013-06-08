package com.goncalomb.bukkit.customitems.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

import com.goncalomb.bukkit.UtilsMc;
import com.goncalomb.bukkit.betterplugin.Lang;
import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.PlayerInventoryDetails;

public class KingsCrown extends CustomItem {
	
	public KingsCrown() {
		super("kings-crown", ChatColor.GOLD + "King's Crown", new MaterialData(Material.GOLD_HELMET));
		addEnchantment(Enchantment.PROTECTION_FALL, 4);
	}
	
	@Override
	public void onPickup(PlayerPickupItemEvent event) {
		PlayerInventory inv = event.getPlayer().getInventory();
		ItemStack lastHelmet = inv.getHelmet();
		if (lastHelmet == null || inv.addItem(lastHelmet).size() == 0) {
			inv.setHelmet(event.getItem().getItemStack());
			event.getItem().remove();
			event.setCancelled(true);
			UtilsMc.broadcastToWorld(event.getPlayer().getWorld(), Lang._format("citems.crown.found", event.getPlayer().getName()));
		}
	}
	
	@Override
	public void onDrop(PlayerDropItemEvent event) {
		lostCrown(event.getPlayer());
	}
	
	@Override
	public void onDespawn(ItemDespawnEvent event) {
		UtilsMc.broadcastToWorld(event.getEntity().getWorld(), Lang._("citems.crown.despawn"));
	}
	
	@Override
	public void onPlayerDeath(PlayerDeathEvent event, PlayerInventoryDetails details) {
		lostCrown(event.getEntity());
	}
	
	private void lostCrown(Player player) {
		UtilsMc.broadcastToWorld(player.getWorld(), Lang._format("citems.crown.lost", player.getName()));
	}
	
}
