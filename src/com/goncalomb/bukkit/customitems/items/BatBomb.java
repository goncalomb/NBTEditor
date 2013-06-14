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
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;

import com.goncalomb.bukkit.customitems.api.PlayerDetails;

public final class BatBomb extends GenericBomb {
	
	private float _power;
	
	public BatBomb() {
		super("bat-bomb", ChatColor.RED + "Bat Bomb", new MaterialData(Material.MONSTER_EGG, (byte)65));
		setLore("§bLeft-click or drop key to throw the bomb.",
				"§bRight-Click to spawn the bats at your location.",
				"§bThe bats will explode after a few seconds.");
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
	public void onDropperPickup(InventoryPickupItemEvent event) {
		if (event.getItem().hasMetadata("is-active")) {
			event.setCancelled(true);
		}
	}
	
	@Override
	public void onTrigger(Item item) {
		Entity bat = item.getWorld().spawnEntity(item.getLocation(), EntityType.BAT);
		item.setMetadata("is-active", new FixedMetadataValue(getPlugin(), true));
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
