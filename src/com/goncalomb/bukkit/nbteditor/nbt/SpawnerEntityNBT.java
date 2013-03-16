package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.entity.EntityType;

import com.goncalomb.bukkit.EntityTypeMap;
import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;

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
		_entityNbt = EntityNBT.fromAnyEntityType(entityType);
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
	
	NBTTagCompoundWrapper buildTagCompound() {
		NBTTagCompoundWrapper data = new  NBTTagCompoundWrapper();
		data.setInt("Weight", _weight);
		data.setString("Type", EntityTypeMap.getName(_entityNbt.getEntityType()));
		data.setCompound("Properties", _entityNbt._data);
		return data;
	}
	
}
