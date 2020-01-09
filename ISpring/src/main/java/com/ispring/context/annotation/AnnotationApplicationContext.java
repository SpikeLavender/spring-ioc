package com.ispring.context.annotation;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.HashSet;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationApplicationContext implements ApplicationContext {
	private Map<String, Object> map = new ConcurrentHashMap<>(64);

//	private String packageName;
//
//	public AnnotationApplicationContext(String packageName) {
//		this.packageName = packageName;
//	}


	public AnnotationApplicationContext(String packageName) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException {
		//扫描包下面的类是否有注解
		String path = packageName.replace(".","/");
		//项目路径
		URL url = this.getClass().getClassLoader().getResource(path);
		//判断路径是否存在
		if(url != null) {
			File file = new File(url.getPath());
			//判断是否是目录
			Map<String, Class> nameMap = new ConcurrentHashMap<>(64);
			if(file.isDirectory()) {
				File[] listFiles = file.listFiles();
				if (null == listFiles) {
					return;
				}
				for (File file2 : listFiles) {
					Set<String> fileNames = getFiles(file2.getAbsolutePath());
					for (String fileName : fileNames) {
						//String fileName = file2.getName();
						String split = fileName.split(packageName)[1].split(".class")[0];

						String className = packageName + split.replace("\\", ".");
						//System.out.println(className);
						Class cls = Class.forName(className);

						//判断是否有Component注解

						if(cls.isAnnotationPresent(Component.class) || cls.isAnnotationPresent(Service.class)
								|| cls.isAnnotationPresent(Controller.class) || cls.isAnnotationPresent(Repository.class)) {
							Object obj = cls.newInstance();
							//获取实例名
							String id;
							if (cls.isAnnotationPresent(Component.class)) {
								id = ((Component)cls.getAnnotation(Component.class)).value();
							} else if (cls.isAnnotationPresent(Service.class)) {
								id = ((Service)cls.getAnnotation(Service.class)).value();
							} else if (cls.isAnnotationPresent(Controller.class)) {
								id = ((Controller)cls.getAnnotation(Controller.class)).value();
							} else if (cls.isAnnotationPresent(Repository.class)) {
								id = ((Repository)cls.getAnnotation(Repository.class)).value();
							} else {
								throw new RuntimeException("tt");
							}

							if("".equals(id)) {
								id = cls.getSimpleName();
								id = id.substring(0, 1).toLowerCase() + id.substring(1);
							}

							map.put(id, obj);
							nameMap.put(id, cls);
						}
					}
				}
			}

			for (Map.Entry<String, Class> entry : nameMap.entrySet()) {

				String id = entry.getKey();
				Class cls = entry.getValue();

				//注入属性值
				Field[] fields = cls.getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					Autowired autowired = field.getAnnotation(Autowired.class);
					String name = field.getName();
					if (autowired != null && autowired.required()) {
						field.set(map.get(id), map.get(name));
					}
				}

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
	}

	private void transactionalHandler(String id, Transactional annotation) {
		if (annotation != null) {
			String value = annotation.value();
			Object jdkProxy = ProxyFactory.getInstance().getJdkProxy(map.get(id), map.get(value));
			map.put(id, jdkProxy);
		}
	}

	private Set<String> getFiles(String path) {
		Set<String> fileName = new HashSet<>();
		File file = new File(path);
		// 取 文件/文件夹
		File[] files = file.listFiles();
		if (null != files) {
			for (File f : files) {
				if (f.isDirectory()) {
					fileName.addAll(getFiles(f.getAbsolutePath()));
				} else if (f.isFile()) {
					fileName.add(f.getPath());
				}
			}
		}
		return fileName;
	}


	@Override
	public Object getBean(String beanName) {
		return map.get(beanName);
	}
}
