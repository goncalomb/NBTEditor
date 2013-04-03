package com.goncalomb.bukkit.customitems.api;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.goncalomb.bukkit.betterplugin.Lang;

abstract class CustomItemListener<T extends CustomItem> extends CustomItemContainer<T> implements Listener {
	
	protected final boolean isOverriden(CustomItem item, String methodName, Class<?>... methodParams) throws NoSuchMethodException {
		return isOverriden(item, CustomItem.class, methodName, methodParams);
	}
	
	protected final boolean isOverriden(CustomItem item, Class<? extends CustomItem> baseClass, String methodName, Class<?>... methodParams) throws NoSuchMethodException {
		return (item.getClass().getMethod(methodName, methodParams).getDeclaringClass() != baseClass);
	}
	
	protected final boolean verifyCustomItem(CustomItem customItem, World world) {
		return (customItem != null && customItem.isEnabled() && customItem.isValidWorld(world));
	}
	
	protected final boolean verifyCustomItem(CustomItem customItem, Player player, boolean silent) {
		if (customItem != null) {
			if (!customItem.isEnabled()) {
				if (!silent) player.sendMessage(Lang._("citems.disabled"));
			} else if (player != null && !player.hasPermission("customitemsapi.use." + customItem.getSlug())) {
				if (!silent) player.sendMessage(Lang._("citems.no-perm"));
			} else if (!customItem.isValidWorld(player.getWorld()) && !player.hasPermission("customitemsapi.world-override." + customItem.getSlug())) {
				if (!silent) player.sendMessage(Lang._("citems.invalid-world"));
			} else {
				return true;
			}
		}
		return false;
	}
	
}
