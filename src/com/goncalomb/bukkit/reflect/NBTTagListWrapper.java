package com.goncalomb.bukkit.reflect;

import java.lang.reflect.Method;

public final class NBTTagListWrapper extends NBTBaseWrapper {
	
	private static Method _get;
	private static Method _add;
	private static Method _size;
	
	static void prepareReflectionz() throws SecurityException, NoSuchMethodException {
		_get = _nbtTagListClass.getMethod("get", int.class);
		_add = _nbtTagListClass.getMethod("add", _nbtBaseClass);
		_size = _nbtTagListClass.getMethod("size");
	}
	
	public NBTTagListWrapper() {
		super(BukkitReflect.newInstance(_nbtTagListClass));
	}
	
	NBTTagListWrapper(Object nbtTagListObject) {
		super(nbtTagListObject);
	}
	
	public NBTTagListWrapper(Object... values) {
		super(BukkitReflect.newInstance(_nbtTagListClass));
		for (Object value : values) {
			add(value);
		}
	}
	
	public Object get(int index) {
		return NBTTagTypeHandler.getObjectFromTag(invokeMethod(_get, index));
	}
	
	public void add(Object value) {
		invokeMethod(_add, NBTTagTypeHandler.getTagFromObject(value));
	}
	
	public int size() {
		return (Integer) invokeMethod(_size);
	}
	
	public Object[] getAsArray() {
		int length = size();
		Object[] result = new Object[length];
		for (int i = 0; i < length; ++i) {
			result[i] = get(i);
		}
		return result;
	}
	
	@Override
	public NBTTagListWrapper clone() {
		return (NBTTagListWrapper) super.clone();
	}
	
}
