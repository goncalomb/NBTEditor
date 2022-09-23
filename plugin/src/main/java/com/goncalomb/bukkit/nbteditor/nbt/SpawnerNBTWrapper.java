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
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.goncalomb.bukkit.mylib.reflect.BukkitReflect;
import com.goncalomb.bukkit.nbteditor.nbt.SpawnerNBTWrapperAdapter.SpawnerAdapterWrappedEntity;

import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

public final class SpawnerNBTWrapper extends TileNBTWrapper {

	private static SpawnerNBTWrapperAdapter adapter = null;

	public static void prepareReflection(Class<?> serverClass, Logger logger) throws Exception {
		String packageName = serverClass.getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf('.') + 1);

		Class<?> clazz = Class.forName("com.goncalomb.bukkit.nbteditor.nbt.SpawnerNBTWrapperAdapter_" + version);
		adapter = (SpawnerNBTWrapperAdapter) clazz.getConstructor().newInstance();
		logger.info("Loaded SpawnerNBTWrapper adapter for " + version);
	}

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

		private SpawnerAdapterWrappedEntity wrapForAdapter() {
			return new SpawnerAdapterWrappedEntity(entityNBT._data, weight);
		}

		private static SpawnerEntity fromAdapterWrapped(SpawnerAdapterWrappedEntity wrapped) {
			return new SpawnerEntity(EntityNBT.fromEntityData(wrapped.data), wrapped.weight);
		}
	}

	public SpawnerNBTWrapper(Block block) {
		super(block);
	}

	public void addEntity(SpawnerEntity entity) {
		BukkitReflect.ensureAdapter(adapter);
		adapter.addEntity(_data, entity.wrapForAdapter());
	}

	public List<SpawnerEntity> getEntities() {
		BukkitReflect.ensureAdapter(adapter);
		List<SpawnerAdapterWrappedEntity> entities = adapter.getEntities(_data);
		if (entities == null) {
			return null;
		}
		return new ArrayList<>(entities.stream().map((entity) -> SpawnerEntity.fromAdapterWrapped(entity)).collect(Collectors.toList()));
	}

	public void setEntities(List<SpawnerEntity> entities) {
		BukkitReflect.ensureAdapter(adapter);
		if (entities == null) {
			adapter.setEntities(_data, null);
		} else {
			adapter.setEntities(_data, entities.stream().map((entity) -> entity.wrapForAdapter()).collect(Collectors.toList()));
		}
	}

	public void clearEntities() {
		setEntities(null);
	}

	public EntityType getCurrentEntity() {
		BukkitReflect.ensureAdapter(adapter);
		return adapter.getCurrentEntity(_data);
	}
}
