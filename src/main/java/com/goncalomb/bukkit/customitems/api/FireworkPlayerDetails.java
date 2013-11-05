package com.goncalomb.bukkit.customitems.api;

import org.bukkit.entity.Firework;

public final class FireworkPlayerDetails extends DelayedPlayerDetails {
	
	private Firework _firework;
	
	static FireworkPlayerDetails fromConsumableDetails(IConsumableDetails details, Firework firework, Object userObject) {
		if (details instanceof PlayerDetails) {
			return new FireworkPlayerDetails((PlayerDetails) details, firework, userObject);
		}
		return new FireworkPlayerDetails(details, firework, userObject);
	}
	
	private FireworkPlayerDetails(IConsumableDetails details, Firework firework, Object userObject) {
		super(details.getItem(), null);
		_firework = firework;
		_userObject = userObject;
	}
	
	private FireworkPlayerDetails(PlayerDetails details, Firework firework, Object userObject) {
		super(details._item, details._player);
		_firework = firework;
		_userObject = userObject;
	}
	
	public Firework getFirework() {
		return _firework;
	}

}
