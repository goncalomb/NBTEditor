package com.goncalomb.bukkit.customitems.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.DispenserDetails;
import com.goncalomb.bukkit.customitems.api.PlayerDetails;

public abstract class GenericBomb extends CustomItem {
	
	private int _fuse;
	private boolean _triggerOnDrop;
	
	protected GenericBomb(String slug, String name, MaterialData material) {
		super(slug, name, material);
		setDefaultConfig("fuse", 40);
		setDefaultConfig("trigger-on-drop", false);
	}
	
	protected int getFuse() {
		return _fuse;
	}
	
	@Override
	public void applyConfig(ConfigurationSection section) {
		super.applyConfig(section);
		_fuse = section.getInt("fuse", 40);
		_triggerOnDrop = section.getBoolean("trigger-on-drop", false);
	}
	
	@Override
	public final void onLeftClick(PlayerInteractEvent event, PlayerDetails details) {
		details.consumeItem();
		Player player = event.getPlayer();
		Location loc = player.getEyeLocation();
		trigger(createItem(loc, loc.getDirection()));
		event.setCancelled(true);
	}
	
	@Override
	public final void onDrop(PlayerDropItemEvent event) {
		if (_triggerOnDrop) {
			trigger(event.getItemDrop());
		}
	}
	
	@Override
	public void onDispense(BlockDispenseEvent event, DispenserDetails details) {
		event.setCancelled(true);
		details.consumeItem();
		trigger(createItem(details.getLocation(), event.getVelocity()));
	}
	
	private Item createItem(Location loc, Vector vel) {
		Item item = loc.getWorld().dropItem(loc, getItem());
		item.setVelocity(vel);
		item.setPickupDelay(Integer.MAX_VALUE);
		return item;
	}
	
	private void trigger(final Item item) {
		item.setPickupDelay(Integer.MAX_VALUE);
		onTrigger(item);
		Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
			@Override
			public void run() {
				if (!item.isDead()) {
					onExplode(item, item.getLocation());
					item.remove();
				}
			}
		}, _fuse);
	}
	
	public void onTrigger(Item item) { }
	
	public abstract void onExplode(Item item, Location location);
	
}
