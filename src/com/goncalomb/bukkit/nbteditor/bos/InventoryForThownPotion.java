package com.goncalomb.bukkit.nbteditor.bos;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.betterplugin.Lang;
import com.goncalomb.bukkit.nbteditor.nbt.ThrownPotionNBT;

public final class InventoryForThownPotion extends InventoryForSingleItem {
	
	private static HashMap<Integer, ItemStack> _placeholders = new HashMap<Integer, ItemStack>();
	
	static {
		_placeholders.put(4, createPlaceholder(Material.GLASS_BOTTLE, Lang._("nbt.bos.potion.pholder")));
	}
	
	private BookOfSouls _bos;
	
	public InventoryForThownPotion(BookOfSouls bos, Player owner) {
		super(Lang._("nbt.bos.potion.title"), _placeholders, ((ThrownPotionNBT) bos.getEntityNBT()).getPotion(), bos, owner);
		_bos = bos;
	}
	
	@Override
	protected void inventoryClick(InventoryClickEvent event) {
		super.inventoryClick(event);
		int slot = event.getRawSlot();
		boolean isShift = event.isShiftClick();
		ItemStack itemToCheck = null;
		if (isShift && slot > 8 && event.getInventory().firstEmpty() == 4) {
			itemToCheck = event.getCurrentItem();
		} else if (slot == 4 && !isShift && event.getCursor().getType() != Material.AIR) {
			itemToCheck = event.getCursor();
		}
		if (itemToCheck != null) {
			if (itemToCheck.getType() != Material.POTION) {
				((Player)event.getWhoClicked()).sendMessage(Lang._("nbt.bos.mob.potion"));
				event.setCancelled(true);
			}
		}
	}
	
	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		((ThrownPotionNBT) _bos.getEntityNBT()).setPotion(getContents()[4]);
		_bos.saveBook();
		((Player)event.getPlayer()).sendMessage(Lang._("nbt.bos.potion.done"));
	}

}
