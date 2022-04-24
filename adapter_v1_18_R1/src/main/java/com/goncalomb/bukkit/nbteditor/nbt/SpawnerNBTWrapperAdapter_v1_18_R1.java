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

import com.goncalomb.bukkit.mylib.namemaps.EntityTypeMap;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;

import org.bukkit.entity.EntityType;

// minecraft:spawner{Delay:0s,MaxNearbyEntities:6s,MaxSpawnDelay:600s,MinSpawnDelay:400s,RequiredPlayerRange:10s,SpawnCount:4s,SpawnData:{entity:{ArmorItems:[{Count:1b,id:"minecraft:leather_boots",tag:{display:{color:1908001}}},{Count:1b,id:"minecraft:leather_leggings",tag:{display:{color:7567221}}},{Count:1b,id:"minecraft:leather_chestplate",tag:{display:{color:7567221}}},{Count:1b,id:"minecraft:leather_helmet",tag:{display:{Name:'{"text":"clSacrifice\'s Scalp"}',color:9468004},plain:{display:{Name:"Sacrifice's Scalp"}}}}],Attributes:[{Base:30.0d,Name:"minecraft:generic.max_health"}],CustomName:'{"text":"Desiccated Husk"}',HandItems:[{Count:1b,id:"minecraft:yellow_glazed_terracotta",tag:{AttributeModifiers:[{Amount:7.0d,AttributeName:"minecraft:generic.attack_damage",Name:"Modifier",Operation:0,Slot:"mainhand",UUID:[I;1255868359,-901623816,-1585118361,1237496632]},{Amount:0.12d,AttributeName:"minecraft:generic.movement_speed",Name:"Modifier",Operation:1,Slot:"mainhand",UUID:[I;-1540949385,586696667,-1422368804,222333757]}]}},{}],Health:30.0f,Profession:3,id:"minecraft:zombie_villager",plain:{CustomName:"Desiccated Husk"}}},SpawnPotentials:[{data:{entity:{ArmorItems:[{Count:1b,id:"minecraft:leather_boots",tag:{display:{color:1908001}}},{Count:1b,id:"minecraft:leather_leggings",tag:{display:{color:7567221}}},{Count:1b,id:"minecraft:leather_chestplate",tag:{display:{color:7567221}}},{Count:1b,id:"minecraft:leather_helmet",tag:{display:{Name:'{"text":"clSacrifice\'s Scalp"}',color:9468004},plain:{display:{Name:"Sacrifice's Scalp"}}}}],Attributes:[{Base:30.0d,Name:"minecraft:generic.max_health"}],CustomName:'{"text":"Desiccated Husk"}',HandItems:[{Count:1b,id:"minecraft:yellow_glazed_terracotta",tag:{AttributeModifiers:[{Amount:7.0d,AttributeName:"minecraft:generic.attack_damage",Name:"Modifier",Operation:0,Slot:"mainhand",UUID:[I;1255868359,-901623816,-1585118361,1237496632]},{Amount:0.12d,AttributeName:"minecraft:generic.movement_speed",Name:"Modifier",Operation:1,Slot:"mainhand",UUID:[I;-1540949385,586696667,-1422368804,222333757]}]}},{}],Health:30.0f,Profession:3,id:"minecraft:zombie_villager",plain:{CustomName:"Desiccated Husk"}}},weight:1}],SpawnRange:4s}
public class SpawnerNBTWrapperAdapter_v1_18_R1 implements SpawnerNBTWrapperAdapter {
	public SpawnerNBTWrapperAdapter_v1_18_R1() {
		// No setup needed but this constructor must exist
	}

	public void addEntity(NBTTagCompound data, SpawnerAdapterWrappedEntity entity) {
		List<SpawnerAdapterWrappedEntity> entities = getEntities(data);
		entities.add(entity);
		NBTTagCompound spawnData = new NBTTagCompound();
		spawnData.setCompound("entity", entity.data);
		data.setCompound("SpawnData", spawnData);
		setEntities(data, entities);
	}

	public List<SpawnerAdapterWrappedEntity> getEntities(NBTTagCompound data) {
		ArrayList<SpawnerAdapterWrappedEntity> entities = new ArrayList<SpawnerAdapterWrappedEntity>();
		if (data.hasKey("SpawnPotentials")) {
			NBTTagList spawnPotentials = data.getList("SpawnPotentials");
			int l = spawnPotentials.size();
			for (int i = 0; i < l; ++i) {
				NBTTagCompound potential = (NBTTagCompound) spawnPotentials.get(i);
				NBTTagCompound dataTag = potential.getCompound("data");
				if (dataTag != null) {
					NBTTagCompound entityNbt = dataTag.getCompound("entity");
					if (entityNbt != null) {
						entities.add(new SpawnerAdapterWrappedEntity(entityNbt, potential.getInt("weight")));
					}
				}
			}
		}
		return entities;
	}

	public void setEntities(NBTTagCompound data, List<SpawnerAdapterWrappedEntity> entities) {
		if (entities != null && entities.size() > 0) {
			NBTTagList spawnPotentials = new NBTTagList();
			for (SpawnerAdapterWrappedEntity entity : entities) {
				NBTTagCompound newEntry = new NBTTagCompound();
				newEntry.setInt("weight", entity.weight);
				NBTTagCompound newEntryData = new NBTTagCompound();
				newEntryData.setCompound("entity", entity.data);
				newEntry.setCompound("data", newEntryData);
				spawnPotentials.add(newEntry);
			}
			data.setList("SpawnPotentials", spawnPotentials);
		} else {
			NBTTagCompound simplePigData = new NBTTagCompound();
			simplePigData.setCompound("entity", new NBTTagCompound());

			data.setCompound("SpawnData", simplePigData);
			data.remove("SpawnPotentials");
		}
	}

	public EntityType getCurrentEntity(NBTTagCompound data) {
		NBTTagCompound spawnData = data.getCompound("SpawnData");
		if (spawnData != null && spawnData.hasKey("entity")) {
			return EntityTypeMap.getByName(spawnData.getCompound("entity").getString("id"));
		}
		return null;
	}
}
