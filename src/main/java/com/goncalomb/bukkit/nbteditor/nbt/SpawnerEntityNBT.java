/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.entity.EntityType;

import com.goncalomb.bukkit.mylib.namemaps.EntityTypeMap;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public class SpawnerEntityNBT {

	private int _weight;
	private EntityNBT _entityNbt;

	public static boolean isValidCreature(String name) {
		return EntityType.fromName(name).isAlive();
	}

	public SpawnerEntityNBT(EntityType entityType)  {
		this(entityType, 1);
	}

	public SpawnerEntityNBT(EntityType entityType, int weight) {
		_weight = weight;
		_entityNbt = EntityNBT.fromEntityType(entityType);
	}

	public SpawnerEntityNBT(EntityNBT entityNbt) {
		this(entityNbt, 1);
	}

	public SpawnerEntityNBT(EntityNBT entityNbt, int weight) {
		_weight = weight;
		_entityNbt = entityNbt;
	}

	public int getWeight() {
		return _weight;
	}

	public EntityNBT getEntityNBT() {
		return _entityNbt;
	}

	public EntityType getEntityType() {
		return _entityNbt.getEntityType();
	}

	public SpawnerEntityNBT clone() {
		return new SpawnerEntityNBT(_entityNbt.clone(), _weight);
	}

	NBTTagCompound buildTagCompound() {
		NBTTagCompound data = new  NBTTagCompound();
		data.setInt("Weight", _weight);
		data.setString("Type", EntityTypeMap.getName(_entityNbt.getEntityType()));
		data.setCompound("Properties", _entityNbt._data);
		return data;
	}

}
