package com.goncalomb.bukkit.customitems.items;

import org.bukkit.ChatColor;

public final class SunStick extends TimeFirework {
	
	public SunStick() {
		super("sun-stick", ChatColor.GOLD + "Sun Stick", 6000);
		setLore("§bA magic flying stick to controls time.",
				"§bUse outside.");
	}
	
}
