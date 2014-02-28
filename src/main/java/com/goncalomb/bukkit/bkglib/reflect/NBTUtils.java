/*
 * Copyright (C) 2013, 2014 - Gonçalo Baltazar <http://goncalomb.com>
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

public final class NBTUtils {
	
	// Minecraft's ItemStack Class;
	private static Method _ItemStack_createStack;
	private static Method _ItemStack_save;
	private static Method _ItemStack_getTag;
	private static Method _ItemStack_setTag;
	
	// CraftItemStack Class;
	private static Method _CraftItemStack_asCraftMirror;
	private static Field _CraftItemStack_handle;
	
	// Minecraft's Entity Class;
	private static Method _Entity_e; // Save data to NBTTagCompound.
	private static Method _Entity_mount;
	private static Method _Entity_getBukkitEntity;
	private static Method _Entity_setPositionRotation;
	private static Field _Entity_pitch;
	private static Field _Entity_yaw;
	
	// CraftEntity Class
	private static Method _CraftEntity_getHandle;
	
	// Minecraft's TileEntity
	private static Method _TileEntity_a; // Load data from NBTTagCompound.
	private static Method _TileEntity_b; // Save data to NBTTagCompound.
	
	// CraftWorld
	private static Method _CraftWorld_getHandle;
	private static Method _CraftWorld_getTileEntityAt;
	
	// Minecraft's World Class
	private static Method _World_addEntity;
	
	// Minecraft's EntityTypes Class
	private static Method _EntityTypes_a; // Creates an entity from a NBTCompound.
	
	static void prepareReflection() throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		Class<?> nbtTagCompoundClass = BukkitReflect.getMinecraftClass("NBTTagCompound");
		
		Class<?> minecraftItemStackClass = BukkitReflect.getMinecraftClass("ItemStack");
		_ItemStack_createStack = minecraftItemStackClass.getMethod("createStack", nbtTagCompoundClass);
		_ItemStack_save = minecraftItemStackClass.getMethod("save", nbtTagCompoundClass);
		_ItemStack_getTag = minecraftItemStackClass.getMethod("getTag");
		_ItemStack_setTag = minecraftItemStackClass.getMethod("setTag", nbtTagCompoundClass);
		
		Class<?> craftItemStackClass = BukkitReflect.getCraftBukkitClass("inventory.CraftItemStack");
		_CraftItemStack_asCraftMirror = craftItemStackClass.getMethod("asCraftMirror", minecraftItemStackClass);
		_CraftItemStack_handle = craftItemStackClass.getDeclaredField("handle");
		_CraftItemStack_handle.setAccessible(true);
		
		Class<?> minecraftEntityClass = BukkitReflect.getMinecraftClass("Entity");
		_Entity_e = minecraftEntityClass.getMethod("e", nbtTagCompoundClass);
		_Entity_setPositionRotation = minecraftEntityClass.getMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class);
		_Entity_yaw = minecraftEntityClass.getField("yaw");
		_Entity_pitch = minecraftEntityClass.getField("pitch");
		_Entity_mount = minecraftEntityClass.getMethod("mount", minecraftEntityClass);
		_Entity_getBukkitEntity = minecraftEntityClass.getMethod("getBukkitEntity");
		
		Class<?> craftEntityClass = BukkitReflect.getCraftBukkitClass("entity.CraftEntity");
		_CraftEntity_getHandle = craftEntityClass.getMethod("getHandle");
		
		Class<?> minecraftTileEntityClass = BukkitReflect.getMinecraftClass("TileEntity");
		_TileEntity_b = minecraftTileEntityClass.getMethod("b", nbtTagCompoundClass);
		_TileEntity_a = minecraftTileEntityClass.getMethod("a", nbtTagCompoundClass);
		
		Class<?> craftWorldClass = BukkitReflect.getCraftBukkitClass("CraftWorld");
		_CraftWorld_getHandle = craftWorldClass.getMethod("getHandle");
		_CraftWorld_getTileEntityAt = craftWorldClass.getMethod("getTileEntityAt", int.class, int.class, int.class);
		
		Class<?> minecraftWorldClass = BukkitReflect.getMinecraftClass("World");
		_World_addEntity = minecraftWorldClass.getMethod("addEntity", minecraftEntityClass);
		
		Class<?> minecraftEntityTypesClass = BukkitReflect.getMinecraftClass("EntityTypes");
		_EntityTypes_a = minecraftEntityTypesClass.getMethod("a", nbtTagCompoundClass, minecraftWorldClass);
	}
	
	private NBTUtils() { }
	
	public static ItemStack itemStackFromNBTData(NBTTagCompound data) {
		return (ItemStack) BukkitReflect.invokeMethod(null, _CraftItemStack_asCraftMirror, BukkitReflect.invokeMethod(null, _ItemStack_createStack, data._handle));
	}
	
	public static NBTTagCompound itemStackToNBTData(ItemStack stack) {
		NBTTagCompound data = new NBTTagCompound();
		Object handle = BukkitReflect.getFieldValue(stack, _CraftItemStack_handle);
		BukkitReflect.invokeMethod(handle, _ItemStack_save, data._handle);
		return data;
	}
	
	public static Entity spawnEntity(NBTTagCompound data, Location location) {
		Object worldHandle = BukkitReflect.invokeMethod(location.getWorld(), _CraftWorld_getHandle);
		Object prevEntityHandle = null;
		do {
			Object entityHandle = BukkitReflect.invokeMethod(null, _EntityTypes_a, data._handle, worldHandle);
			if (entityHandle != null) {
				float yaw = (Float) BukkitReflect.getFieldValue(entityHandle, _Entity_yaw);
				float pitch = (Float) BukkitReflect.getFieldValue(entityHandle, _Entity_pitch);
				BukkitReflect.invokeMethod(entityHandle, _Entity_setPositionRotation, location.getX(), location.getY(), location.getZ(), yaw, pitch);
				BukkitReflect.invokeMethod(worldHandle, _World_addEntity, entityHandle);
				if (prevEntityHandle != null) {
					BukkitReflect.invokeMethod(prevEntityHandle, _Entity_mount, entityHandle);
				}
				prevEntityHandle = entityHandle;
			} else {
				break;
			}
			data = data.getCompound("Riding");
		} while (data != null);
		return (Entity) BukkitReflect.invokeMethod(prevEntityHandle, _Entity_getBukkitEntity);
	}
	
	public static NBTTagCompound getEntityNBTData(Entity entity) {
		Object entityHandle = BukkitReflect.invokeMethod(entity, _CraftEntity_getHandle);
		NBTTagCompound data = new NBTTagCompound();
		BukkitReflect.invokeMethod(entityHandle, _Entity_e, data._handle);
		return data;
	}
	
	public static NBTTagList potionToNBTEffectsList(ItemStack potion) {
		NBTTagCompound tag = getItemStackTag(potion);
		if (tag.hasKey("CustomPotionEffects")) {
			return tag.getList("CustomPotionEffects").clone();
		}
		// Fallback to default potion effect.
		Collection<PotionEffect> effects = Potion.fromItemStack(potion).getEffects();
		NBTTagList effectList = new NBTTagList();
		for (PotionEffect effect : effects) {
			NBTTagCompound effectTag = new NBTTagCompound();
			effectTag.setByte("Id", (byte)effect.getType().getId());
			effectTag.setByte("Amplifier", (byte)effect.getAmplifier());
			effectTag.setInt("Duration", effect.getDuration());
			effectList.add(effectTag);
		}
		return effectList;
	}
	
	public static ItemStack potionFromNBTEffectsList(NBTTagList effects) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setList("CustomPotionEffects", effects.clone());
		NBTTagCompound data = new NBTTagCompound();
		data.setShort("id", (short) Material.POTION.getId());
		data.setByte("Count", (byte) 1);
		data.setShort("Damage", (short) (new Potion(PotionType.SPEED, 1)).toDamageValue());
		data.setCompound("tag", tag);
		return itemStackFromNBTData(data);
	}
	
	private static Object getTileEntity(Block block) {
		return BukkitReflect.invokeMethod(block.getWorld(), _CraftWorld_getTileEntityAt, block.getX(), block.getY(), block.getZ());
	}
	
	public static NBTTagCompound getTileEntityNBTData(Block block) {
		NBTBase.prepareReflection();
		Object tileEntity = getTileEntity(block);
		if (tileEntity != null) {
			NBTTagCompound data = new NBTTagCompound();
			BukkitReflect.invokeMethod(tileEntity, _TileEntity_b, data._handle);
			return data;
		}
		return null;
	}
	
	public static void setTileEntityNBTData(Block block, NBTTagCompound data) {
		NBTBase.prepareReflection();
		Object tileEntity = getTileEntity(block);
		if (tileEntity != null) {
			BukkitReflect.invokeMethod(tileEntity, _TileEntity_a, data._handle);
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
	
	public static void setItemStackFakeEnchantment(ItemStack item) {
		NBTTagCompound tag = getItemStackTag(item);
		tag.setList("ench", new NBTTagList());
		setItemStackTag(item, tag);
	}
}
