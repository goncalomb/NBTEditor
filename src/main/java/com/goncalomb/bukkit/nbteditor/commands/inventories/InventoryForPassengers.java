package com.goncalomb.bukkit.nbteditor.commands.inventories;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.nbteditor.bos.BookOfSouls;
import com.goncalomb.bukkit.nbteditor.nbt.BaseNBT;
import com.goncalomb.bukkit.nbteditor.nbt.EntityNBT;
import com.goncalomb.bukkit.nbteditor.nbt.variables.PassengersVariable;

public class InventoryForPassengers extends InventoryForSpecialVariable<PassengersVariable> {

	private static ItemStack placeholder = createPlaceholder(Material.PAPER, "§6Put Books of Souls here to add entities.");

	public InventoryForPassengers(Player owner, BaseNBT wrapper, PassengersVariable variable) {
		super(owner, wrapper, variable, 54, wrapper.getId() + " (Passengers)", true);
		setPlaceholder(53, placeholder);
		int i = 0;
		for (EntityNBT passager : variable.getPassengers()) {
			setItem(i++, (new BookOfSouls(passager)).getBook());
		}
	}

	@Override
	protected void inventoryClick(InventoryClickEvent event) {
		super.inventoryClick(event);
		ItemStack item = event.getCurrentItem();
		if (item != null && item.getType() != Material.AIR) {
			Player player = (Player) event.getWhoClicked();
			if (item.equals(((EntityNBT) _wrapper)._bos.getBook())) {
				event.setCancelled(true);
			} else if (!BookOfSouls.isValidBook(item)) {
				event.setCancelled(true);
				player.sendMessage("§cYou can only put Books of Souls.");
			} else {
				EntityNBT entityNbt = BookOfSouls.bookToEntityNBT(item);
				if (entityNbt == null) {
					player.sendMessage("§cThat Book of Souls is corrupted!");
					event.setCancelled(true);
				}
			}
		}
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		ArrayList<EntityNBT> passagers = new ArrayList<EntityNBT>(54);
		for (ItemStack item : getContents()) {
			if (BookOfSouls.isValidBook(item)) {
				passagers.add(BookOfSouls.bookToEntityNBT(item));
			}
		}
		_variable.setPassengers(passagers.toArray(new EntityNBT[passagers.size()]));
		super.inventoryClose(event);
	}

}
