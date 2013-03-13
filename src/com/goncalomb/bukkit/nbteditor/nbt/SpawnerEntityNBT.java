package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.entity.EntityType;

import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;

public class SpawnerEntityNBT {
	
	private int _weight;
	private EntityNBT _entityNbt;

	public static boolean isValidCreature(String name) {
		return EntityNBT.isValidType(EntityType.fromName(name));
	}
	
	public static EntityType[] getValidCreatures() {
		return EntityNBT.getValidEntityTypes();
	}
	
	public SpawnerEntityNBT(String creatureName)  {
		this(creatureName, 1);
	}
	
	public SpawnerEntityNBT(String creatureName, int weight) {
		if (!isValidCreature(creatureName)) throw new IllegalArgumentException("Invalid argument creatureName, " + creatureName + ".");
		if (weight < 1) throw new IllegalArgumentException("Invalid argument weight, " + weight + ".");
		_weight = weight;
		_entityNbt = EntityNBT.fromAnyEntityType(EntityType.fromName(creatureName));
	}
	
	public SpawnerEntityNBT(EntityNBT entityNbt) {
		this(entityNbt, 1);
	}
	
	public SpawnerEntityNBT(EntityNBT entityNbt, int weight) {
		if (entityNbt.getEntityType() == null) throw new IllegalArgumentException("Invalid argument entityNbt.");
		if (weight < 1) throw new IllegalArgumentException("Invalid argument weight, " + weight + ".");
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
		data.setString("Type", _entityNbt.getEntityType().getName());
		data.setCompound("Properties", _entityNbt._data);
		return data;
	}
	
}
