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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public final class NBTUtils {

	// Minecraft's ItemStack Class;
	private static Constructor<?> _ItemStack_nbtConstructor;
	private static Method _ItemStack_save;
	private static Method _ItemStack_getTag;
	private static Method _ItemStack_setTag;

	// CraftItemStack Class;
	private static Method _CraftItemStack_asCraftMirror;
	private static Method _CraftItemStack_asCraftCopy;
	private static Field _CraftItemStack_handle;

	// Minecraft's Entity Class;
	private static Method _Entity_save;
	private static Method _Entity_getBukkitEntity;

	// CraftEntity Class
	private static Method _CraftEntity_getHandle;

	// Minecraft's TileEntity
	private static Method _TileEntity_load; // Load data from NBTTagCompound.
	private static Method _TileEntity_save; // Save data to NBTTagCompound.

	// CraftWorld
	private static Method _CraftWorld_getHandle;

	// Minecraft's BlockPosition
	private static Constructor<?> _BlockPosition_constructor;

	// Minecraft's World
	private static Method _World_getTileEntity;

	// Minecraft's ChunkRegionLoader Class
	private static Method _ChunkRegionLoader_a; // Spawn an entity from a NBTCompound.

	static void prepareReflection() throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		Class<?> nbtTagCompoundClass = BukkitReflect.getMinecraftClass("NBTTagCompound");

		Class<?> minecraftItemStackClass = BukkitReflect.getMinecraftClass("ItemStack");
		_ItemStack_nbtConstructor = minecraftItemStackClass.getDeclaredConstructor(nbtTagCompoundClass);
		_ItemStack_nbtConstructor.setAccessible(true);
		_ItemStack_save = minecraftItemStackClass.getMethod("save", nbtTagCompoundClass);
		_ItemStack_getTag = minecraftItemStackClass.getMethod("getTag");
		_ItemStack_setTag = minecraftItemStackClass.getMethod("setTag", nbtTagCompoundClass);

		Class<?> craftItemStackClass = BukkitReflect.getCraftBukkitClass("inventory.CraftItemStack");
		_CraftItemStack_asCraftMirror = craftItemStackClass.getMethod("asCraftMirror", minecraftItemStackClass);
		_CraftItemStack_asCraftCopy = craftItemStackClass.getMethod("asCraftCopy", ItemStack.class);
		_CraftItemStack_handle = craftItemStackClass.getDeclaredField("handle");
		_CraftItemStack_handle.setAccessible(true);

		Class<?> minecraftEntityClass = BukkitReflect.getMinecraftClass("Entity");
		_Entity_save = minecraftEntityClass.getMethod("save", nbtTagCompoundClass);
		_Entity_getBukkitEntity = minecraftEntityClass.getMethod("getBukkitEntity");

		Class<?> craftEntityClass = BukkitReflect.getCraftBukkitClass("entity.CraftEntity");
		_CraftEntity_getHandle = craftEntityClass.getMethod("getHandle");

		Class<?> minecraftTileEntityClass = BukkitReflect.getMinecraftClass("TileEntity");
		_TileEntity_save = minecraftTileEntityClass.getMethod("save", nbtTagCompoundClass);
		try {
			// Bukkit 1.12.1+
			_TileEntity_load = minecraftTileEntityClass.getMethod("load", nbtTagCompoundClass);
		} catch (NoSuchMethodException e) {
			// Bukkit 1.12
			// XXX: remove fallback on next version
			_TileEntity_load = minecraftTileEntityClass.getMethod("a", nbtTagCompoundClass);
		}

		Class<?> craftWorldClass = BukkitReflect.getCraftBukkitClass("CraftWorld");
		_CraftWorld_getHandle = craftWorldClass.getMethod("getHandle");

		Class<?> minecraftBlockPositionClass = BukkitReflect.getMinecraftClass("BlockPosition");
		_BlockPosition_constructor = minecraftBlockPositionClass.getConstructor(int.class, int.class, int.class);

		Class<?> minecraftWorldClass = BukkitReflect.getMinecraftClass("World");
		_World_getTileEntity = minecraftWorldClass.getMethod("getTileEntity", minecraftBlockPositionClass);

		Class<?> minecraftChunkRegionLoaderClass = BukkitReflect.getMinecraftClass("ChunkRegionLoader");
		_ChunkRegionLoader_a = minecraftChunkRegionLoaderClass.getMethod("a", nbtTagCompoundClass, minecraftWorldClass, double.class, double.class, double.class, boolean.class);
	}

	private NBTUtils() { }

	public static ItemStack itemStackFromNBTData(NBTTagCompound data) {
		return (ItemStack) BukkitReflect.invokeMethod(null, _CraftItemStack_asCraftMirror, BukkitReflect.invokeConstuctor(_ItemStack_nbtConstructor, data._handle));
	}

	public static NBTTagCompound itemStackToNBTData(ItemStack stack) {
		NBTTagCompound data = new NBTTagCompound();
		Object handle = BukkitReflect.getFieldValue(stack, _CraftItemStack_handle);
		BukkitReflect.invokeMethod(handle, _ItemStack_save, data._handle);
		return data;
	}

	public static Entity spawnEntity(NBTTagCompound data, Location location) {
		Object worldHandle = BukkitReflect.invokeMethod(location.getWorld(), _CraftWorld_getHandle);
		Object entityHandle = BukkitReflect.invokeMethod(null, _ChunkRegionLoader_a, data._handle, worldHandle, location.getX(), location.getY(), location.getZ(), true);
		if (entityHandle == null) {
			return null;
		}
		return (Entity) BukkitReflect.invokeMethod(entityHandle, _Entity_getBukkitEntity);
	}

	public static NBTTagCompound getEntityNBTData(Entity entity) {
		Object entityHandle = BukkitReflect.invokeMethod(entity, _CraftEntity_getHandle);
		NBTTagCompound data = new NBTTagCompound();
		BukkitReflect.invokeMethod(entityHandle, _Entity_save, data._handle);
		return data;
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
		tag.setString("Potion", "minecraft:empty");
		NBTTagCompound data = new NBTTagCompound();
		data.setString("id", "minecraft:potion");
		data.setByte("Count", (byte) 1);
		data.setShort("Damage", (short) 0);
		data.setCompound("tag", tag);
		return itemStackFromNBTData(data);
	}

	private static Object getTileEntity(Block block) {
		Object worldHandle = BukkitReflect.invokeMethod(block.getWorld(), _CraftWorld_getHandle);
		Object pos = BukkitReflect.invokeConstuctor(_BlockPosition_constructor, block.getX(), block.getY(), block.getZ());
		return BukkitReflect.invokeMethod(worldHandle, _World_getTileEntity, pos);
	}

	public static NBTTagCompound getTileEntityNBTData(Block block) {
		NBTBase.prepareReflection();
		Object tileEntity = getTileEntity(block);
		if (tileEntity != null) {
			NBTTagCompound data = new NBTTagCompound();
			BukkitReflect.invokeMethod(tileEntity, _TileEntity_save, data._handle);
			return data;
		}
		return null;
	}

	public static void setTileEntityNBTData(Block block, NBTTagCompound data) {
		NBTBase.prepareReflection();
		Object tileEntity = getTileEntity(block);
		if (tileEntity != null) {
			BukkitReflect.invokeMethod(tileEntity, _TileEntity_load, data._handle);
		}
	}

	public static NBTTagCompound getItemStackTag(ItemStack item) {
		Object handle = BukkitReflect.getFieldValue(item, _CraftItemStack_handle);
		Object tag = BukkitReflect.invokeMethod(handle, _ItemStack_getTag);
		return (tag == null ? new NBTTagCompound() : new NBTTagCompound(tag));
	}

	public static void setItemStackTag(ItemStack item, NBTTagCompound tag) {
		Object handle = BukkitReflect.getFieldValue(item, _CraftItemStack_handle);
		BukkitReflect.invokeMethod(handle, _ItemStack_setTag, tag._handle);
	}

	public static ItemStack itemStackToCraftItemStack(ItemStack item) {
		if (!_CraftItemStack_asCraftCopy.getClass().isInstance(item)) {
			return (ItemStack) BukkitReflect.invokeMethod(null, _CraftItemStack_asCraftCopy, item);
		}
		return item;
	}

}
