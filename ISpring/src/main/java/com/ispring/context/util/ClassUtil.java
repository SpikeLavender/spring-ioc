package com.ispring.context.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;

public class ClassUtil {

	//获取类加载器
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	//根据类名加载类
	public static Class<?> loadClass(String className) {
		try {
			return Class.forName(className, true, getClassLoader());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	//根据类加载器加载单个文件
	public static void addClass(Set<Class<?>> classes, String className) {
		Class<?> aClass = loadClass(className);
		classes.add(aClass);
	}

	//根据包路径、包名递归加载文件
	public static void addClassReceive(Set<Class<?>> classes, String packagePath, String packageName) {
		File dir = new File(packagePath);
		//读取目录下的所有文件
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return (file.isFile() && file.getName().endsWith(".class") || file.isDirectory());
			}
		});

		if (files != null && files.length > 0) {
			for (File file : files) {
				String fileName = file.getName();
				if (file.isFile()) {
					//是文件
					String className = file.getName().substring(0, file.getName().lastIndexOf("."));
					if (StringUtils.isNotEmpty(packageName)) {
						className = packageName + "." + className;
					}
					addClass(classes, className);
				} else {
					//是目录，递归读取
					String subPackagePath = fileName;
					if (StringUtils.isNotEmpty(subPackagePath)) {
						subPackagePath = packagePath + "/" + subPackagePath;
					}
					String subPackageName = fileName;
					if (StringUtils.isNotEmpty(subPackageName)) {
						subPackageName = packageName + "." + subPackageName;
					}
					addClassReceive(classes, subPackagePath, subPackageName);
				}
			}
		}
	}

	//根据基准包名读取包名下的所有文件
	public static Set<Class<?>> loadClassPackage(String packageName) {
		Set<Class<?>> classes = new HashSet<>();

		if (StringUtils.isNotEmpty(packageName)) {
			try {
				Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".","/"));
				if (urls != null) {
					while (urls.hasMoreElements()) {
						URL url = urls.nextElement();

						if (url != null) {
							String protoType = url.getProtocol();
							if (StringUtils.isNotEmpty(protoType)) {
								if (protoType.equals("file")) {
									String packagePath = url.getPath().replaceAll("%20", " ");
									addClassReceive(classes, packagePath, packageName);
								} else if (protoType.equals("jar")) {
									JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
									if (jarURLConnection != null) {
										Enumeration<JarEntry> entries = jarURLConnection.getJarFile().entries();
										if (entries != null) {
											while (entries.hasMoreElements()) {
												JarEntry jarEntry = entries.nextElement();
												String name = jarEntry.getName();
												if (StringUtils.isNotEmpty(name) && name.endsWith(".class")) {
													String className = name.substring(0, name.lastIndexOf(".")).replaceAll("/", ".");
													addClass(classes, className);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return classes;
	}

}
