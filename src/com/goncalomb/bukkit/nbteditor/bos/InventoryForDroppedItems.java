package com.goncalomb.bukkit.nbteditor.bos;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.nbteditor.nbt.DroppedItemNBT;
import com.goncalomb.bukkit.betterplugin.Lang;

public final class InventoryForDroppedItems extends InventoryForSingleItem {
	
	private static HashMap<Integer, ItemStack> _placeholders = new HashMap<Integer, ItemStack>();
	
	static {
		_placeholders.put(4, createPlaceholder(Material.PAPER, Lang._("nbt.bos.item.pholder")));
	}
	
	private BookOfSouls _bos;
	
	public InventoryForDroppedItems(BookOfSouls bos, Player owner) {
		super(Lang._("nbt.bos.item.title"), _placeholders, ((DroppedItemNBT) bos.getEntityNBT()).getItem(), bos, owner);
		_bos = bos;
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		((DroppedItemNBT) _bos.getEntityNBT()).setItem(getContents()[4]);
		_bos.saveBook();
		((Player)event.getPlayer()).sendMessage(Lang._("nbt.bos.item.done"));
	}

}
