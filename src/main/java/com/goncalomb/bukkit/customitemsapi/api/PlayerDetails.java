/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
 *
 * This file is part of NBTEditor.
 *
 * NBTEditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NBTEditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NBTEditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.customitemsapi.api;

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
