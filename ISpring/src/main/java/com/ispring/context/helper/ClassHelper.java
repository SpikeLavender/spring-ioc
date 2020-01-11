package com.ispring.context.helper;

import com.ispring.context.annotation.Controller;
import com.ispring.context.annotation.Service;
import com.ispring.context.annotation.Transactional;
import com.ispring.context.util.ClassUtil;
import com.ispring.context.util.PropertiesUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * 类加载器助手类
 */
public class ClassHelper {
	public static String packageName = PropertiesUtil.getProperties("base_package");

	//加载的所有文件

	public static Set<Class<?>> CLASS_LIST;

	//加载所有文件的方法
	static {
		CLASS_LIST = ClassUtil.loadClassPackage(packageName);
	}


	public static Set<Class<?>> loadClassByAnnotation(Class<? extends Annotation> annotation) {
		Set<Class<?>> classes = new HashSet<>();
		if (CLASS_LIST != null && CLASS_LIST.size() > 0) {
			for (Class<?> cls : CLASS_LIST) {
				if (cls.isAnnotationPresent(annotation)) {
					classes.add(cls);
				}
			}
		}
		return classes;
	}

	/**
	 * 加载Service注解类
	 * @return
	 */
	public static Set<Class<?>> loadServiceClasses() {
		Set<Class<?>> classes = new HashSet<>();
		if (CLASS_LIST != null && CLASS_LIST.size() > 0) {
			for (Class<?> cls : CLASS_LIST) {
				if (cls.isAnnotationPresent(Service.class)) {
					classes.add(cls);
				}
			}
		}
		return classes;
	}

	/**
	 * 加载Controller注解类
	 * @return
	 */
	public static Set<Class<?>> loadControllerClasses() {
		Set<Class<?>> classes = new HashSet<>();
		if (CLASS_LIST != null && CLASS_LIST.size() > 0) {
			for (Class<?> cls : CLASS_LIST) {
				if (cls.isAnnotationPresent(Controller.class)) {
					classes.add(cls);
				}
			}
		}
		return classes;
	}

	/**
	 * 加载Transactional注解类
	 * @return
	 */
	public static Set<Class<?>> loadTransactionalClasses() {
		Set<Class<?>> classes = new HashSet<>();
		if (CLASS_LIST != null && CLASS_LIST.size() > 0) {
			for (Class<?> cls : CLASS_LIST) {
				if (cls.isAnnotationPresent(Transactional.class)) {
					classes.add(cls);
				}
			}
		}
		return classes;
	}

	/**
	 * 根据父类加载所有子类或接口实现类
	 * @param superClass
	 * @return
	 */
	public static Set<Class<?>> loadClassBySuperClass(Class<?> superClass) {
		Set<Class<?>> classes = new HashSet<>();
		if (CLASS_LIST != null && CLASS_LIST.size() > 0) {
			for (Class<?> cls : CLASS_LIST) {
				if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)) {
					classes.add(cls);
				}
			}
		}
		return classes;
	}
}

