package com.goncalomb.bukkit.customitems.api;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

final class ListenerForInteractionEvents extends CustomItemListener<CustomItem> {
	
	private static HashSet<Material> _interationMaterials = new HashSet<Material>(Arrays.asList(new Material[] { Material.WORKBENCH, Material.CHEST, Material.ENDER_CHEST, Material.BREWING_STAND, Material.ENCHANTMENT_TABLE }));
	
	@Override
	public boolean put(CustomItem customItem) {
		try {
			if (isOverriden(customItem, "onLeftClick", PlayerInteractEvent.class, PlayerDetails.class)
					|| isOverriden(customItem, "onRightClick", PlayerInteractEvent.class, PlayerDetails.class)
					|| isOverriden(customItem, "onAttack", EntityDamageByEntityEvent.class, PlayerDetails.class)
					|| isOverriden(customItem, "onInteractEntity", PlayerInteractEntityEvent.class, PlayerDetails.class)) {
				return super.put(customItem);
			}
		} catch (NoSuchMethodException e) {
			throw new Error(e);
		}
		return false;
	}
	
	@EventHandler
	private void playerInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		if (action != Action.PHYSICAL) {
			if (action == Action.RIGHT_CLICK_BLOCK && _interationMaterials.contains(event.getClickedBlock().getType())) {
				return;
			}
			CustomItem customItem = get(event.getItem());
			if (customItem != null) {
				event.setCancelled(true);
				if (verifyCustomItem(customItem, event.getPlayer(), false)) {
					if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
						customItem.onRightClick(event, new PlayerDetails(event));
					} else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
						customItem.onLeftClick(event, new PlayerDetails(event));
					}
				}
			}
		}
	}
	
	@EventHandler
	private void playerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		CustomItem customItem = get(item);
		
		if (customItem != null) {
			if (verifyCustomItem(customItem, event.getPlayer(), false)) {
				event.setCancelled(true);
				customItem.onInteractEntity(event, new PlayerDetails(item, event.getPlayer()));
			}
		}
	}
	
	@EventHandler
	private void entityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		if (damager instanceof Player) {
			Player player = (Player) damager;
			ItemStack item = player.getItemInHand();
			CustomItem customItem = get(item);
			
			if (customItem != null) {
				if (verifyCustomItem(customItem, player, true)) {
					customItem.onAttack(event, new PlayerDetails(item, player));
				}
			}
		}
	}
	
}
