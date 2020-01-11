package com.ispring.context.helper;

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
//							if (cls.isAnnotationPresent())
						}
					}
				}
			}
		}
	}
}
