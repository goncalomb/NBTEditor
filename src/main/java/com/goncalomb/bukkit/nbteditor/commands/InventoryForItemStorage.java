package com.goncalomb.bukkit.nbteditor.commands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.goncalomb.bukkit.mylib.utils.CustomInventory;
import com.goncalomb.bukkit.mylib.utils.UtilsMc;
import com.goncalomb.bukkit.nbteditor.ItemStorage;

public final class InventoryForItemStorage extends CustomInventory {

	private final int ITEMS_PER_PAGE = 54 - 9;
	private int _page = 0;
	private ItemStack[] _originaItems;

	private static void setItemStackName(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
	}

	public InventoryForItemStorage(Player owner) {
		super(owner, 54, "Global Item Storage");
		_inventory.setItem(52, UtilsMc.newSingleItemStack(Material.ARROW, "[0] Previous Page"));
		_inventory.setItem(53, UtilsMc.newSingleItemStack(Material.ARROW, "[0] Next Page"));
		changePage(0);
	}

	private void changePage(int offset) {
		int page = _page + offset;
		if (page >= 0) {
			int index = page * ITEMS_PER_PAGE;
			String[] names = ItemStorage.listItems().toArray(new String[0]);
			if (index > names.length) {
				page = names.length / ITEMS_PER_PAGE;
				index = page * ITEMS_PER_PAGE;
			}
			_originaItems = new ItemStack[ITEMS_PER_PAGE];
			for (int i = 0, j = index; i < ITEMS_PER_PAGE; i++, j++) {
				if (j < names.length) {
					_originaItems[i] = ItemStorage.getItem(names[j]);
					ItemStack item = new ItemStack(_originaItems[i]);
					ItemMeta meta = item.getItemMeta();
					ArrayList<String> lore = new ArrayList<String>();
					lore.add("§r[ " + names[j] + " ]");
					if (meta.hasLore()) {
						lore.addAll(meta.getLore());
					}
					meta.setLore(lore);
					item.setItemMeta(meta);
					_inventory.setItem(i, item);
				} else {
					_inventory.setItem(i, null);
				}
			}
			_page = page;
			setItemStackName(_inventory.getItem(52), "[" + _page + "] Previous Page");
			setItemStackName(_inventory.getItem(53), "[" + _page + "] Next Page");
		}
	}

	@Override
	protected void inventoryClick(InventoryClickEvent event) {
		Inventory inventory = event.getClickedInventory();
		if (inventory != null) {
			InventoryAction action = event.getAction();
			if (action == InventoryAction.COLLECT_TO_CURSOR) {
				event.setCancelled(true);
			} else if (inventory.getType() == InventoryType.PLAYER) {
				if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
					event.setCancelled(true);
				}
			} else {
				event.setCancelled(true);
				int slot = event.getSlot();
				if (slot < ITEMS_PER_PAGE) {
					if (event.getCursor().getType() == Material.AIR && _originaItems[slot] != null) {
						event.getView().setCursor(_originaItems[slot]);
					}
				} else if (slot == 52) {
					// previous page
					changePage(-1);
				} else if (slot == 53) {
					// next page
					changePage(1);
				}
			}
		}
		
	}
	
	@Override
	protected void inventoryDrag(InventoryDragEvent event) {
		InventoryView view = event.getView();
		for (int slot : event.getRawSlots()) {
			if (view.getInventory(slot).getType() != InventoryType.PLAYER) {
				event.setCancelled(true);
				return;
			}
		}
	}

}
