package com.goncalomb.bukkit.customitems.api;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDetails extends ItemDetails implements IConsumableDetails {
	
	protected Player _player;
	
	PlayerDetails(BlockBreakEvent event) {
		super(event.getPlayer().getItemInHand());
		_player = event.getPlayer();
	}
	
	PlayerDetails(PlayerInteractEvent event) {
		super(event.getItem());
		_player = event.getPlayer();
	}
	
	PlayerDetails(ItemStack item, Player player) {
		super(item);
		_player = player;
	}
	
	public final Player getPlayer() {
		return _player;
	}
	
	@Override
	public void consumeItem() {
		if (_player.getGameMode() == GameMode.CREATIVE) return;
		if (_item.getAmount() > 1) {
			_item.setAmount(_item.getAmount() - 1);
		} else {
			_player.setItemInHand(null);
		}
	}
	
}
