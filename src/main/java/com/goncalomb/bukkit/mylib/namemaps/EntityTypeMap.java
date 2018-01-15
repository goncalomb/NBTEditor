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

package com.goncalomb.bukkit.mylib.namemaps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.EntityType;

public final class EntityTypeMap {

	private final static HashMap<String, EntityType> _legacyNames = new HashMap<String, EntityType>();
	private final static List<String> _entityNames;
	private final static List<String> _livingEntityNames;
	private final static String _entityNamesAsString;
	private final static String _livingEntityNamesAsString;

	static {
		_legacyNames.put("Item".toLowerCase(), EntityType.DROPPED_ITEM);
		_legacyNames.put("XPOrb".toLowerCase(), EntityType.EXPERIENCE_ORB);
		_legacyNames.put("LeashKnot".toLowerCase(), EntityType.LEASH_HITCH);
		_legacyNames.put("Painting".toLowerCase(), EntityType.PAINTING);
		_legacyNames.put("Arrow".toLowerCase(), EntityType.ARROW);
		_legacyNames.put("Snowball".toLowerCase(), EntityType.SNOWBALL);
		_legacyNames.put("Fireball".toLowerCase(), EntityType.FIREBALL);
		_legacyNames.put("SmallFireball".toLowerCase(), EntityType.SMALL_FIREBALL);
		_legacyNames.put("ThrownEnderpearl".toLowerCase(), EntityType.ENDER_PEARL);
		_legacyNames.put("EyeOfEnderSignal".toLowerCase(), EntityType.ENDER_SIGNAL);
		_legacyNames.put("ThrownExpBottle".toLowerCase(), EntityType.THROWN_EXP_BOTTLE);
		_legacyNames.put("ItemFrame".toLowerCase(), EntityType.ITEM_FRAME);
		_legacyNames.put("WitherSkull".toLowerCase(), EntityType.WITHER_SKULL);
		_legacyNames.put("PrimedTnt".toLowerCase(), EntityType.PRIMED_TNT);
		_legacyNames.put("FallingSand".toLowerCase(), EntityType.FALLING_BLOCK);
		_legacyNames.put("FireworksRocketEntity".toLowerCase(), EntityType.FIREWORK);
		_legacyNames.put("SpectralArrow".toLowerCase(), EntityType.SPECTRAL_ARROW);
		_legacyNames.put("ShulkerBullet".toLowerCase(), EntityType.SHULKER_BULLET);
		_legacyNames.put("DragonFireball".toLowerCase(), EntityType.DRAGON_FIREBALL);
		_legacyNames.put("ArmorStand".toLowerCase(), EntityType.ARMOR_STAND);
		_legacyNames.put("MinecartCommandBlock".toLowerCase(), EntityType.MINECART_COMMAND);
		_legacyNames.put("Boat".toLowerCase(), EntityType.BOAT);
		_legacyNames.put("MinecartRideable".toLowerCase(), EntityType.MINECART);
		_legacyNames.put("MinecartChest".toLowerCase(), EntityType.MINECART_CHEST);
		_legacyNames.put("MinecartFurnace".toLowerCase(), EntityType.MINECART_FURNACE);
		_legacyNames.put("MinecartTNT".toLowerCase(), EntityType.MINECART_TNT);
		_legacyNames.put("MinecartHopper".toLowerCase(), EntityType.MINECART_HOPPER);
		_legacyNames.put("Creeper".toLowerCase(), EntityType.CREEPER);
		_legacyNames.put("Skeleton".toLowerCase(), EntityType.SKELETON);
		_legacyNames.put("Spider".toLowerCase(), EntityType.SPIDER);
		_legacyNames.put("Giant".toLowerCase(), EntityType.GIANT);
		_legacyNames.put("Zombie".toLowerCase(), EntityType.ZOMBIE);
		_legacyNames.put("Slime".toLowerCase(), EntityType.SLIME);
		_legacyNames.put("Ghast".toLowerCase(), EntityType.GHAST);
		_legacyNames.put("PigZombie".toLowerCase(), EntityType.PIG_ZOMBIE);
		_legacyNames.put("Enderman".toLowerCase(), EntityType.ENDERMAN);
		_legacyNames.put("CaveSpider".toLowerCase(), EntityType.CAVE_SPIDER);
		_legacyNames.put("Silverfish".toLowerCase(), EntityType.SILVERFISH);
		_legacyNames.put("Blaze".toLowerCase(), EntityType.BLAZE);
		_legacyNames.put("LavaSlime".toLowerCase(), EntityType.MAGMA_CUBE);
		_legacyNames.put("EnderDragon".toLowerCase(), EntityType.ENDER_DRAGON);
		_legacyNames.put("WitherBoss".toLowerCase(), EntityType.WITHER);
		_legacyNames.put("Bat".toLowerCase(), EntityType.BAT);
		_legacyNames.put("Witch".toLowerCase(), EntityType.WITCH);
		_legacyNames.put("Endermite".toLowerCase(), EntityType.ENDERMITE);
		_legacyNames.put("Guardian".toLowerCase(), EntityType.GUARDIAN);
		_legacyNames.put("Shulker".toLowerCase(), EntityType.SHULKER);
		_legacyNames.put("Pig".toLowerCase(), EntityType.PIG);
		_legacyNames.put("Sheep".toLowerCase(), EntityType.SHEEP);
		_legacyNames.put("Cow".toLowerCase(), EntityType.COW);
		_legacyNames.put("Chicken".toLowerCase(), EntityType.CHICKEN);
		_legacyNames.put("Squid".toLowerCase(), EntityType.SQUID);
		_legacyNames.put("Wolf".toLowerCase(), EntityType.WOLF);
		_legacyNames.put("MushroomCow".toLowerCase(), EntityType.MUSHROOM_COW);
		_legacyNames.put("SnowMan".toLowerCase(), EntityType.SNOWMAN);
		_legacyNames.put("Ozelot".toLowerCase(), EntityType.OCELOT);
		_legacyNames.put("VillagerGolem".toLowerCase(), EntityType.IRON_GOLEM);
		_legacyNames.put("EntityHorse".toLowerCase(), EntityType.HORSE);
		_legacyNames.put("Rabbit".toLowerCase(), EntityType.RABBIT);
		_legacyNames.put("PolarBear".toLowerCase(), EntityType.POLAR_BEAR);
		_legacyNames.put("Villager".toLowerCase(), EntityType.VILLAGER);
		_legacyNames.put("EnderCrystal".toLowerCase(), EntityType.ENDER_CRYSTAL);

		_legacyNames.put("ThrownPotion".toLowerCase(), EntityType.SPLASH_POTION);
		_legacyNames.put("ThrownEgg".toLowerCase(), EntityType.EGG);
		_legacyNames.put("MinecartSpawner".toLowerCase(), EntityType.MINECART_MOB_SPAWNER);
		_legacyNames.put("AreaEffectCloud".toLowerCase(), EntityType.AREA_EFFECT_CLOUD);

		_legacyNames.put("TippedArrow".toLowerCase(), EntityType.ARROW);

		List<String> entityNames = new ArrayList<String>();
		List<String> livingEntityNames = new ArrayList<String>();
		for (EntityType type : EntityType.values()) {
			String name = getSimpleName(type);
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
		name = name.toLowerCase();
		EntityType e = _legacyNames.get(name);
		if (e != null) {
			return e;
		}
		if (name.startsWith("minecraft:")) {
			name = name.substring(10);
		}
		return EntityType.fromName(name);
	}

	public static String getName(EntityType type) {
		return "minecraft:" + type.getName();
	}

	public static String getSimpleName(EntityType type) {
		return type.getName();
	}

	public static List<String> getNames() {
		return _entityNames;
	}

	public static List<String> getNames(Collection<EntityType> types) {
		ArrayList<String> names = new ArrayList<String>(types.size());
		for (EntityType type : types) {
			names.add(getSimpleName(type));
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
