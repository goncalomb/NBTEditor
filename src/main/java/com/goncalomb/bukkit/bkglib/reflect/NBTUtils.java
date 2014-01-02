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

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

public final class NBTUtils {
	
	// Minecraft's ItemStack Class;
	private static Method _createStack;
	private static Method _save;
	private static Method _getTag;
	private static Method _setTag;
	
	// CraftItemStack Class;
	private static Method _asBukkitCopy;
	private static Method _asNMSCopy;
	private static Field _handle;
	
	// Minecraft's Entity Class;
	private static Method _e0; // Save data to NBTTagCompound.
	private static Method _f0; // Load data from NBTTagCompound (generic data).
	private static Method _a0; // Load data from NBTTagCompound (entity specific data), this is a protected method.
	
	// CraftEntity Class
	private static Method _getHandle;
	
	// Minecraft's TileEntity
	private static Method _b1; // Save data to NBTTagCompound.
	private static Method _a1; // Load data from NBTTagCompound.
	
	// CraftWorld
	private static Method _getTileEntity;
	
	static void prepareReflection() throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		Class<?> nbtTagCompoundClass = BukkitReflect.getMinecraftClass("NBTTagCompound");
		
		Class<?> minecraftItemStackClass = BukkitReflect.getMinecraftClass("ItemStack");
		_createStack = minecraftItemStackClass.getMethod("createStack", nbtTagCompoundClass);
		_save = minecraftItemStackClass.getMethod("save", nbtTagCompoundClass);
		_getTag = minecraftItemStackClass.getMethod("getTag");
		_setTag = minecraftItemStackClass.getMethod("setTag", nbtTagCompoundClass);
		
		Class<?> craftItemStackClass = BukkitReflect.getCraftBukkitClass("inventory.CraftItemStack");
		_asBukkitCopy = craftItemStackClass.getMethod("asBukkitCopy", minecraftItemStackClass);
		_asNMSCopy = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);
		_handle = craftItemStackClass.getDeclaredField("handle");
		_handle.setAccessible(true);
		
		Class<?> minecraftEntityClass = BukkitReflect.getMinecraftClass("Entity");
		_e0 = minecraftEntityClass.getMethod("e", nbtTagCompoundClass);
		_f0 = minecraftEntityClass.getMethod("f", nbtTagCompoundClass);
		_a0 = minecraftEntityClass.getDeclaredMethod("a", nbtTagCompoundClass);
		_a0.setAccessible(true);
		
		Class<?> craftEntityClass = BukkitReflect.getCraftBukkitClass("entity.CraftEntity");
		_getHandle = craftEntityClass.getMethod("getHandle");
		
		Class<?> minecraftTileEntityClass = BukkitReflect.getMinecraftClass("TileEntity");
		_b1 = minecraftTileEntityClass.getMethod("b", nbtTagCompoundClass);
		_a1 = minecraftTileEntityClass.getMethod("a", nbtTagCompoundClass);
		
		Class<?> craftWorldClass = BukkitReflect.getCraftBukkitClass("CraftWorld");
		_getTileEntity = craftWorldClass.getMethod("getTileEntityAt", int.class, int.class, int.class);
	}
	
	private NBTUtils() { }
	
	public static ItemStack itemStackFromNBTData(NBTTagCompound data) {
		return (ItemStack) BukkitReflect.invokeMethod(null, _asBukkitCopy, BukkitReflect.invokeMethod(null, _createStack, data._handle));
	}
	
	public static NBTTagCompound itemStackToNBTData(ItemStack stack) {
		NBTTagCompound data = new NBTTagCompound();
		BukkitReflect.invokeMethod(BukkitReflect.invokeMethod(null, _asNMSCopy, stack), _save, data._handle);
		return data;
	}
	
	static NBTTagCompound getInternalEntityNBTData(Object minecraftEntity) {
		NBTTagCompound data = new NBTTagCompound();
		BukkitReflect.invokeMethod(minecraftEntity, _e0, data._handle);
		return data;
	}
	
	static void setInternalEntityNBTData(Object minecraftEntity, NBTTagCompound data) {
		NBTTagCompound entityData = getInternalEntityNBTData(minecraftEntity);
		// Do not override UUID and position.
		long uuidMost = entityData.getLong("UUIDMost");
		long uuidLeast = entityData.getLong("UUIDLeast");
		NBTTagList pos = entityData.getList("Pos");
		// Merge the data.
		entityData.merge(data);
		// Re-apply UUID and position.
		entityData.setLong("UUIDMost", uuidMost);
		entityData.setLong("UUIDLeast", uuidLeast);
		entityData.setList("Pos", pos);
		// Apply data.
		BukkitReflect.invokeMethod(minecraftEntity, _f0, entityData._handle);
		BukkitReflect.invokeMethod(minecraftEntity, _a0, entityData._handle);
	}
	
	public static NBTTagCompound getEntityNBTData(Entity entity) {
		return getInternalEntityNBTData(BukkitReflect.invokeMethod(entity, _getHandle));
	}
	
	public static void setEntityNBTData(Entity entity, NBTTagCompound data) {
		setInternalEntityNBTData(BukkitReflect.invokeMethod(entity, _getHandle), data);
	}
	
	public static NBTTagList potionToNBTEffectsList(ItemStack potion) {
		Object tagObject = BukkitReflect.invokeMethod(BukkitReflect.invokeMethod(null, _asNMSCopy, potion), _getTag);
		if (tagObject != null) {
			NBTTagCompound tag = new NBTTagCompound(tagObject);
			if (tag != null && tag.hasKey("CustomPotionEffects")) {
				return tag.getList("CustomPotionEffects").clone();
			}
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
		return BukkitReflect.invokeMethod(block.getWorld(), _getTileEntity, block.getX(), block.getY(), block.getZ());
	}
	
	public static NBTTagCompound getTileEntityNBTData(Block block) {
		NBTBase.prepareReflection();
		Object tileEntity = getTileEntity(block);
		if (tileEntity != null) {
			NBTTagCompound data = new NBTTagCompound();
			BukkitReflect.invokeMethod(tileEntity, _b1, data._handle);
			return data;
		}
		return null;
	}
	
	public static void setTileEntityNBTData(Block block, NBTTagCompound data) {
		NBTBase.prepareReflection();
		Object tileEntity = getTileEntity(block);
		if (tileEntity != null) {
			BukkitReflect.invokeMethod(tileEntity, _a1, data._handle);
		}
	}
	
	public static NBTTagCompound getItemStackTag(ItemStack item) {
		Object handle = BukkitReflect.getFieldValue(item, _handle);
		Object tag = BukkitReflect.invokeMethod(handle, _getTag);
		return (tag == null ? new NBTTagCompound() : new NBTTagCompound(tag));
	}
	
	public static void setItemStackTag(ItemStack item, NBTTagCompound tag) {
		Object handle = BukkitReflect.getFieldValue(item, _handle);
		BukkitReflect.invokeMethod(handle, _setTag, tag._handle);
	}
	
	public static void setItemStackFakeEnchantment(ItemStack item) {
		NBTTagCompound tag = getItemStackTag(item);
		tag.setList("ench", new NBTTagList());
		setItemStackTag(item, tag);
	}
}
