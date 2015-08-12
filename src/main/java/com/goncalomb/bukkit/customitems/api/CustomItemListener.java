/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
 *
 * This file is part of NBTEditor.
 *
 * NBTEditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NBTEditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NBTEditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.customitems.api;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

final class CustomItemListener implements Listener {
	
	private static final HashSet<Material> _interationMaterials = new HashSet<Material>(Arrays.asList(new Material[] { Material.WORKBENCH, Material.CHEST, Material.ENDER_CHEST, Material.BREWING_STAND, Material.ENCHANTMENT_TABLE }));
	
	private static boolean verifyCustomItem(CustomItem customItem, World world) {
		return (customItem != null && customItem.isEnabled() && customItem.isValidWorld(world));
	}
	
	private static boolean verifyCustomItem(CustomItem customItem, Player player, boolean silent) {
		if (customItem != null) {
			if (!customItem.isEnabled()) {
				if (!silent) player.sendMessage("§cThis item is disabled!");
			} else if (player != null && !player.hasPermission("nbteditor.customitems.use." + customItem.getSlug())) {
				if (!silent) player.sendMessage("§cYou don't have permission to use this item!");
			} else if (!customItem.isValidWorld(player.getWorld()) && !player.hasPermission("nbteditor.customitems.world-override." + customItem.getSlug())) {
				if (!silent) player.sendMessage("§cThis item is disabled on this world!");
			} else {
				return true;
			}
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
			CustomItem customItem = CustomItemManager.getCustomItem(event.getItem());
			if (customItem != null) {
				if (verifyCustomItem(customItem, event.getPlayer(), false)) {
					if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
						customItem.onRightClick(event, new PlayerDetails(event));
					} else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
						customItem.onLeftClick(event, new PlayerDetails(event));
					}
				} else {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	private void playerInteract(BlockBreakEvent event) {
		ItemStack item = event.getPlayer().getItemInHand();
		CustomItem customItem = CustomItemManager.getCustomItem(item);
		if (customItem != null) {
			if (verifyCustomItem(customItem, event.getPlayer(), false)) {
				customItem.onBlockBreak(event, new PlayerDetails(event));
			} else {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	private void playerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		CustomItem customItem = CustomItemManager.getCustomItem(item);
		
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
			CustomItem customItem = CustomItemManager.getCustomItem(item);
			
			if (customItem != null) {
				if (verifyCustomItem(customItem, player, true)) {
					customItem.onAttack(event, new PlayerDetails(item, player));
				}
			}
		} else {
			if (damager.hasMetadata("CustomItem-bow")) {
				Object[] data = (Object[]) damager.getMetadata("CustomItem-bow").get(0).value();
				((CustomBow) data[0]).onProjectileDamageEntity(event, (DelayedPlayerDetails) data[1]);
			}
		}
	}
	
	@EventHandler
	private void playerPickupItem(PlayerPickupItemEvent event) {
		CustomItem customItem = CustomItemManager.getCustomItem(event.getItem().getItemStack());
		if (verifyCustomItem(customItem, event.getPlayer(), true)) {
			customItem.onPickup(event);
		}
	}
	
	@EventHandler
	private void playerDropItem(PlayerDropItemEvent event) {
		CustomItem customItem = CustomItemManager.getCustomItem(event.getItemDrop().getItemStack());
		if (verifyCustomItem(customItem, event.getPlayer(), true)) {
			customItem.onDrop(event);
		}
	}
	
	@EventHandler
	private void itemDespawnItem(ItemDespawnEvent event) {
		CustomItem customItem = CustomItemManager.getCustomItem(event.getEntity().getItemStack());
		if (verifyCustomItem(customItem, event.getEntity().getWorld())) {
			customItem.onDespawn(event);
		}
	}
	
	@EventHandler
	private void inventoryPickupItemItem(InventoryPickupItemEvent event) {
		CustomItem customItem = CustomItemManager.getCustomItem(event.getItem().getItemStack());
		if (verifyCustomItem(customItem, event.getItem().getWorld())) {
			customItem.onDropperPickup(event);
		}
	}
	
	@EventHandler
	private void entityShootBow(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			CustomItem customItem = CustomItemManager.getCustomItem(event.getBow());
			if (verifyCustomItem(customItem, player, false)) {
				DelayedPlayerDetails details = new DelayedPlayerDetails(event.getBow(), player);
				((CustomBow) customItem).onShootBow(event, details);
				if (!event.isCancelled() && event.getProjectile() instanceof Projectile) {
					details.lock();
					event.getProjectile().setMetadata("CustomItem-bow", new FixedMetadataValue(CustomItemManager._plugin, new Object[] { customItem, details }));
				}
			}
		}
	}
	
	@EventHandler
	private void projectileHit(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		if (projectile.hasMetadata("CustomItem-bow")) {
			Object[] data = (Object[]) projectile.getMetadata("CustomItem-bow").get(0).value();
			((CustomBow) data[0]).onProjectileHit(event, (DelayedPlayerDetails) data[1]);
		}
	}
	
	@EventHandler
	private void blockDispense(BlockDispenseEvent event) {
		if (event.getBlock().getType() != Material.DISPENSER) {
			return;
		}
		CustomItem customItem = CustomItemManager.getCustomItem(event.getItem());
		if (customItem != null) {
			if (verifyCustomItem(customItem, event.getBlock().getWorld())) {
				customItem.onDispense(event, new DispenserDetails(event, customItem._owner));
			} else {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	private void playerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		for (ItemStack item : event.getDrops()) {
			CustomItem customItem = CustomItemManager.getCustomItem(item);
			if (verifyCustomItem(customItem, player, true)) {
				// This is very dirty, all "Details" classes need to be refactored.
				customItem.onPlayerDeath(event, new PlayerDetails(item, player) {
					@Override
					public void consumeItem() {
						if (_player.getGameMode() == GameMode.CREATIVE) return;
						_item.setAmount(_item.getAmount() - 1);
					}
				});
			}
		}
	}
	
}
