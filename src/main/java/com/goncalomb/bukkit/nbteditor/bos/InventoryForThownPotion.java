package com.goncalomb.bukkit.nbteditor.bos;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.nbteditor.NBTEditor;
import com.goncalomb.bukkit.nbteditor.nbt.ThrownPotionNBT;

public final class InventoryForThownPotion extends InventoryForSingleItem {
	
	private static HashMap<Integer, ItemStack> _placeholders = new HashMap<Integer, ItemStack>();
	
	static {
		_placeholders.put(4, createPlaceholder(Material.GLASS_BOTTLE, Lang._(NBTEditor.class, "bos.potion.pholder")));
	}
	
	private BookOfSouls _bos;
	
	public InventoryForThownPotion(BookOfSouls bos, Player owner) {
		super(Lang._(NBTEditor.class, "bos.potion.title"), _placeholders, ((ThrownPotionNBT) bos.getEntityNBT()).getPotion(), bos, owner);
		_bos = bos;
	}
	
	@Override
	protected void inventoryClick(InventoryClickEvent event) {
		super.inventoryClick(event);
		ItemStack itemToCheck = getItemToCheck(event);
		if (itemToCheck != null && itemToCheck.getType() != Material.POTION) {
			((Player)event.getWhoClicked()).sendMessage(Lang._(NBTEditor.class, "bos.mob.potion"));
			event.setCancelled(true);
		}
	}
	
	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		((ThrownPotionNBT) _bos.getEntityNBT()).setPotion(getContents()[4]);
		_bos.saveBook();
		((Player)event.getPlayer()).sendMessage(Lang._(NBTEditor.class, "bos.potion.done"));
	}

}
