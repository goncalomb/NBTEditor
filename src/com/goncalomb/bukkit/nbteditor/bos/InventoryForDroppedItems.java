package com.goncalomb.bukkit.nbteditor.bos;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.nbteditor.nbt.DroppedItemNBT;
import com.goncalomb.bukkit.lang.Lang;

public class InventoryForDroppedItems extends IInventoryForBos {
	
	private static HashMap<Integer, ItemStack> _placeholders = new HashMap<Integer, ItemStack>();
	
	static {
		_placeholders.put(4, createPlaceholder(Material.PAPER, Lang._("nbt.bos.item.pholder")));
	}
	
	private BookOfSouls _bos;
	
	public InventoryForDroppedItems(BookOfSouls bos, Player owner) {
		super(owner, 9, Lang._("nbt.bos.item.title"), _placeholders);
		_bos = bos;
		Inventory inv = getInventory();
		ItemStack item = ((DroppedItemNBT) _bos.getEntityNBT()).getItem();
		for (int i = 0; i < 9; ++i) {
			if (i != 4) {
				inv.setItem(i, _itemFiller);
			}
		}
		if (item != null) {
			inv.setItem(4, item);
		}
	}

	@Override
	protected void inventoryClick(InventoryClickEvent event) {
		super.inventoryClick(event);
		if (event.getRawSlot() != 4 && event.getRawSlot() < 9) {
			event.setCancelled(true);
		}
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		((DroppedItemNBT) _bos.getEntityNBT()).setItem(getContents()[4]);
		_bos.saveBook();
		((Player)event.getPlayer()).sendMessage(Lang._("nbt.bos.item.done"));
	}

}
