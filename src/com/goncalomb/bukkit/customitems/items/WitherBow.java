package com.goncalomb.bukkit.customitems.items;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.entity.EntityShootBowEvent;

import com.goncalomb.bukkit.customitems.api.CustomBow;
import com.goncalomb.bukkit.customitems.api.DelayedPlayerDetails;
import com.goncalomb.bukkit.lang.Lang;

public final class WitherBow extends CustomBow {
	
	public WitherBow() {
		super("wither-bow", ChatColor.GREEN + "Wither Bow");
		setLore(Lang._list("citems.item-lores.wither-bow"));
	}
	
	@Override
	public void onShootBow(EntityShootBowEvent event, DelayedPlayerDetails details) {
		Entity skull = event.getEntity().launchProjectile(WitherSkull.class);
		skull.setVelocity(event.getProjectile().getVelocity());
		event.setProjectile(skull);
	}

}
