package com.goncalomb.bukkit.customitems.api;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.goncalomb.bukkit.lang.Lang;

abstract class CustomItemListener<T extends CustomItem> extends CustomItemContainer<T> implements Listener {
	
	protected final boolean isOverriden(CustomItem item, String methodName, Class<?>... methodParams) throws NoSuchMethodException {
		return isOverriden(item, CustomItem.class, methodName, methodParams);
	}
	
	protected final boolean isOverriden(CustomItem item, Class<? extends CustomItem> baseClass, String methodName, Class<?>... methodParams) throws NoSuchMethodException {
		return (item.getClass().getMethod(methodName, methodParams).getDeclaringClass() != baseClass);
	}
	
	protected final boolean verifyCustomItem(CustomItem customItem, World world) {
		return verifyCustomItem(customItem, world, null, true);
	}
	
	protected final boolean verifyCustomItem(CustomItem customItem, Player player, boolean silent) {
		return verifyCustomItem(customItem, player.getWorld(), player, silent);
	}
	
	protected final boolean verifyCustomItem(CustomItem customItem, World world, Player player, boolean silent) {
		if (customItem != null) {
			if (!customItem.isEnabled()) {
				if (!silent && player != null) player.sendMessage(Lang._("citems.disabled"));
			} else if (player != null && !player.hasPermission("customitemx.item." + customItem.getSlug() + ".use")) {
				if (!silent) player.sendMessage(Lang._("citems.no-perm"));
			} else if (!customItem.isValidWorld(world)) {
				if (!silent && player != null) player.sendMessage(Lang._("citems.invalid-world"));
			} else {
				return true;
			}
		}
		return false;
	}
	
}
