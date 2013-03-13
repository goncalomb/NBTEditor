package com.goncalomb.bukkit.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.entity.ExperienceOrb;

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
	
}
