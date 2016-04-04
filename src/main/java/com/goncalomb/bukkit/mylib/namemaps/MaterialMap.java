package com.goncalomb.bukkit.mylib.namemaps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;

import com.goncalomb.bukkit.mylib.reflect.BukkitReflect;

public class MaterialMap {

	private static NamingMap<Material> _materials = new NamingMap<Material>();
	private static List<String> _materialBlockNames = new ArrayList<String>();
	private static List<String> _materialNames;

	static {
		for (Material material : Material.values()) {
			String name = BukkitReflect.getMaterialName(material);
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
