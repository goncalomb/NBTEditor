package com.goncalomb.bukkit.bkglib.namemaps;

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
