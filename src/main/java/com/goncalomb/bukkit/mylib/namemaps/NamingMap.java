/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

final class NamingMap<T> {
	
	private HashMap<String, T> _map = new HashMap<String, T>();
	private HashMap<T, String> _inverse = new HashMap<T, String>();
	
	public boolean put(String name, T value) {
		String nameLower = name.toLowerCase();
		if (!_map.containsKey(nameLower) && !_inverse.containsKey(value)) {
			_map.put(nameLower, value);
			_inverse.put(value, name);
			return true;
		}
		return false;
	}
	
	public T getByName(String name) {
		return _map.get(name.toLowerCase());
	}
	
	public String getName(T value) {
		return _inverse.get(value);
	}
	
	public Collection<String> names() {
		return Collections.unmodifiableCollection(_inverse.values());
	}
	
	public Set<T> values() {
		return Collections.unmodifiableSet(_inverse.keySet());
	}
	
}
