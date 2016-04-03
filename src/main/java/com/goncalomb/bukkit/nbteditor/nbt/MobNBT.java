/*
 * Copyright (C) 2013-2016 Gonçalo Baltazar <me@goncalomb.com>
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

import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.reflect.NBTTagList;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;
import com.goncalomb.bukkit.nbteditor.nbt.attributes.AttributeContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.FloatArrayVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.FloatVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.ShortVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.StringVariable;

public class MobNBT extends EquippableNBT {

	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Mob");
		variables.add("Health", new FloatVariable("Health", 0.0f));
		variables.add("AttackTime", new ShortVariable("AttackTime"));
		variables.add("HurtTime", new ShortVariable("HurtTime"));
		variables.add("DeathTime", new ShortVariable("DeathTime"));
		variables.add("HandDropChances", new FloatArrayVariable("HandDropChances", 2, 0f, 2f));
		variables.add("ArmorDropChances", new FloatArrayVariable("ArmorDropChances", 4, 0f, 2f));
		variables.add("PickLoot", new BooleanVariable("CanPickUpLoot"));
		variables.add("NoAI", new BooleanVariable("NoAI"));
		variables.add("Persistent", new BooleanVariable("PersistenceRequired"));
		variables.add("Name", new StringVariable("CustomName"));
		variables.add("NameVisible", new BooleanVariable("CustomNameVisible"));
		variables.add("LeftHanded", new BooleanVariable("LeftHanded"));
		registerVariables(MobNBT.class, variables);
	}

	public void setEffectsFromPotion(ItemStack potion) {
		if (potion != null) {
			NBTTagList effects = NBTUtils.potionToNBTEffectsList(potion);
			if (effects != null) {
				_data.setList("ActiveEffects", effects);
				return;
			}
		} else {
			_data.remove("ActiveEffects");
		}
	}

	public ItemStack getEffectsAsPotion() {
		if (_data.hasKey("ActiveEffects")) {
			return NBTUtils.potionFromNBTEffectsList(_data.getList("ActiveEffects"));
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

	@Override
	void onUnserialize() {
		super.onUnserialize();
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

}
