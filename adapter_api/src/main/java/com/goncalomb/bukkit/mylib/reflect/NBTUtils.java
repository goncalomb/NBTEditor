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

package com.goncalomb.bukkit.mylib.reflect;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public final class NBTUtils {

	private static NBTUtilsAdapter adapter = null;

	public static void prepareReflection(Class<?> serverClass, Logger logger) throws Exception {
		String packageName = serverClass.getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf('.') + 1);

		Class<?> clazz = Class.forName("com.goncalomb.bukkit.mylib.reflect.NBTUtilsAdapter_" + version);
		adapter = (NBTUtilsAdapter) clazz.getConstructor().newInstance();
		logger.info("Loaded NBTUtils adapter for " + version);
	}

	public static ItemStack itemStackFromNBTData(NBTTagCompound data) {
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
		return adapter.itemStackFromNBTData(data);
	}

	public static NBTTagCompound itemStackToNBTData(ItemStack stack) {
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
		return adapter.itemStackToNBTData(stack);
	}

	public static Entity spawnEntity(NBTTagCompound data, Location location) {
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
		return adapter.spawnEntity(data, location);
	}

	public static NBTTagCompound getEntityNBTData(Entity entity) {
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
		return adapter.getEntityNBTData(entity);
	}

	public static NBTTagList potionToNBTEffectsList(ItemStack potion) {
		NBTTagCompound tag = getItemStackTag(potion);
		if (tag.hasKey("CustomPotionEffects")) {
			return tag.getList("CustomPotionEffects").clone();
		}
		// Fallback to default potion effect.

		// XXX: implement fallback

		// Finding the default potion effect is not trivial on 1.9.
		// Wait until org.bukkit.craftbukkit.potion.CraftPotionUtil is available upstream.
		// For now, display some alert messages on InventoryForMobs and InventoryForThrownPotion.
		return new NBTTagList();
	}

	public static ItemStack potionFromNBTEffectsList(NBTTagList effects) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setList("CustomPotionEffects", effects.clone());
		tag.setString("Potion", "minecraft:mundane");
		NBTTagCompound data = new NBTTagCompound();
		data.setString("id", "minecraft:potion");
		data.setByte("Count", (byte) 1);
		data.setShort("Damage", (short) 0);
		data.setCompound("tag", tag);
		return itemStackFromNBTData(data);
	}

	public static NBTTagCompound getTileEntityNBTData(Block block) {
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
		return adapter.getTileEntityNBTData(block);
	}

	public static void setTileEntityNBTData(Block block, NBTTagCompound data) {
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
		adapter.setTileEntityNBTData(block, data);
	}

	public static NBTTagCompound getItemStackTag(ItemStack item) {
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
		return adapter.getItemStackTag(item);
	}

	public static void setItemStackTag(ItemStack item, NBTTagCompound tag) {
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
		adapter.setItemStackTag(item, tag);
	}

	public static ItemStack itemStackToCraftItemStack(ItemStack item) {
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
		return adapter.itemStackToCraftItemStack(item);
	}
}
