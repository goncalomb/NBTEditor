package com.goncalomb.bukkit.customitems.items;

import org.bukkit.ChatColor;

public final class MoonStick extends TimeFirework {
	
	public MoonStick() {
		super("moon-stick", ChatColor.DARK_AQUA + "Moon Stick", 18000);
		setLore("§bA magic flying stick to controls time.",
				"§bUse outside.");
	}
	
}
