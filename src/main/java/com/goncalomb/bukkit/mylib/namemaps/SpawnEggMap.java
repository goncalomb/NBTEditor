package com.goncalomb.bukkit.mylib.namemaps;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class SpawnEggMap {

	private final static NamingMap<Material> _spawnEggs = new NamingMap<Material>();

	static {
		for (Material mat : Material.values()) {
			String name = mat.name();
			if (name.endsWith("_SPAWN_EGG")) {
				String entityName = name.substring(0, name.length() - 10);
				if (entityName.equals("MOOSHROOM")) {
					_spawnEggs.put(EntityTypeMap.getName(EntityType.MUSHROOM_COW), mat);
				} else if (entityName.equals("ZOMBIE_PIGMAN")) {
					_spawnEggs.put(EntityTypeMap.getName(EntityType.ZOMBIFIED_PIGLIN), mat);
				} else {
					EntityType entityType = EntityType.valueOf(entityName);
					if (entityType != null) {
						_spawnEggs.put(EntityTypeMap.getName(entityType), mat);
					}
				}
			}
		}
	}

	public static Material getEggForEntity(String type) {
		return _spawnEggs.getByName(type);
	}

	public static Material getEggForEntity(EntityType type) {
		return _spawnEggs.getByName(EntityTypeMap.getName(type));
	}

	public static String getEntityType(Material egg) {
		return _spawnEggs.getName(egg);
	}

	private SpawnEggMap() { }
}
