package com.goncalomb.bukkit.customitems.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;

import com.goncalomb.bukkit.customitems.api.PlayerDetails;
import com.goncalomb.bukkit.lang.Lang;

public final class BatBomb extends GenericBomb {
	
	private float _power;
	
	public BatBomb() {
		super("bat-bomb", ChatColor.RED + "Bat Bomb", new MaterialData(Material.MONSTER_EGG, (byte)65), false);
		setLore(Lang._list("citems.item-lores.bat-bomb"));
		setDefaultConfig("fuse", 50);
		setDefaultConfig("power", 3.8d);
	}
	
	@Override
	public void applyConfig(ConfigurationSection section) {
		super.applyConfig(section);
		_power = (float) section.getDouble("power");
	}
	
	@Override
	public void onRightClick(PlayerInteractEvent event, PlayerDetails details) {
		details.consumeItem();
		spawnBatsAt(event.getPlayer().getEyeLocation(), 10, 100);
	}
	
	@Override
	public void onTrigger(Item item) {
		Entity bat = item.getWorld().spawnEntity(item.getLocation(), EntityType.BAT);
		item.setPassenger(bat);
	}
	
	@Override
	public void onExplode(Item item, Location location) {
		Entity bat = item.getPassenger();
		if (bat != null && !bat.isDead()) {
			bat.remove();
			spawnBatsAt(location, 10, 50);
		}
	}
	
	private void spawnBatsAt(final Location loc, final int count, int fuse) {
		final World world = loc.getWorld();
		final Entity[] bats = new Entity[count];
		for (int i = 0; i < count; ++i) {
			bats[i] = world.spawnEntity(loc, EntityType.BAT);
		}
		Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
			@Override
			public void run() {
				for (Entity e : bats) {
					if (!e.isDead()) {
						Location loc = e.getLocation();
						world.createExplosion(loc.getX(), loc.getY(), loc.getZ(), _power, false, false);
						e.remove();
					}
				}
			}
		}, fuse);
	}
	
}
