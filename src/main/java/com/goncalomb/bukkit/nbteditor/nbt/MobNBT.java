/*
 * Copyright (C) 2013 - Gonçalo Baltazar <http://goncalomb.com>
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

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.bkglib.reflect.NBTTagListWrapper;
import com.goncalomb.bukkit.bkglib.reflect.NBTUtils;
import com.goncalomb.bukkit.nbteditor.nbt.attributes.AttributeContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.FloatVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.ShortVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.StringVariable;

public class MobNBT extends EntityNBT {
	
	private ItemStack[] _equipment;
	
	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Mob");
		variables.add("health", new FloatVariable("HealF", 0.0f));
		variables.add("old-health", new ShortVariable("Health", (short) 0));
		variables.add("attack-time", new ShortVariable("AttackTime"));
		variables.add("hurt-time", new ShortVariable("HurtTime"));
		variables.add("death-time", new ShortVariable("DeathTime"));
		variables.add("pick-loot", new BooleanVariable("CanPickUpLoot"));
		variables.add("persistent", new BooleanVariable("PersistenceRequired"));
		variables.add("name", new StringVariable("CustomName"));
		variables.add("name-visible", new BooleanVariable("CustomNameVisible"));
		EntityNBTVariableManager.registerVariables(MobNBT.class, variables);
	}
	
	public void setEquipment(ItemStack hand, ItemStack feet, ItemStack legs, ItemStack chest, ItemStack head) {
		if (hand == null && feet == null && legs == null && chest == null && head == null) {
			clearEquipment();
			return;
		}
		_equipment = new ItemStack[] { hand, feet, legs, chest, head };
		Object[] equipmentData = new Object[5];
		for (int i = 0; i < 5; ++i) {
			if (_equipment[i] != null) {
				equipmentData[i] = NBTUtils.nbtTagCompoundFromItemStack(_equipment[i]);
			} else {
				equipmentData[i] = new NBTTagCompoundWrapper();
			}
		}
		_data.setList("Equipment", equipmentData);
	}
	
	public ItemStack[] getEquipment() {
		if (_equipment == null) {
			_equipment = new ItemStack[5];
			if (_data.hasKey("Equipment")) {
				Object[] equipmentData = _data.getListAsArray("Equipment");
				for (int i = 0; i < 5; ++i) {
					_equipment[i] = NBTUtils.itemStackFromNBTTagCompound((NBTTagCompoundWrapper) equipmentData[i]);
				}
			}
		}
		return _equipment;
	}
	
	public void clearEquipment() {
		_data.remove("Equipment");
		_equipment = null;
	}
	
	public void setDropChances(float hand, float feet, float legs, float chest, float head) {
		_data.setList("DropChances", hand, feet, legs, chest, head);
	}
	
	public float[] getDropChances() {	
		if (_data.hasKey("DropChances")) {
			return ArrayUtils.toPrimitive(Arrays.copyOfRange(_data.getListAsArray("DropChances"), 0, 5, Float[].class));
		}
		return null;
	}
	
	public void clearDropChances() {
		_data.remove("DropChances");
	}
	
	public void setEffectsFromPotion(ItemStack potion) {
		if (potion != null) {
			NBTTagListWrapper effects = NBTUtils.effectsListFromPotion(potion);
			if (effects != null) {
				_data.set("ActiveEffects", effects);
				return;
			}
		} else {
			_data.remove("ActiveEffects");
		}
	}
	
	public ItemStack getEffectsAsPotion() {
		if (_data.hasKey("ActiveEffects")) {
			return NBTUtils.getGenericPotionFromEffectList(_data.getList("ActiveEffects"));
		}
		return null;
	}
	
	public AttributeContainer getAttributes() {
		if (_data.hasKey("Attributes")) {
			return AttributeContainer.fromNBT(_data.getList("Attributes"));
		}
		return new AttributeContainer();
	}
	
	public void setAttributes(AttributeContainer container) {
		if (container == null || container.size() == 0) {
			_data.remove("Attributes");
		} else {
			_data.setList("Attributes", container.toNBT());
		}
	}
	
}
