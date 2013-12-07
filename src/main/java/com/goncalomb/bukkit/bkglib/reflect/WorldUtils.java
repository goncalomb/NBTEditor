/*
 * Copyright (C) 2013 - Gonçalo Baltazar <http://goncalomb.com>
 *
 * This file is part of BKgLib.
 *
 * BKgLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BKgLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BKgLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.bkglib.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.ThrownPotion;

public final class WorldUtils {
	
	private static boolean _isPrepared = false;
	
	// CraftWorld Class
	private static Method _getHandle;
	
	// Minecraft's Entity Class
	private static Method _getBukkitEntity;
	private static Method _setPositionRotation;
	
	// Minecraft's World Class
	private static Method _addEntity;
	
	// Minecraft's EntityExperienceOrb Class
	private static Constructor<?> _xpOrbConstructor;
	
	// Minecraft's EntityPotion Class
	private static Constructor<?> _potionConstructor;
	
	// Minecraft's EntityEnderPearl Class
	private static Constructor<?> _enderPearlConstructor;
	
	public static void prepareReflection() {
		if (!_isPrepared) {
			try {
				Class<?> craftWorldClass = BukkitReflect.getCraftBukkitClass("CraftWorld");
				_getHandle = craftWorldClass.getMethod("getHandle");
				
				Class<?> minecraftEntityClass = BukkitReflect.getMinecraftClass("Entity");
				_getBukkitEntity = minecraftEntityClass.getMethod("getBukkitEntity");
				_setPositionRotation = minecraftEntityClass.getMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class);
				
				Class<?> minecraftWorldClass = BukkitReflect.getMinecraftClass("World");
				_addEntity = minecraftWorldClass.getMethod("addEntity", minecraftEntityClass);
				
				Class<?> minecraftEntityExperienceOrbClass = BukkitReflect.getMinecraftClass("EntityExperienceOrb");
				_xpOrbConstructor = minecraftEntityExperienceOrbClass.getConstructor(minecraftWorldClass, double.class, double.class, double.class, int.class);
				
				Class<?> minecraftEntityPotionClass = BukkitReflect.getMinecraftClass("EntityPotion");
				_potionConstructor = minecraftEntityPotionClass.getConstructor(minecraftWorldClass, double.class, double.class, double.class, BukkitReflect.getMinecraftClass("ItemStack"));
				
				Class<?> minecraftEntityEnderPearl = BukkitReflect.getMinecraftClass("EntityEnderPearl");
				_enderPearlConstructor = minecraftEntityEnderPearl.getConstructor(minecraftWorldClass);
				
			} catch (Exception e) {
				throw new Error("Error while preparing WorldUtils.", e);
			}
			_isPrepared = true;
		}
	}
	
	private WorldUtils() { }
	
	public static ExperienceOrb spawnXPOrb(Location location, short value) {
		prepareReflection();
		Object world = BukkitReflect.invokeMethod(location.getWorld(), _getHandle);
		Object entity = BukkitReflect.newInstance(_xpOrbConstructor, world, location.getBlockX() + 0.5, location.getBlockY(), location.getZ(), (int) value);
		BukkitReflect.invokeMethod(entity, _setPositionRotation, location.getBlockX() + 0.5, location.getBlockY(), location.getZ(), 0, 0);
		BukkitReflect.invokeMethod(world, _addEntity, entity);
		return (ExperienceOrb) BukkitReflect.invokeMethod(entity, _getBukkitEntity);
	}
	
	public static ThrownPotion spawnPotion(Location location, NBTTagCompoundWrapper data) {
		Object world = BukkitReflect.invokeMethod(location.getWorld(), _getHandle);
		Object entity = BukkitReflect.newInstance(_potionConstructor, world, location.getBlockX() + 0.5, location.getBlockY(), location.getZ(), null);
		NBTUtils.setMineEntityNBTTagCompound(entity, data);
		BukkitReflect.invokeMethod(world, _addEntity, entity);
		return (ThrownPotion) BukkitReflect.invokeMethod(entity, _getBukkitEntity);
	}
	
	public static EnderPearl spawnEnderpearl(Location location, NBTTagCompoundWrapper data) {
		Object world = BukkitReflect.invokeMethod(location.getWorld(), _getHandle);
		Object entity = BukkitReflect.newInstance(_enderPearlConstructor, world);
		BukkitReflect.invokeMethod(entity, _setPositionRotation, location.getBlockX() + 0.5, location.getBlockY(), location.getZ(), 0, 0);
		NBTUtils.setMineEntityNBTTagCompound(entity, data);
		BukkitReflect.invokeMethod(world, _addEntity, entity);
		return (EnderPearl) BukkitReflect.invokeMethod(entity, _getBukkitEntity);
	}
	
}
