package com.goncalomb.bukkit.nbteditor.bos;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.betterplugin.Lang;
import com.goncalomb.bukkit.nbteditor.nbt.FireworkNBT;

public final class InventoryForFirework extends InventoryForSingleItem {
	
	private static HashMap<Integer, ItemStack> _placeholders = new HashMap<Integer, ItemStack>();
	
	static {
		_placeholders.put(4, createPlaceholder(Material.PAPER, Lang._("nbt.bos.firework.pholder")));
	}
	
	private BookOfSouls _bos;
	
	public InventoryForFirework(BookOfSouls bos, Player owner) {
		super(Lang._("nbt.bos.firework.title"), _placeholders, ((FireworkNBT) bos.getEntityNBT()).getFirework(), bos, owner);
		_bos = bos;
	}
	
	@Override
	protected void inventoryClick(InventoryClickEvent event) {
		super.inventoryClick(event);
		ItemStack itemToCheck = getItemToCheck(event);
		if (itemToCheck != null && itemToCheck.getType() != Material.FIREWORK) {
			((Player)event.getWhoClicked()).sendMessage(Lang._("nbt.bos.firework.nop"));
			event.setCancelled(true);
		}
	}
	
	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		((FireworkNBT) _bos.getEntityNBT()).setFirework(getContents()[4]);
		_bos.saveBook();
		((Player)event.getPlayer()).sendMessage(Lang._("nbt.bos.firework.done"));
	}

}
