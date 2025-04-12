package com.goncalomb.bukkit.nbteditor.commands.inventories;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.nbteditor.nbt.BaseNBT;
import com.goncalomb.bukkit.nbteditor.nbt.variables.ItemsVariable;
import org.bukkit.inventory.meta.CrossbowMeta;

import java.util.Arrays;

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
		Player p = (Player)event.getPlayer();
		if(Arrays.stream(getContents()).anyMatch(s -> s.getType() != null && s.getType() == Material.CROSSBOW)) {
			//Concurrent Modification Exception workaround
			ItemStack[] items = getContents().clone();
			for (int i = 0; i < items.length; i++) {
				ItemStack item = items[i];
				if (item != null && item.getType() != null && item.getType() == Material.CROSSBOW) {
					NBTTagCompound tags = NBTUtils.getItemStackTag(item);
					if(tags.hasKey("ChargedProjectiles")) {
						tags.remove("ChargedProjectiles");
						NBTUtils.setItemStackTag(item, tags);
						setItem(i, item);
					}
				}
			}
		}
		_variable.setItems(getContents());
		super.inventoryClose(event);
	}

}
