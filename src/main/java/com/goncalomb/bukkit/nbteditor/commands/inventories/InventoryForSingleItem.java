package com.goncalomb.bukkit.nbteditor.commands.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.nbteditor.nbt.BaseNBT;
import com.goncalomb.bukkit.nbteditor.nbt.variables.EffectsVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.SingleItemVariable;

public class InventoryForSingleItem extends InventoryForSpecialVariable<SingleItemVariable> {

	public InventoryForSingleItem(Player owner, BaseNBT wrapper, SingleItemVariable variable) {
		super(owner, wrapper, variable);
		for (int i = 0; i < 9; ++i) {
			if (i != 4) {
				setItem(i, ITEM_FILLER);
			}
		}
		ItemStack item = variable.getItem();
		if (item != null) {
			setItem(4, item);
		} else {
			setPlaceholder(4, createPlaceholder(Material.PAPER, "§6" + _variable.getFormat()));
		}
		if (variable instanceof EffectsVariable) {
			// XXX: remove this alert, implement default potion fallback on NBTUtils
			owner.sendMessage("§eWhen setting effects you must use a custom potion. §nNormal potions don't work.");
			owner.sendMessage("§eGrab any potion and use '/nbtpotion' to edit.");
		}
	}

	protected ItemStack getItemToCheck(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		boolean isShift = event.isShiftClick();
		if (isShift && slot > 8 && event.getInventory().firstEmpty() == 4) {
			return event.getCurrentItem();
		} else if (slot == 4 && !isShift && event.getCursor().getType() != Material.AIR) {
			return event.getCursor();
		}
		return null;
	}

	@Override
	protected void inventoryClick(InventoryClickEvent event) {
		super.inventoryClick(event);
		int slot = event.getRawSlot();
		boolean isShift = event.isShiftClick();
		ItemStack item = null;
		if (isShift && slot > 8 && event.getInventory().firstEmpty() == 4) {
			item = event.getCurrentItem();
		} else if (slot == 4 && !isShift && event.getCursor().getType() != Material.AIR) {
			item = event.getCursor();
		}
		if (item != null) {
			if (!isValidItem((Player) event.getWhoClicked(), item)) {
				event.setCancelled(true);
			}
		}
	}

	protected boolean isValidItem(Player player, ItemStack item) {
		return _variable.isValidItem(player, item);
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		_variable.setItem(getContents()[4]);
		super.inventoryClose(event);
	}

}
