package com.goncalomb.bukkit.customitems.items;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.material.MaterialData;

public final class LightningRod extends GenericBomb {
	
	public LightningRod() {
		super("lightning-rod", ChatColor.GRAY + "Lightning Rod", new MaterialData(Material.IRON_INGOT), true);
		setLore("§bLeft-click or drop key to throw the rod.",
				"§bLightning will strike after a few seconds.");
		setDefaultConfig("fuse", 40);
	}
	
	@Override
	public void onExplode(Item item, Location location) {
		item.getWorld().strikeLightning(location);
	}

}
