package com.ispring.context.util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

	private static Properties properties;

	static {
		properties = new Properties();
		try {
			properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("base.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 指定属性 KEY 获取属性 VALUE
	 * @param key
	 * @return
	 */
	public static String getProperties(String key) {
		return properties.getProperty(key);
	}


}
