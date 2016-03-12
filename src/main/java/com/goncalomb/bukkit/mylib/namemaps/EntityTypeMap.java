/*
 * Copyright (C) 2013-2016 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.mylib.namemaps;

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
		} else if (name.equalsIgnoreCase("ThrownEgg")) {
			return EntityType.EGG;
		} else if (name.equalsIgnoreCase("MinecartSpawner")) {
			return EntityType.MINECART_MOB_SPAWNER;
		} else if (name.equalsIgnoreCase("AreaEffectCloud")) {
			return EntityType.AREA_EFFECT_CLOUD;
		} else {
			return EntityType.fromName(name);
		}
	}

	public static String getName(EntityType type) {
		if (type == EntityType.SPLASH_POTION) {
			return "ThrownPotion";
		} else if (type == EntityType.EGG) {
			return "ThrownEgg";
		} else if (type == EntityType.MINECART_MOB_SPAWNER) {
			return "MinecartSpawner";
		} else if (type == EntityType.AREA_EFFECT_CLOUD) {
			return "AreaEffectCloud";
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
