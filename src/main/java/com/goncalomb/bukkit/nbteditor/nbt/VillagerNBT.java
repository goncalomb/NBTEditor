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

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;
import com.goncalomb.bukkit.nbteditor.nbt.variable.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.VillagerCareerVariable;

public class VillagerNBT extends BreedNBT {

	private ArrayList<VillagerNBTOffer> _offers;

	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Villager");
		variables.add("Career", new VillagerCareerVariable());
		variables.add("CareerLevel", new IntegerVariable("CareerLevel", 0));
		variables.add("Willing", new BooleanVariable("Willing")); // XXX: not effect?
		// TODO: add villager inventory
		registerVariables(VillagerNBT.class, variables);
	}

	public void clearOffers() {
		_data.remove("Offers");
		_offers = null;
	}

	public void addOffer(VillagerNBTOffer offer) {
		NBTTagCompound offers = _data.getCompound("Offers");
		if (offers == null) {
			offers = new NBTTagCompound();
			_data.setCompound("Offers", offers);
		}
		NBTTagList recipes = offers.getList("Recipes");
		if (recipes == null) {
			recipes = new NBTTagList();
			offers.setList("Recipes", recipes);
		}
		recipes.add(offer.getCompound());
		if (_offers != null) {
			_offers.add(offer);
		}
	}

	public List<VillagerNBTOffer> getOffers() {
		if (_offers == null) {
			_offers = new ArrayList<VillagerNBTOffer>();
			if (_data.hasKey("Offers")) {
				NBTTagCompound offers = _data.getCompound("Offers");
				if (offers.hasKey("Recipes")) {
					Object[] recipes = offers.getListAsArray("Recipes");
					for (Object recipe : recipes) {
						_offers.add(new VillagerNBTOffer((NBTTagCompound)recipe));
					}
				}
			}
		}
		return _offers;
	}

}
