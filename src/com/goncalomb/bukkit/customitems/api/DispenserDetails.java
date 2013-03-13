package com.goncalomb.bukkit.customitems.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dispenser;
import org.bukkit.plugin.Plugin;

import com.goncalomb.bukkit.UtilsMc;

public final class DispenserDetails extends ItemDetails implements IConsumableDetails {
	
	private Plugin _plugin;
	private Block _block;
	private Location _location;
	
	DispenserDetails(BlockDispenseEvent event, Plugin plugin) {
		super(event.getItem());
		_block = event.getBlock();
		_plugin = plugin;
	}
	
	public Location getLocation() {
		if (_location == null) {
			BlockFace face = ((Dispenser) _block.getState().getData()).getFacing();
			_location = _block.getLocation().add(UtilsMc.faceToDelta(face, 0.2)).add(0, -0.3, 0);
		}
		return _location;
	}
	
	@Override
	public void consumeItem() {
		org.bukkit.block.Dispenser disp = (org.bukkit.block.Dispenser) _block.getState();
		final Inventory inv = disp.getInventory();
		Bukkit.getScheduler().runTask(_plugin, new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < inv.getSize(); ++i) {
					ItemStack item = inv.getItem(i);
					if (item != null && item.isSimilar(_item)) {
						if (item.getAmount() > 1) {
							item.setAmount(item.getAmount() - 1);
						} else {
							inv.clear(i);
						}
						return;
					}
				}
			}
		});
	}
	
}
