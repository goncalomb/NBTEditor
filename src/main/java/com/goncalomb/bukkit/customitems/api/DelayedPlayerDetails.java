package com.goncalomb.bukkit.customitems.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DelayedPlayerDetails extends PlayerDetails {
	
	private boolean _locked = false;
	Object _userObject;
	
	DelayedPlayerDetails(ItemStack item, Player player) {
		super(item, player);
	}
	
	void lock() {
		_locked = true;
	}
	
	@Override
	public final void consumeItem() {
		if (!_locked && _player != null) {
			super.consumeItem();
		}
	}
	
	public final void setUserObject(Object object) {
		_userObject = object;
	}
	
	public final Object getUserObject() {
		return _userObject;
	}
	
}
