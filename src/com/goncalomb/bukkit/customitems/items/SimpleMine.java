package com.goncalomb.bukkit.customitems.items;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.goncalomb.bukkit.customitems.api.CustomItem;

public final class SimpleMine extends CustomItem {
	
	public SimpleMine() {
		super("simple-mine", ChatColor.GREEN + "Mine", new MaterialData(Material.FLOWER_POT_ITEM));
	}
	
	@Override
	public void onPickup(PlayerPickupItemEvent event) {
		event.setCancelled(true);
		Item item = event.getItem();
		ItemStack stack = item.getItemStack();
		Location loc = item.getLocation();
		item.remove();
		loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 4f + 0.2f*stack.getAmount(), false, false);
	}
	
	@Override
	public void onDrop(PlayerDropItemEvent event) {
		event.getItemDrop().getItemStack().addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 0);
	}
	
}
