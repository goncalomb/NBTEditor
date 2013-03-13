package com.goncalomb.bukkit.customitems.api;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class PlayerInventoryDetails extends PlayerDetails {
	
	private int _slot;
	
	PlayerInventoryDetails(ItemStack item, Player player, int slot) {
		super(item, player);
		_slot = slot;
	}
	
	public boolean isArmor() {
		return (_slot >= _player.getInventory().getSize());
	}
	
	@Override
	public void consumeItem() {
		if (_player.getGameMode() == GameMode.CREATIVE) return;
		if (_item.getAmount() > 1) {
			_item.setAmount(_item.getAmount() - 1);
		} else /*if (!isArmor())*/ {
			_player.getInventory().clear(_slot);
		}/* else {
			PlayerInventory inv = _player.getInventory();
			int slotDiff = _slot - _player.getInventory().getSize();
			switch (slotDiff) {
			case 0: inv.setBoots(null); break;
			case 1: inv.setLeggings(null); break;
			case 2: inv.setChestplate(null); break;
			case 3: inv.setHelmet(null); break;
			}
		}*/
	}
	
}
