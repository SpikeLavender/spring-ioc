package com.ispring.context.factory;


import com.ispring.context.annotation.*;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory {
	/** Map of bean definition objects, keyed by bean name. */
	private final Map<String, Object> beanDefinitionMap = new ConcurrentHashMap<>(256);

	private Set<String> classNames = new HashSet<>();

	public DefaultBeanFactory() {

	}

	/**
	 * 根据配置文件扫描待处理的类
	 */
	public void scanBean(String... packages)  {
		//扫描包下面的类是否有注解
		for (String aPackage : packages) {
			String path = aPackage.replace(".","/");
			//项目路径
			URL url = this.getClass().getClassLoader().getResource(path);
			//判断路径是否存在
			if(url != null) {
				File file = new File(url.getPath());
				//判断是否是目录
				if (file.isDirectory()) {
					//fileNamesMap.put(packageName, getFiles(file.getAbsolutePath()));
					classNames.addAll(getClassNames(aPackage, file.getAbsolutePath()));
				}
			}
		}

	}


	/**
	 * 工厂实例化bean
	 * @throws ReflectiveOperationException
	 */

	public void createBeanInstance() throws ReflectiveOperationException {
		for (String className : classNames) {
			//System.out.println(className);
			Class cls = Class.forName(className);

			//判断是否有Component注解

			if (cls.isAnnotationPresent(Component.class) || cls.isAnnotationPresent(Service.class)
					|| cls.isAnnotationPresent(Controller.class) || cls.isAnnotationPresent(Repository.class)) {
				Object obj = cls.newInstance();
				//获取实例名
				String id;
				if (cls.isAnnotationPresent(Component.class)) {
					id = ((Component) cls.getAnnotation(Component.class)).value();
				} else if (cls.isAnnotationPresent(Service.class)) {
					id = ((Service) cls.getAnnotation(Service.class)).value();
				} else if (cls.isAnnotationPresent(Controller.class)) {
					id = ((Controller) cls.getAnnotation(Controller.class)).value();
				} else if (cls.isAnnotationPresent(Repository.class)) {
					id = ((Repository) cls.getAnnotation(Repository.class)).value();
				} else {
					throw new RuntimeException("tt");
				}

				if ("".equals(id)) {
					id = cls.getSimpleName();
					id = id.substring(0, 1).toLowerCase() + id.substring(1);
				}

				beanDefinitionMap.put(id, obj);
			}
		}
	}

	//注入属性值
	public void setBeanAttribute() throws ReflectiveOperationException {
		for (Map.Entry<String, Object> entry : beanDefinitionMap.entrySet()) {

			String id = entry.getKey();
			Class cls = entry.getValue().getClass();

			//注入属性值
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Autowired autowired = field.getAnnotation(Autowired.class);
				String name = field.getName();
				if (autowired != null && autowired.required()) {
					field.set(beanDefinitionMap.get(id), beanDefinitionMap.get(name));
				}
			}
		}
	}

	public void afterBeanCreate() {
		for (Map.Entry<String, Object> entry : beanDefinitionMap.entrySet()) {

			String id = entry.getKey();
			Class cls = entry.getValue().getClass();

			if (cls.isAnnotationPresent(Transactional.class)) {
				Transactional annotation = (Transactional) cls.getAnnotation(Transactional.class);
				transactionalHandler(id, annotation);
			}

			Method[] methods = cls.getDeclaredMethods();
			for (Method method : methods) {
				Transactional annotation = method.getAnnotation(Transactional.class);
				transactionalHandler(id, annotation);
			}
		}
	}


	private void initializeBean(String id, final Object bean) {

	}

	private void transactionalHandler(String id, Transactional annotation) {
		if (annotation != null) {
			String value = annotation.value();
			value = value.isEmpty() ? "transactionManager" : value;
			Object o = beanDefinitionMap.get(id);

			Object proxy;
			if (annotation.proxyMethod().equalsIgnoreCase("JDK")) {
				proxy = ProxyFactory.getInstance().getJdkProxy(beanDefinitionMap.get(id), beanDefinitionMap.get(value));
			} else {
				proxy = ProxyFactory.getInstance().getCglibProxy(beanDefinitionMap.get(id), beanDefinitionMap.get(value));
			}
			beanDefinitionMap.put(id, proxy);
		}
	}

	private Set<String> getClassNames(String packageName, String path) {
		Set<String> fileName = new HashSet<>();
		File file = new File(path);
		// 取 文件/文件夹
		File[] files = file.listFiles();
		if (null != files) {
			for (File f : files) {
				if (f.isDirectory()) {
					fileName.addAll(getClassNames(packageName, f.getAbsolutePath()));
				} else if (f.isFile()) {
					String split = f.getPath().split(packageName)[1].split(".class")[0];
					String className = packageName + split.replace("\\", ".");
					fileName.add(className);
				}
			}
		}
		return fileName;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String id)  {

		Object resolved = beanDefinitionMap.get(id);

		return (T) resolved;
	}

}
