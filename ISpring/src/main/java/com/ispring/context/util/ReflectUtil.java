package com.ispring.context.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射工具类
 */
public class ReflectUtil {

	//创建指定类型实例
	public static Object newInstance(Class<?> cls) {
		Object obj = new Object();
		try {
			obj = cls.newInstance();
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
		return obj;
	}

	//调用实例方法
	public static void invokeMethod(Object obj, Method method, Object... args) {
		try {
			method.invoke(obj, args);
		}  catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	//给属性赋值
	public static void setField(Object obj, Field field, Object fieldValue) {
		try {
			if (field != null) {
				field.setAccessible(true);
				field.set(obj, fieldValue);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
