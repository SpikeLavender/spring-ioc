package com.ispring.context.helper;

import com.ispring.context.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Map;

public class IoCHelper {
	static {
		Map<Class<?>, Object> map = BeanHelper.getBeanMap();
		if (map != null) {
			for (Map.Entry<Class<?>, Object> entry : map.entrySet()) {
				Class<?> cls = entry.getKey();
				Object instance = entry.getValue();
				if (instance != null) {
					Field[] fields = cls.getDeclaredFields();
					if (fields.length > 0) {
						for (Field field : fields) {
							if (field.isAnnotationPresent(Autowired.class)) {
								//cls.getAnnotation(Autowired.class).required()
								String name = field.getType().getName();
								field.setAccessible(true);
								try {
									field.set(instance, map.get(Class.forName(name)));
									BeanHelper.putBean(cls, instance);
								} catch (IllegalAccessException | ClassNotFoundException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}
}
