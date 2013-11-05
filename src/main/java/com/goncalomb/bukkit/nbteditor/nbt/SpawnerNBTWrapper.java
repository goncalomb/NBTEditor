package com.goncalomb.bukkit.nbteditor.nbt;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

import com.goncalomb.bukkit.bkglib.EntityTypeMap;
import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.bkglib.reflect.NBTTagListWrapper;
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
	private NBTTagCompoundWrapper _data;
	private List<SpawnerEntityNBT> _entities;

	public SpawnerNBTWrapper(Block block) {
		_spawnerBlock = block;
		_data = NBTUtils.getTileEntityNBTTagCompound(block);
		
		if (_data.hasKey("SpawnPotentials")) {
			NBTTagListWrapper spawnPotentials = _data.getList("SpawnPotentials");
			int l = spawnPotentials.size();
			
			_entities = new ArrayList<SpawnerEntityNBT>(l);
			for (int i = 0; i < l; ++i) {
				NBTTagCompoundWrapper potential = (NBTTagCompoundWrapper) spawnPotentials.get(i);
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
		NBTTagCompoundWrapper clone = other._data.clone();
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
			NBTTagListWrapper spawnPotentials = new NBTTagListWrapper();
			for (SpawnerEntityNBT spawnerEntityNbt : _entities) {
				spawnPotentials.add(spawnerEntityNbt.buildTagCompound());
			}
			_data.set("SpawnPotentials", spawnPotentials);
		} else {
			_data.setString("EntityId", "Pig");
			_data.remove("SpawnData");
			_data.remove("SpawnPotentials");
		}
		NBTUtils.setTileEntityNBTTagCompound(_spawnerBlock, _data);
	}
	
}
