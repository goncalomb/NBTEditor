/*
 * Copyright (C) 2013-2018 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.customitems.items;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.goncalomb.bukkit.customitems.api.PlayerDetails;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public final class BatBomb extends GenericBomb {

	private float _power;

	public BatBomb() {
		super("bat-bomb", ChatColor.RED + "Bat Bomb", Material.BAT_SPAWN_EGG);
		setLore("§bLeft-click to throw the bomb.",
				"§bRight-click to spawn the bats at your location.",
				"§bThe bats will explode after a few seconds.");
		setDefaultConfig("fuse", 50);
		setDefaultConfig("power", 3.8d);
	}

	@Override
	protected ItemStack customizeItem(ItemStack item) {
		item = NBTUtils.itemStackToCraftItemStack(item);
		NBTTagCompound tag = NBTUtils.getItemStackTag(item);
		NBTTagCompound entityTag = new NBTTagCompound();
		entityTag.setString("id", "Bat");
		tag.setCompound("EntityTag", entityTag);
		NBTUtils.setItemStackTag(item, tag);
		return item;
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
		item.addPassenger(bat);
	}

	@Override
	public void onExplode(Item item, Location location) {
		List<Entity> passengers = item.getPassengers();
		if (passengers.size() != 0) {
			Entity bat = passengers.get(0);
			if (bat != null && bat instanceof Bat && !bat.isDead()) {
				bat.remove();
				spawnBatsAt(location, 10, 50);
			}
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
