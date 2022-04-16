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

	static void prepareReflection(Class<?> serverClass, Logger logger) throws Exception {
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
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
		return adapter.potionToNBTEffectsList(potion);
	}

	public static ItemStack potionFromNBTEffectsList(NBTTagList effects) {
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
		return adapter.potionFromNBTEffectsList(effects);
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
