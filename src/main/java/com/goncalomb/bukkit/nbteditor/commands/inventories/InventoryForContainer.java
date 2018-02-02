package com.goncalomb.bukkit.nbteditor.commands.inventories;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.goncalomb.bukkit.nbteditor.nbt.BaseNBT;
import com.goncalomb.bukkit.nbteditor.nbt.variables.ContainerVariable;

public class InventoryForContainer extends InventoryForSpecialVariable<ContainerVariable> {

	public InventoryForContainer(Player owner, BaseNBT wrapper, ContainerVariable variable) {
		super(owner, wrapper, variable, ((variable.size + 8)/9)*9);
		_variable.toInventoryItems(_inventory);
		for (int i = _variable.size, l = _inventory.getSize(); i < l; i++) {
			setItem(i, ITEM_FILLER);
		}
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		_variable.fromInventoryItems(_inventory);
		super.inventoryClose(event);
	}

}
