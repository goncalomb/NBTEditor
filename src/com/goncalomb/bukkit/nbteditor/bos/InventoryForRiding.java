package com.goncalomb.bukkit.nbteditor.bos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.betterplugin.Lang;
import com.goncalomb.bukkit.nbteditor.nbt.EntityNBT;

public final class InventoryForRiding extends IInventoryForBos {
	
	private static HashMap<Integer, ItemStack> _placeholders = new HashMap<Integer, ItemStack>();
	
	static {
		_placeholders.put(53, createPlaceholder(Material.PAPER, Lang._("nbt.bos.riding.pholder"), Lang._("nbt.bos.riding.pholder-lore")));
	}
	
	private BookOfSouls _bos;
	
	public InventoryForRiding(BookOfSouls bos, Player owner) {
		super(owner, 54, Lang._("nbt.bos.riding.title"), _placeholders, true);
		_bos = bos;
		Inventory inv = getInventory();
		int i = 0;
		EntityNBT entityNBT = bos.getEntityNBT();
		while ((entityNBT = entityNBT.getRiding()) != null) {
			EntityNBT riding = entityNBT.clone();
			riding.setRiding((EntityNBT[]) null);
			inv.setItem(i++, (new BookOfSouls(riding)).getBook());
		}
	}
	
	@Override
	protected void inventoryClick(InventoryClickEvent event) {
		super.inventoryClick(event);
		ItemStack item = event.getCurrentItem();
		if (item != null && item.getType() != Material.AIR) {
			Player player = (Player) event.getWhoClicked();
			if (item.equals(_bos.getBook())) {
				event.setCancelled(true);
			} else if (!BookOfSouls.isValidBook(item)) {
				event.setCancelled(true);
				player.sendMessage(Lang._("nbt.bos.riding.only-bos"));
			} else {
				EntityNBT entityNbt = BookOfSouls.toEntityNBT(item);
				if (entityNbt == null) {
					player.sendMessage(Lang._("nbt.bos.corrupted"));
					event.setCancelled(true);
				} else if (entityNbt.getRiding() != null) {
					player.sendMessage(Lang._("nbt.bos.riding.has-riding"));
					event.setCancelled(true);
				}
			}
		}
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		List<EntityNBT> rides = new ArrayList<EntityNBT>(54);
		ItemStack[] items = getContents();
		for (ItemStack item : items) {
			if (BookOfSouls.isValidBook(item)) {
				rides.add(BookOfSouls.toEntityNBT(item));
			}
		}
		_bos.getEntityNBT().setRiding(rides.toArray(new EntityNBT[rides.size()]));
		_bos.saveBook();
		((Player)event.getPlayer()).sendMessage(Lang._("nbt.bos.riding.done"));
	}
	
}
