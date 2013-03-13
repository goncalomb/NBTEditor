package com.goncalomb.bukkit.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;

public final class BukkitReflect {
	
	private final static class CachedPackage {
		
		private String _packageName;
		private HashMap<String, Class<?>> _cache = new HashMap<String, Class<?>>();
		
		public CachedPackage(String packageName) {
			_packageName = packageName;
		}
		
		public Class<?> getClass(String className) {
			Class <?> clazz = _cache.get(className);
			if (clazz == null) {
				try {
					clazz = this.getClass().getClassLoader().loadClass(_packageName + "." + className);
				} catch (ClassNotFoundException e) {
					throw new Error("Cannot find class " + _packageName + "." + className + ".", e);
				}
				_cache.put(className, clazz);
			}
			return clazz;
		}
		
	}
	
	private static boolean _isPrepared = false;
	
	private static CachedPackage _craftBukkitPackage;
	private static CachedPackage _minecraftPackage;
	
	private static Method _getCommandMap;
	
	public static void prepareReflection() {
		if (!_isPrepared) {
			Server server = Bukkit.getServer();
			_craftBukkitPackage = new CachedPackage(server.getClass().getPackage().getName());
			
			try {
				Class<?> craftServerClass = server.getClass();
				Method getHandle = craftServerClass.getMethod("getHandle");
				_minecraftPackage = new CachedPackage(getHandle.getReturnType().getPackage().getName());
				_getCommandMap = craftServerClass.getMethod("getCommandMap");
			} catch (NoSuchMethodException e) {
				throw new Error("Cannot find getHandle() method on server.", e);
			}
			
			_isPrepared = true;
		}
	}
	
	private BukkitReflect() { }
	
	public static Class<?> getCraftBukkitClass(String className) {
		prepareReflection();
		return _craftBukkitPackage.getClass(className);
	}
	
	public static Class<?> getMinecraftClass(String className) {
		prepareReflection();
		return _minecraftPackage.getClass(className);
	}
	
	public static SimpleCommandMap getCommandMap() {
		prepareReflection();
		return (SimpleCommandMap) invokeMethod(Bukkit.getServer(), _getCommandMap);
	}
	
	static Object invokeMethod(Object object, Method method, Object... args) {
		try {
			return method.invoke(object, args);
		} catch (Exception e) {
			throw new Error("Error while invoking " + method.getName() + ".", e);
		}
	}
	
	static Object newInstance(Class<?> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new Error("Error creating instance of " + clazz.getSimpleName() + ".", e);
		}
	}
	
	static Object newInstance(Constructor<?> contructor, Object... initargs) {
		try {
			return contructor.newInstance(initargs);
		} catch (Exception e) {
			throw new Error("Error creating instance of " + contructor.getDeclaringClass().getSimpleName() + ".", e);
		}
	}
	
}
