package com.goncalomb.bukkit.bkglib.namemaps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.EntityType;

public final class EntityTypeMap {
	
	private final static List<String> _entityNames;
	private final static List<String> _livingEntityNames;
	private final static String _entityNamesAsString;
	private final static String _livingEntityNamesAsString;
	
	static {
		List<String> entityNames = new ArrayList<String>();
		List<String> livingEntityNames = new ArrayList<String>();
		for (EntityType type : EntityType.values()) {
			String name = getName(type);
			if (name != null && type != EntityType.PLAYER) {
				entityNames.add(name);
				if (type.isAlive()) {
					livingEntityNames.add(name);
				}
			}
		}
		_entityNames = Collections.unmodifiableList(entityNames);
		_livingEntityNames = Collections.unmodifiableList(livingEntityNames);
		_entityNamesAsString = StringUtils.join(_entityNames, ", ");
		_livingEntityNamesAsString = StringUtils.join(_livingEntityNames, ", "); 
	}
	
	private EntityTypeMap() { }
	
	public static EntityType getByName(String name) {
		if (name.equalsIgnoreCase("ThrownPotion")) {
			return EntityType.SPLASH_POTION;
		} else if (name.equalsIgnoreCase("MinecartSpawner")) {
			return EntityType.MINECART_MOB_SPAWNER;
		} else {
			return EntityType.fromName(name);
		}
	}
	
	public static String getName(EntityType type) {
		if (type == EntityType.SPLASH_POTION) {
			return "ThrownPotion";
		} else if (type == EntityType.MINECART_MOB_SPAWNER) {
			return "MinecartSpawner";
		} else {
			return type.getName();
		}
	}
	
	public static List<String> getNames() {
		return _entityNames;
	}
	
	public static List<String> getNames(Collection<EntityType> types) {
		ArrayList<String> names = new ArrayList<String>(types.size());
		for (EntityType type : types) {
			names.add(getName(type));
		}
		return names;
	}
	
	public static String getNamesAsString() {
		return _entityNamesAsString;
	}
	
	public static List<String> getLivingNames() {
		return _livingEntityNames;
	}
	
	public static String getLivingNamesAsString() {
		return _livingEntityNamesAsString;
	}
	
}
