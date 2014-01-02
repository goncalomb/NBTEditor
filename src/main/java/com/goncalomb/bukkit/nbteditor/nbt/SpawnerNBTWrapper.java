/*
 * Copyright (C) 2013, 2014 - Gonçalo Baltazar <http://goncalomb.com>
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

import com.goncalomb.bukkit.bkglib.namemaps.EntityTypeMap;
import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.bkglib.reflect.NBTTagList;
import com.goncalomb.bukkit.bkglib.reflect.NBTUtils;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.ShortVariable;

public final class SpawnerNBTWrapper {
	
	private static NBTGenericVariableContainer _variables;
	
	static {
		_variables = new NBTGenericVariableContainer("Spawner");
		_variables.add("count", new ShortVariable("SpawnCount", (short) 0));
		_variables.add("range", new ShortVariable("SpawnRange", (short) 0));
		_variables.add("delay", new ShortVariable("Delay", (short) 0));
		_variables.add("min-delay", new ShortVariable("MinSpawnDelay", (short) 0));
		_variables.add("max-delay", new ShortVariable("MaxSpawnDelay", (short) 0));
		_variables.add("max-entities", new ShortVariable("MaxNearbyEntities", (short) 0));
		_variables.add("player-range", new ShortVariable("RequiredPlayerRange", (short) 0));
	}
	
	private Block _spawnerBlock;
	private NBTTagCompound _data;
	private List<SpawnerEntityNBT> _entities;
	
	public static Collection<String> variableNames() {
		return Collections.unmodifiableCollection(_variables.getVarNames());
	}
	
	public SpawnerNBTWrapper(Block block) {
		_spawnerBlock = block;
		_data = NBTUtils.getTileEntityNBTData(block);
		
		if (_data.hasKey("SpawnPotentials")) {
			NBTTagList spawnPotentials = _data.getList("SpawnPotentials");
			int l = spawnPotentials.size();
			
			_entities = new ArrayList<SpawnerEntityNBT>(l);
			for (int i = 0; i < l; ++i) {
				NBTTagCompound potential = (NBTTagCompound) spawnPotentials.get(i);
				EntityType entityType = EntityTypeMap.getByName(potential.getString("Type"));
				if (entityType != null) {
					EntityNBT entityNbt;
					if (potential.hasKey("Properties")) {
						entityNbt = EntityNBT.fromEntityType(entityType, potential.getCompound("Properties"));
					} else {
						entityNbt = EntityNBT.fromEntityType(entityType);
					}
					_entities.add(new SpawnerEntityNBT(entityNbt, potential.getInt("Weight")));
				}
			}
			_data.remove("SpawnPotentials");
		} else {
			_entities = new ArrayList<SpawnerEntityNBT>();
		}
	}
	
	public void addEntity(SpawnerEntityNBT spawnerEntityNbt) {
		_data.setString("EntityId", EntityTypeMap.getName(spawnerEntityNbt.getEntityType()));
		_data.setCompound("SpawnData", spawnerEntityNbt.getEntityNBT()._data.clone());
		_entities.add(spawnerEntityNbt);
	}
	
	public void clearEntities() {
		_entities.clear();
	}

	public void cloneFrom(SpawnerNBTWrapper other) {
		NBTTagCompound clone = other._data.clone();
		clone.remove("id");
		clone.remove("x");
		clone.remove("y");
		clone.remove("z");
		_data.merge(clone);
		this._entities = new ArrayList<SpawnerEntityNBT>(other._entities.size());
		for (SpawnerEntityNBT spawnerEntityNBT : other._entities) {
			this._entities.add(spawnerEntityNBT.clone());
		}
	}
	
	public List<SpawnerEntityNBT> getEntities() {
		return _entities;
	}
	
	public void removeEntity(int index) {
		_entities.remove(index);
	}
	
	public EntityType getCurrentEntity() {
		return EntityTypeMap.getByName(_data.getString("EntityId"));
	}
	
	public Location getLocation() {
		return _spawnerBlock.getLocation();
	}
	
	public NBTVariableContainer getVariables() {
		return _variables.boundToData(_data);
	}
	
	public NBTVariable getVariable(String name) {
		return _variables.getVariable(name, _data);
	}
	
	public void save() {
		if (_entities.size() > 0) {
			NBTTagList spawnPotentials = new NBTTagList();
			for (SpawnerEntityNBT spawnerEntityNbt : _entities) {
				spawnPotentials.add(spawnerEntityNbt.buildTagCompound());
			}
			_data.setList("SpawnPotentials", spawnPotentials);
		} else {
			_data.setString("EntityId", "Pig");
			_data.remove("SpawnData");
			_data.remove("SpawnPotentials");
		}
		NBTUtils.setTileEntityNBTData(_spawnerBlock, _data);
	}
	
}
