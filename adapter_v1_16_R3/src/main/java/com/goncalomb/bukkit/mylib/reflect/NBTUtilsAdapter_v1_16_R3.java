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
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public final class NBTUtilsAdapter_v1_16_R3 implements NBTUtilsAdapter {

	// Minecraft's ItemStack Class;
	private Constructor<?> _ItemStack_nbtConstructor;
	private Method _ItemStack_save;
	private Method _ItemStack_getTag;
	private Method _ItemStack_setTag;

	// CraftItemStack Class;
	private Method _CraftItemStack_asCraftMirror;
	private Method _CraftItemStack_asCraftCopy;
	private Field _CraftItemStack_handle;

	// Minecraft's Entity Class;
	private Method _Entity_save;
	private Method _Entity_getBukkitEntity;
	private Method _Entity_setPosition;

	// CraftEntity Class
	private Method _CraftEntity_getHandle;

	// Minecraft's TileEntity
	private Method _TileEntity_load; // Load data from NBTTagCompound.
	private Method _TileEntity_save; // Save data to NBTTagCompound.

	// CraftWorld
	private Method _CraftWorld_getHandle;

	// Minecraft's BlockPosition
	private Constructor<?> _BlockPosition_constructor;

	// Minecraft's World
	private Method _World_getTileEntity;
	private Method _WorldServer_addAllEntitiesSafely;

	// Minecraft's EntityTypes Class
	private Method _EntityTypes_a; // Spawn an entity from a NBTCompound.

	public NBTUtilsAdapter_v1_16_R3() throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		Class<?> nbtTagCompoundClass = BukkitReflectAdapter_v1_16_R3.getMinecraftClass("NBTTagCompound");

		Class<?> minecraftItemStackClass = BukkitReflectAdapter_v1_16_R3.getMinecraftClass("ItemStack");
		_ItemStack_nbtConstructor = minecraftItemStackClass.getDeclaredConstructor(nbtTagCompoundClass);
		_ItemStack_nbtConstructor.setAccessible(true);
		_ItemStack_save = minecraftItemStackClass.getMethod("save", nbtTagCompoundClass);
		_ItemStack_getTag = minecraftItemStackClass.getMethod("getTag");
		_ItemStack_setTag = minecraftItemStackClass.getMethod("setTag", nbtTagCompoundClass);

		Class<?> craftItemStackClass = BukkitReflectAdapter_v1_16_R3.getCraftBukkitClass("inventory.CraftItemStack");
		_CraftItemStack_asCraftMirror = craftItemStackClass.getMethod("asCraftMirror", minecraftItemStackClass);
		_CraftItemStack_asCraftCopy = craftItemStackClass.getMethod("asCraftCopy", ItemStack.class);
		_CraftItemStack_handle = craftItemStackClass.getDeclaredField("handle");
		_CraftItemStack_handle.setAccessible(true);

		Class<?> minecraftEntityClass = BukkitReflectAdapter_v1_16_R3.getMinecraftClass("Entity");
		_Entity_save = minecraftEntityClass.getMethod("save", nbtTagCompoundClass);
		_Entity_getBukkitEntity = minecraftEntityClass.getMethod("getBukkitEntity");
		_Entity_setPosition = minecraftEntityClass.getMethod("setPosition", double.class, double.class, double.class);

		Class<?> craftEntityClass = BukkitReflectAdapter_v1_16_R3.getCraftBukkitClass("entity.CraftEntity");
		_CraftEntity_getHandle = craftEntityClass.getMethod("getHandle");

		Class<?> minecraftTileEntityClass = BukkitReflectAdapter_v1_16_R3.getMinecraftClass("TileEntity");
		_TileEntity_save = minecraftTileEntityClass.getMethod("save", nbtTagCompoundClass);
		Class<?> minecraftIBlockDataClass = BukkitReflectAdapter_v1_16_R3.getMinecraftClass("IBlockData");
		_TileEntity_load = minecraftTileEntityClass.getMethod("load", minecraftIBlockDataClass, nbtTagCompoundClass);

		Class<?> craftWorldClass = BukkitReflectAdapter_v1_16_R3.getCraftBukkitClass("CraftWorld");
		_CraftWorld_getHandle = craftWorldClass.getMethod("getHandle");

		Class<?> minecraftBlockPositionClass = BukkitReflectAdapter_v1_16_R3.getMinecraftClass("BlockPosition");
		_BlockPosition_constructor = minecraftBlockPositionClass.getConstructor(int.class, int.class, int.class);

		Class<?> minecraftWorldClass = BukkitReflectAdapter_v1_16_R3.getMinecraftClass("World");
		_World_getTileEntity = minecraftWorldClass.getMethod("getTileEntity", minecraftBlockPositionClass);

		Class<?> minecraftWorldServerClass = BukkitReflectAdapter_v1_16_R3.getMinecraftClass("WorldServer");
		_WorldServer_addAllEntitiesSafely = minecraftWorldServerClass.getMethod("addAllEntitiesSafely", minecraftEntityClass);

		Class<?> minecraftEntityTypesClass = BukkitReflectAdapter_v1_16_R3.getMinecraftClass("EntityTypes");
		_EntityTypes_a = minecraftEntityTypesClass.getMethod("a", nbtTagCompoundClass, minecraftWorldClass, Function.class);
	}

	public ItemStack itemStackFromNBTData(NBTTagCompound data) {
		return (ItemStack) BukkitReflectAdapter_v1_16_R3.invokeMethod(null, _CraftItemStack_asCraftMirror, BukkitReflectAdapter_v1_16_R3.invokeConstuctor(_ItemStack_nbtConstructor, data._handle));
	}

	public NBTTagCompound itemStackToNBTData(ItemStack stack) {
		NBTTagCompound data = new NBTTagCompound();
		Object handle = BukkitReflectAdapter_v1_16_R3.getFieldValue(stack, _CraftItemStack_handle);
		BukkitReflectAdapter_v1_16_R3.invokeMethod(handle, _ItemStack_save, data._handle);
		return data;
	}

	public Entity spawnEntity(NBTTagCompound data, Location location) {
		Object worldHandle = BukkitReflectAdapter_v1_16_R3.invokeMethod(location.getWorld(), _CraftWorld_getHandle);
		// This function will be applied to each summoned entity (including passengers) to set their location
		Function<Object, Object> entityFunction = (Object entity) -> {
			BukkitReflectAdapter_v1_16_R3.invokeMethod(entity, _Entity_setPosition, location.getX(), location.getY(), location.getZ());
			return entity;
		};
		// Summon the entity, and for each entity summoned (including passengers) run the above function
		Object entityHandle = BukkitReflectAdapter_v1_16_R3.invokeMethod(null, _EntityTypes_a, data._handle, worldHandle, entityFunction);
		if (entityHandle == null) {
			return null;
		}
		BukkitReflectAdapter_v1_16_R3.invokeMethod(worldHandle, _WorldServer_addAllEntitiesSafely, entityHandle);
		return (Entity) BukkitReflectAdapter_v1_16_R3.invokeMethod(entityHandle, _Entity_getBukkitEntity);
	}

	public NBTTagCompound getEntityNBTData(Entity entity) {
		Object entityHandle = BukkitReflectAdapter_v1_16_R3.invokeMethod(entity, _CraftEntity_getHandle);
		NBTTagCompound data = new NBTTagCompound();
		BukkitReflectAdapter_v1_16_R3.invokeMethod(entityHandle, _Entity_save, data._handle);
		return data;
	}

	private Object getTileEntity(Block block) {
		Object worldHandle = BukkitReflectAdapter_v1_16_R3.invokeMethod(block.getWorld(), _CraftWorld_getHandle);
		Object pos = BukkitReflectAdapter_v1_16_R3.invokeConstuctor(_BlockPosition_constructor, block.getX(), block.getY(), block.getZ());
		return BukkitReflectAdapter_v1_16_R3.invokeMethod(worldHandle, _World_getTileEntity, pos);
	}

	public NBTTagCompound getTileEntityNBTData(Block block) {
		Object tileEntity = getTileEntity(block);
		if (tileEntity != null) {
			NBTTagCompound data = new NBTTagCompound();
			BukkitReflectAdapter_v1_16_R3.invokeMethod(tileEntity, _TileEntity_save, data._handle);
			return data;
		}
		return null;
	}

	public void setTileEntityNBTData(Block block, NBTTagCompound data) {
		Object tileEntity = getTileEntity(block);
		if (tileEntity != null) {
			BukkitReflectAdapter_v1_16_R3.invokeMethod(tileEntity, _TileEntity_load, null, data._handle);
		}
	}

	public NBTTagCompound getItemStackTag(ItemStack item) {
		Object handle = BukkitReflectAdapter_v1_16_R3.getFieldValue(item, _CraftItemStack_handle);
		Object tag = BukkitReflectAdapter_v1_16_R3.invokeMethod(handle, _ItemStack_getTag);
		return (tag == null ? new NBTTagCompound() : new NBTTagCompound(tag));
	}

	public void setItemStackTag(ItemStack item, NBTTagCompound tag) {
		Object handle = BukkitReflectAdapter_v1_16_R3.getFieldValue(item, _CraftItemStack_handle);
		BukkitReflectAdapter_v1_16_R3.invokeMethod(handle, _ItemStack_setTag, tag._handle);
	}

	public ItemStack itemStackToCraftItemStack(ItemStack item) {
		if (!_CraftItemStack_asCraftCopy.getClass().isInstance(item)) {
			return (ItemStack) BukkitReflectAdapter_v1_16_R3.invokeMethod(null, _CraftItemStack_asCraftCopy, item);
		}
		return item;
	}
}
