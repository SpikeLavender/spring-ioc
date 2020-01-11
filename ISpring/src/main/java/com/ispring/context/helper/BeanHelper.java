package com.ispring.context.helper;

import com.ispring.context.util.ReflectUtil;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BeanHelper {

	//Bean反射类型和Bean实例之间的映射关系
	private static Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

	static {
		Set<Class<?>> classes = ClassHelper.CLASS_LIST;

		if (classes != null) {
			for (Class<?> cls : classes) {
				//创建每个类型的实例，存入类型-实例映射Map
				Object instance = ReflectUtil.newInstance(cls);
				beanMap.put(cls, instance);
			}
		}

	}


	//获取映射Map
	public static Map<Class<?>, Object> getBeanMap() {
		return beanMap;
	}

	//根据类型获取值（Bean实例）
	public static Object getInstance(Class<?> cls) {
		return beanMap.get(cls);
	}

	public static void putBean(Class<?> targetClass, Object proxy) {
		beanMap.put(targetClass, proxy);
	}


}
