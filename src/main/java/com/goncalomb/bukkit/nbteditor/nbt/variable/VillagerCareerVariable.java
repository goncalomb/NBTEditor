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

package com.goncalomb.bukkit.nbteditor.nbt.variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public final class VillagerCareerVariable extends NBTGenericVariable2X {

	private static final class CareerData {
		public final int professionId;
		public final int careerId;
		public CareerData(int professionId, int careerId) {
			this.professionId = professionId;
			this.careerId = careerId;
		}
	}

	private static HashMap<String, CareerData> _careers = new HashMap<String, CareerData>();
	private static List<String> _careerNames;

	static {
		_careers.put("Farmer_Any", new CareerData(0, 0));
		_careers.put("Farmer_Farmer", new CareerData(0, 1));
		_careers.put("Farmer_Fisherman", new CareerData(0, 2));
		_careers.put("Farmer_Shepherd", new CareerData(0, 3));
		_careers.put("Farmer_Fletcher", new CareerData(0, 4));
		_careers.put("Librarian_Any", new CareerData(1, 0));
		_careers.put("Librarian_Librarian", new CareerData(1, 1));
		_careers.put("Priest_Any", new CareerData(2, 0));
		_careers.put("Priest_Cleric", new CareerData(2, 1));
		_careers.put("Blacksmith_Any", new CareerData(3, 0));
		_careers.put("Blacksmith_Armorer", new CareerData(3, 1));
		_careers.put("Blacksmith_WeaponSmith", new CareerData(3, 2));
		_careers.put("Blacksmith_ToolSmith", new CareerData(3, 3));
		_careers.put("Butcher_Any", new CareerData(4, 0));
		_careers.put("Butcher_Butcher", new CareerData(4, 1));
		_careers.put("Butcher_Leatherworker", new CareerData(4, 2));
		_careerNames = new ArrayList<String>(_careers.keySet());
		Collections.sort(_careerNames, String.CASE_INSENSITIVE_ORDER);
		_careerNames = Collections.unmodifiableList(_careerNames);
	}

	public VillagerCareerVariable() {
		super("Profession", "Career");
	}

	@Override
	boolean set(NBTTagCompound data, String value, Player player) {
		CareerData career = _careers.get(value);
		if (career != null) {
			data.setInt(_nbtKey, career.professionId);
			data.setInt(_nbtKey2, career.careerId);
			if (!data.hasKey("CareerLevel")) {
				data.setInt("CareerLevel", 1);
			}
			return true;
		}
		return false;
	}

	@Override
	String get(NBTTagCompound data) {
		if (!data.hasKey(_nbtKey)) {
			if (data.hasKey(_nbtKey2)) {
				return "Invalid";
			}
			return null;
		}
		int pId = data.getInt(_nbtKey);
		int cId = data.getInt(_nbtKey2);
		for (Entry<String, CareerData> entry: _careers.entrySet()) {
			CareerData career = entry.getValue();
			if (career.professionId == pId && career.careerId == cId) {
				return entry.getKey();
			}
		}
		return "Unknown";
	}

	@Override
	String getFormat() {
		return "A villager's profession/career. Use tab completion to view possible values.";
	}

	@Override
	public List<String> getPossibleValues() {
		return _careerNames;
	}

}
