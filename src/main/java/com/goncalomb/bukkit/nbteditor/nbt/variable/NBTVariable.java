/*
 * Copyright (C) 2013, 2014 - Gonçalo Baltazar <http://goncalomb.com>
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

import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompound;

public final class NBTVariable {
	
	private String _name;
	private NBTGenericVariable _generic;
	private NBTTagCompound _data;
	
	NBTVariable(String name, NBTGenericVariable generic, NBTTagCompound data) {
		_name = name;
		_generic = generic;
		_data = data;
	}
	
	public String getName() {
		return _name;
	}
	
	public boolean setValue(String value) {
		return _generic.set(_data, value);
	}
	
	public String getValue() {
		return _generic.get(_data);
	}
	
	public void clear() {
		_generic.clear(_data);
	}
	
	public String getFormat() {
		return _generic.getFormat();
	}
	
}
