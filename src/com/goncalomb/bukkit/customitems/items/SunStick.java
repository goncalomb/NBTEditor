package com.goncalomb.bukkit.customitems.items;

import org.bukkit.ChatColor;

import com.goncalomb.bukkit.lang.Lang;

public final class SunStick extends TimeFirework {
	
	public SunStick() {
		super("sun-stick", ChatColor.GOLD + "Sun Stick", 6000);
		setLore(Lang._list("citems.item-lores.sun-stick"));
	}
	
}
