package com.goncalomb.bukkit.nbteditor.commands.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.nbteditor.nbt.BaseNBT;
import com.goncalomb.bukkit.nbteditor.nbt.variables.ItemsVariable;

public class InventoryForItems extends InventoryForSpecialVariable<ItemsVariable> {

	public InventoryForItems(Player owner, BaseNBT wrapper, ItemsVariable variable) {
		super(owner, wrapper, variable);
		ItemStack[] items = _variable.getItems();
		int i = 0;
		for (; i < _variable.count(); i++) {
			if (items[i] != null && items[i].getType() != Material.AIR) {
				setItem(i, items[i]);
			} else {
				setPlaceholder(i, createPlaceholder(Material.PAPER, "§6" + _variable.getDescription(i)));
			}
		}
		for (; i < 9; i++) {
			setItem(i, ITEM_FILLER);
		}
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		_variable.setItems(getContents());
		super.inventoryClose(event);
	}

}
