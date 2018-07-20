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
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;

public class MaterialMap {

	private static NamingMap<Material> _materials = new NamingMap<Material>();
	private static List<String> _materialBlockNames = new ArrayList<String>();
	private static List<String> _materialNames;

	static {
		for (Material material : Material.values()) {
			String name = material.getKey().toString();
			if (name != null) {
				_materials.put(name, material);
				if (material.isBlock()) {
					_materialBlockNames.add(name);
				}
			}
		}
		Collections.sort(_materialBlockNames, String.CASE_INSENSITIVE_ORDER);
		_materialBlockNames = Collections.unmodifiableList(_materialBlockNames);
		_materialNames = new ArrayList<String>(_materials.names());
		Collections.sort(_materialNames, String.CASE_INSENSITIVE_ORDER);
		_materialNames = Collections.unmodifiableList(_materialNames);
	}

	public static Material getByName(String name) {
		return _materials.getByName(name);
	}

	public static String getName(Material material) {
		return _materials.getName(material);
	}

	public static List<String> getBlockNames() {
		return _materialBlockNames;
	}

	public static List<String> getNames() {
		return _materialNames;
	}

	private MaterialMap() { }

}
