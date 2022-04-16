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

package com.goncalomb.bukkit.nbteditor.nbt;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

import com.goncalomb.bukkit.mylib.namemaps.EntityTypeMap;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;

public final class SpawnerNBTWrapper extends TileNBTWrapper {

	public static class SpawnerEntity {

		public final EntityNBT entityNBT;
		public final int weight;

		public SpawnerEntity(EntityNBT entityNBT, int weight) {
			this.entityNBT = entityNBT;
			this.weight = (weight < 1 ? 1 : weight);
		}

		public SpawnerEntity(EntityType entityType, int weight) {
			this(EntityNBT.fromEntityType(entityType), weight);
		}

		NBTTagCompound getCompound() {
			NBTTagCompound data = new  NBTTagCompound();
			data.setInt("Weight", weight);
			data.setCompound("Entity", entityNBT._data);
			return data;
		}

	}

	public SpawnerNBTWrapper(Block block) {
		super(block);
	}

	public void addEntity(SpawnerEntity entity) {
		List<SpawnerEntity> entities = getEntities();
		entities.add(entity);
		_data.setCompound("SpawnData", entity.entityNBT._data);
		setEntities(entities);
	}

	public List<SpawnerEntity> getEntities() {
		ArrayList<SpawnerEntity> entities = new ArrayList<SpawnerEntity>();
		if (_data.hasKey("SpawnPotentials")) {
			NBTTagList spawnPotentials = _data.getList("SpawnPotentials");
			int l = spawnPotentials.size();
			for (int i = 0; i < l; ++i) {
				NBTTagCompound potential = (NBTTagCompound) spawnPotentials.get(i);
				EntityNBT entityNbt = EntityNBT.fromEntityData(potential.getCompound("Entity"));
				if (entityNbt != null) {
					entities.add(new SpawnerEntity(entityNbt, potential.getInt("Weight")));
				}
			}
			_data.remove("SpawnPotentials");
		}
		return entities;
	}

	public void setEntities(List<SpawnerEntity> entities) {
		if (entities != null && entities.size() > 0) {
			NBTTagList spawnPotentials = new NBTTagList();
			for (SpawnerEntity entity : entities) {
				spawnPotentials.add(entity.getCompound());
			}
			_data.setList("SpawnPotentials", spawnPotentials);
		} else {
			NBTTagCompound simplePig = new NBTTagCompound();

			_data.setCompound("SpawnData", simplePig);
			_data.remove("SpawnPotentials");
		}

	}

	public void clearEntities() {
		setEntities(null);
	}

	public EntityType getCurrentEntity() {
		NBTTagCompound spawnData = _data.getCompound("SpawnData");
		if (spawnData != null) {
			return EntityTypeMap.getByName(spawnData.getString("id"));
		}
		return null;
	}

}
