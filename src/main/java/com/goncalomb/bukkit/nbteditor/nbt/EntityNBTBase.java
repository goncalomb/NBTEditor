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

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.goncalomb.bukkit.mylib.namemaps.EntityTypeMap;
import com.goncalomb.bukkit.mylib.reflect.BukkitReflect;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

import net.iharder.Base64;

abstract class EntityNBTBase extends BaseNBT {

	public static EntityNBT fromEntityType(EntityType type) {
		return EntityNBT.newInstance(type, null);
	}

	public static EntityNBT fromEntityData(NBTTagCompound data) {
		EntityType entityType = EntityTypeMap.getByName(data.getString("id"));

		EntityType entityTypeNew = entityType;
		// Backward compatibility with pre-1.11.
		if (entityType == EntityType.GUARDIAN) {
			if (data.hasKey("Elder")) {
				if (data.getByte("Elder") != 0) {
					entityTypeNew = EntityType.ELDER_GUARDIAN;
				}
				data.remove("Elder");
			}
		} else if (entityType == EntityType.SKELETON) {
			if (data.hasKey("SkeletonType")) {
				switch (data.getByte("SkeletonType")) {
				case 1: entityTypeNew = EntityType.WITHER_SKELETON; break;
				case 2: entityTypeNew = EntityType.STRAY; break;
				}
				data.remove("SkeletonType");
			}
		} else if (entityType == EntityType.ZOMBIE) {
			// pre-1.10
			if (data.hasKey("IsVillager")) {
				if (data.getByte("IsVillager") != 0) {
					entityTypeNew = EntityType.ZOMBIE_VILLAGER;
					if (data.hasKey("VillagerProfession")) {
						data.setInt("Profession", data.getInt("VillagerProfession"));
						data.remove("VillagerProfession");
					}
				}
				data.remove("IsVillager");
			}
			// pre-1.11
			if (data.hasKey("ZombieType")) {
				int zombieType = data.getInt("ZombieType");
				if (zombieType == 0) {
					entityTypeNew = EntityType.ZOMBIE;
				} else if (zombieType >= 1 && zombieType <= 5) {
					entityTypeNew = EntityType.ZOMBIE_VILLAGER;
					data.setInt("Profession", zombieType - 1);
				} else if (zombieType == 6) {
					entityTypeNew = EntityType.HUSK;
				}
				data.remove("ZombieType");
			}
		} else if (entityType == EntityType.HORSE) {
			if (data.hasKey("Type")) {
				switch (data.getInt("Type")) {
				case 1: entityTypeNew = EntityType.DONKEY; break;
				case 2: entityTypeNew = EntityType.MULE; break;
				case 3: entityTypeNew = EntityType.ZOMBIE_HORSE; break;
				case 4: entityTypeNew = EntityType.SKELETON_HORSE; break;
				}
				data.remove("Type");
			}
			if (entityTypeNew != EntityType.DONKEY && entityTypeNew != EntityType.MULE) {
				data.remove("ChestedHorse");
			}
		}

		data.setString("id", EntityTypeMap.getName(entityTypeNew));
		entityType = entityTypeNew;

		// Convert custom names to JSON (1.13).
		refreshEntityName(data);

		if (entityType != null) {
			return EntityNBT.newInstance(entityType, data);
		}
		return null;
	}

	private static void refreshEntityName(NBTTagCompound data) {
		String name = data.getString("CustomName");
		if (name != null && !BukkitReflect.isValidRawJSON(name)) {
			data.setString("CustomName", BukkitReflect.textToRawJSON(name));
		}
		Object[] passangers = data.getListAsArray("Passengers");
		if (passangers != null) {
			for (Object passager : passangers) {
				refreshEntityName((NBTTagCompound) passager);
			}
		}
	}

	public static EntityNBT fromEntity(Entity entity) {
		EntityNBT entityNbt = EntityNBT.newInstance(entity.getType(), NBTUtils.getEntityNBTData(entity));
		// When cloning, remove the UUID to force all entities to have a unique one.
		entityNbt._data.remove("UUIDMost");
		entityNbt._data.remove("UUIDLeast");
		return entityNbt;
	}

	public static EntityNBT unserialize(String serializedData) {
		try {
			NBTTagCompound data = NBTTagCompound.unserialize(Base64.decode(serializedData));

			// Backward compatibility with pre-1.9.
			// On 1.9 the entities are stacked for bottom to top.
			// This conversion needs to happen before instantiating any class, we cannot use onUnserialize.
			while (data.hasKey("Riding")) {
				NBTTagCompound riding = data.getCompound("Riding");
				data.remove("Riding");
				riding.setList("Passengers", new NBTTagList(data));
				data = riding;
			}

			return fromEntityData(data);
		} catch (Throwable e) {
			throw new RuntimeException("Error unserializing EntityNBT.", e);
		}
	}

	private EntityType _entityType;

	protected EntityNBTBase(EntityType entityType) {
		super(new NBTTagCompound(), EntityTypeMap.getName(entityType));
		_entityType = entityType;
	}

	abstract public void setDefautData();

	public EntityType getEntityType() {
		return _entityType;
	}

	public Entity spawn(Location location) {
		return NBTUtils.spawnEntity(_data, location);
	}

	public String serialize() {
		try {
			return Base64.encodeBytes(_data.serialize(), Base64.GZIP);
		} catch (Throwable e) {
			throw new RuntimeException("Error serializing EntityNBT.", e);
		}
	}

	protected void backwardCompatibility() {
		// these were moved here from the old classes
		// XXX: remove all backward compatibility in the future

		// EquippableNBT
		// Backward compatibility with pre-1.9.
		if (_data.hasKey("Equipment")) {
			Object[] equip = _data.getListAsArray("Equipment");
			_data.setList("HandItems", new NBTTagList(equip[0], new NBTTagCompound()));
			_data.setList("ArmorItems", new NBTTagList(Arrays.copyOfRange(equip, 1, 5)));
			_data.remove("Equipment");
		}

		// MobNBT
		// Backward compatibility with pre-1.9.
		if (_data.hasKey("HealF")) {
			_data.setFloat("Health", _data.getFloat("HealF"));
			_data.remove("HealF");
		}
		if (_data.hasKey("DropChances")) {
			Object[] drop = _data.getListAsArray("DropChances");
			_data.setList("HandDropChances", new NBTTagList(drop[0], Float.valueOf(0f)));
			_data.setList("ArmorDropChances", new NBTTagList(Arrays.copyOfRange(drop, 1, 5)));
			_data.remove("DropChances");
		}
	}

	public EntityNBT clone() {
		return fromEntityData(_data.clone());
	}

	public NBTTagCompound getData() {
		return _data.clone();
	}

	public String getMetadataString() {
		NBTTagCompound data = _data.clone();
		data.remove("id");
		return data.toString();
	}

}
