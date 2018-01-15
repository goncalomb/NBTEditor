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

package com.goncalomb.bukkit.nbteditor.nbt.attributes;

import java.util.HashMap;

public enum AttributeType {
	// Mobs
	MAX_HEALTH("MaxHealth", "generic.maxHealth", 0.0, Double.MAX_VALUE),
	FOLLOW_RANGE("FollowRange", "generic.followRange", 0.0, 2048),
	KNOCKBACK_RESISTANCE("KnockbackResistance", "generic.knockbackResistance", 0.0, 1.0),
	MOVEMENT_SPEED("MovementSpeed", "generic.movementSpeed", 0.0, Double.MAX_VALUE),
	ATTACK_DAMAGE("AttackDamage", "generic.attackDamage", 0.0, Double.MAX_VALUE),
	ARMOR("Armor", "generic.armor", 0.0, 30.0),
	ARMOR_TOUGHNESS("ArmorToughness", "generic.armorToughness", 0.0, 20.0),
	// Player
	ATTACK_SPEED("AttackSpeed", "generic.attackSpeed", 0.0, 1024.0),
	LUCK("Luck", "generic.luck", -1024.0, 1024.0),
	// Horses
	JUMP_STRENGTH("JumpStrength", "horse.jumpStrength", 0.0, 2),
	// Zombies
	SPAWN_REINFORCEMENTS("SpawnReinforcements", "zombie.spawnReinforcements", 0.0, 1.0);

	private static final HashMap<String, AttributeType> _attributes = new HashMap<String, AttributeType>();
	private static final HashMap<String, AttributeType> _attributesInternal = new HashMap<String, AttributeType>();

	private String _name;
	String _internalName;
	private double _min;
	private double _max;

	static {
		for (AttributeType type : values()) {
			_attributes.put(type._name.toLowerCase(), type);
			_attributesInternal.put(type._internalName, type);
		}
	}

	private AttributeType(String name, String internalName, double min, double max) {
		_name = name;
		_internalName = internalName;
		_min = min;
		_max = max;
	}

	public String getName() {
		return _name;
	}

	public double getMin() {
		return _min;
	}

	public double getMax() {
		return _max;
	}

	public static AttributeType getByName(String name) {
		return _attributes.get(name.toLowerCase());
	}

	static AttributeType getByInternalName(String name) {
		return _attributesInternal.get(name);
	}

	@Override
	public String toString() {
		return _name;
	}

}
