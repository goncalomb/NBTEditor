package com.goncalomb.bukkit.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.commons.lang.ClassUtils;

final class NBTTagTypeHandler {
	
	private static HashMap<Class<?>, NBTTagTypeHandler> _innerTypeMap;
	private static HashMap<Class<?>, NBTTagTypeHandler> _outerTypeMap;
	
	private Class<?> _class;
	private Constructor<?> _contructor;
	private Field _data;
	private Class<?> _dataType;
	
	public static void prepareReflection() throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		_innerTypeMap = new HashMap<Class<?>, NBTTagTypeHandler>();
		_outerTypeMap = new HashMap<Class<?>, NBTTagTypeHandler>();
		registerNew("NBTTagByte");
		registerNew("NBTTagShort");
		registerNew("NBTTagInt");
		registerNew("NBTTagLong");
		registerNew("NBTTagFloat");
		registerNew("NBTTagDouble");
		registerNew("NBTTagString");
	}
	
	private static void registerNew(String tagClassName) throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		NBTTagTypeHandler handler = new NBTTagTypeHandler(tagClassName);
		_innerTypeMap.put((handler._dataType.isPrimitive() ? ClassUtils.primitiveToWrapper(handler._dataType) : handler._dataType), handler);
		_outerTypeMap.put(handler._class, handler);
	}
	
	public static Object getTagFromObject(Object object) {
		if (object instanceof NBTBaseWrapper) {
			return ((NBTBaseWrapper) object)._nbtBaseObject;
		} else {
			NBTTagTypeHandler handler = _innerTypeMap.get(object.getClass());
			if (handler != null) {
				return handler.wrapWithTag(object);
			} else {
				throw new Error(object.getClass().getSimpleName() + " is not a valid NBTTag type.");
			}
		}
	}
	
	public static Object getObjectFromTag(Object tagObject) {
		if (tagObject == null) {
			return null;
		}
		NBTTagTypeHandler handler = _outerTypeMap.get(tagObject.getClass());
		if (handler != null) {
			return handler.unwrapTag(tagObject);
		} else {
			return NBTBaseWrapper.wrap(tagObject);
		}
	}
	
	private NBTTagTypeHandler(String tagClassName) throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		_class = BukkitReflect.getMinecraftClass(tagClassName);
		_data = _class.getDeclaredField("data");
		_dataType = _data.getType();
		_contructor = _class.getConstructor(String.class, _dataType);
	}
	
	private Object wrapWithTag(Object innerObject) {
		return BukkitReflect.newInstance(_contructor, "", innerObject);
	}
	
	private Object unwrapTag(Object tagObject) {
		try {
			return _data.get(tagObject);
		} catch (Exception e) {
			throw new Error("Error while acessing data from " + _class.getSimpleName() + ".", e);
		}
	}
	
}
