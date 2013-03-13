package com.goncalomb.bukkit.customitems.items;

import org.bukkit.ChatColor;

import com.goncalomb.bukkit.lang.Lang;

public final class MoonStick extends TimeFirework {
	
	public MoonStick() {
		super("moon-stick", ChatColor.DARK_AQUA + "Moon Stick", 18000);
		setLore(Lang._list("citems.item-lores.moon-stick"));
	}
	
}
