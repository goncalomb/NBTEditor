package com.goncalomb.bukkit.customitems.items;

import org.bukkit.ChatColor;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityShootBowEvent;

import com.goncalomb.bukkit.customitems.api.CustomBow;
import com.goncalomb.bukkit.customitems.api.DelayedPlayerDetails;
import com.goncalomb.bukkit.lang.Lang;

public final class EnderBow extends CustomBow {
	
	public EnderBow() {
		super("ender-bow", ChatColor.GREEN + "Ender Bow");
		setLore(Lang._list("citems.item-lores.ender-bow"));
	}
	
	@Override
	public void onShootBow(EntityShootBowEvent event, DelayedPlayerDetails details) {
		Entity perl = event.getEntity().launchProjectile(EnderPearl.class);
		perl.setVelocity(event.getProjectile().getVelocity());
		event.setProjectile(perl);
	}

}
