package com.goncalomb.bukkit.customitems.items;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.material.MaterialData;

import com.goncalomb.bukkit.lang.Lang;

public final class LightningRod extends GenericBomb {
	
	public LightningRod() {
		super("lightning-rod", ChatColor.GRAY + "Lightning Rod", new MaterialData(Material.IRON_INGOT), true);
		setLore(Lang._list("citems.item-lores.lightning-rod"));
		setDefaultConfig("fuse", 40);
	}
	
	@Override
	public void onExplode(Item item, Location location) {
		item.getWorld().strikeLightning(location);
	}

}
