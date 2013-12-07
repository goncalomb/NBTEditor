/*
 * Copyright (C) 2013 - Gonçalo Baltazar <http://goncalomb.com>
 *
 * This file is part of BKgLib.
 *
 * BKgLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BKgLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BKgLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.bkglib.reflect;

import java.lang.reflect.Method;

public class NBTBaseWrapper {
	
	private static boolean _isPrepared = false;
	
	protected static Class<?> _nbtBaseClass;
	protected static Class<?> _nbtTagCompoundClass;
	protected static Class<?> _nbtTagListClass;
	
	private static Method _getName;
	private static Method _clone;
	
	protected Object _nbtBaseObject;
	
	public static final void prepareReflection() {
		if (!_isPrepared) {
			_nbtBaseClass = BukkitReflect.getMinecraftClass("NBTBase");
			_nbtTagCompoundClass = BukkitReflect.getMinecraftClass("NBTTagCompound");
			_nbtTagListClass = BukkitReflect.getMinecraftClass("NBTTagList");
			
			try {
				_getName = _nbtBaseClass.getMethod("getName");
				_clone = _nbtBaseClass.getMethod("clone");
				NBTTagCompoundWrapper.prepareReflectionz();
				NBTTagListWrapper.prepareReflectionz();
				NBTTagTypeHandler.prepareReflection();
				NBTUtils.prepareReflection();
			} catch (Exception e) {
				_nbtBaseClass = null;
				throw new Error("Error while preparing NBTWrapper classes.", e);
			}
			
			_isPrepared = true;
		}
	}
	
	protected static final NBTBaseWrapper wrap(Object nbtBaseObject) {
		if (_nbtTagCompoundClass.isInstance(nbtBaseObject)) {
			return new NBTTagCompoundWrapper(nbtBaseObject);
		} else if (_nbtTagListClass.isInstance(nbtBaseObject)) {
			return new NBTTagListWrapper(nbtBaseObject);
		} else {
			return new NBTBaseWrapper(nbtBaseObject);
		}
	}
	
	// Helper method for NBTTagCompoundWrapper.merge(NBTTagCompoundWrapper).
	protected static final String getName(Object nbtBaseObject) {
		return (String) BukkitReflect.invokeMethod(nbtBaseObject, _getName);
	}
	
	// Helper method for NBTTagCompoundWrapper.merge(NBTTagCompoundWrapper).
	protected static final Object clone(Object nbtBaseObject) {
		return BukkitReflect.invokeMethod(nbtBaseObject, _clone);
	}
	
	protected NBTBaseWrapper(Object nbtBaseObject) {
		_nbtBaseObject = nbtBaseObject;
	}
	
	protected final Object invokeMethod(Method method, Object... args) {
		return BukkitReflect.invokeMethod(_nbtBaseObject, method, args);
	}
	
	public NBTBaseWrapper clone() {
		return wrap(invokeMethod(_clone));
	}
	
}
