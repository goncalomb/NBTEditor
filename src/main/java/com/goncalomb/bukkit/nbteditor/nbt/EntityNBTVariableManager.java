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

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.EntityType;

import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTVariableContainer;

public final class EntityNBTVariableManager {

	private static HashMap<Class<? extends EntityNBT>, NBTGenericVariableContainer> _entityVariables = new HashMap<Class<? extends EntityNBT>, NBTGenericVariableContainer>();
	private static HashMap<EntityType, NBTGenericVariableContainer> _entityVariablesByType = new HashMap<EntityType, NBTGenericVariableContainer>();
	
	static void registerVariables(Class<? extends EntityNBT> entityClass, NBTGenericVariableContainer variables) {
		_entityVariables.put(entityClass, variables);
	}
	
	static void registerVariables(EntityType entityType, NBTGenericVariableContainer variables) {
		_entityVariablesByType.put(entityType, variables);
	}
	
	static NBTVariableContainer[] getAllVariables(EntityNBT entity) {
		NBTGenericVariableContainer aux;
		ArrayList<NBTVariableContainer> list = new ArrayList<NBTVariableContainer>(3);
		
		if ((aux = _entityVariablesByType.get(entity.getEntityType())) != null) {
			list.add(aux.boundToData(entity._data));
		}
		
		for (Class<?> clazz = entity.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			if ((aux = _entityVariables.get(clazz)) != null) {
				list.add(aux.boundToData(entity._data));
			}
		}
		return list.toArray(new NBTVariableContainer[0]);
	}
	
	static NBTVariable getVariable(EntityNBT entity, String name) {
		NBTGenericVariableContainer aux;
		name = name.toLowerCase();
		
		if ((aux = _entityVariablesByType.get(entity.getEntityType())) != null) {
			if (aux.hasVariable(name)) {
				return aux.getVariable(name, entity._data);
			}
		}
		
		for (Class<?> clazz = entity.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			if ((aux = _entityVariables.get(clazz)) != null) {
				if (aux.hasVariable(name)) {
					return aux.getVariable(name, entity._data);
				}
			}
		}
		return null;
	}

}
