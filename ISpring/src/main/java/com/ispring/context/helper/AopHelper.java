package com.ispring.context.helper;

import com.ispring.context.annotation.Aspect;
import com.ispring.context.core.Proxy;
import com.ispring.context.core.ProxyAspect;
import com.ispring.context.core.ProxyManager;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * AOP代理助手类
 */
public class AopHelper {
	//加载所有代理类和代理目标类并执行代理执行链
	static {
		try {
			//代理类，代理的多个目标类
			Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
			//目标类，目标的多个代理类
			Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);

			if (targetMap != null) {
				for (Map.Entry<Class<?>, List<Proxy>> entry : targetMap.entrySet()) {
					Class<?> targetClass = entry.getKey();
					List<Proxy> proxyList = entry.getValue();
					System.out.println(proxyList.size());
					//创建代理并执行代理执行链（按顺序执行每个代理的目标方法），返回代理执行链
					Object proxy = ProxyManager.createProxy(targetClass, proxyList);
					//存入Bean类型和Bean实例映射Map
					BeanHelper.putBean(targetClass, proxy);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Throwable {
		Map<Class<?>, List<Proxy>> map = new HashMap<>();
		if (proxyMap != null) {
			for (Map.Entry<Class<?>, Set<Class<?>>> entry : proxyMap.entrySet()) {
				Class<?> proxyClass = entry.getKey();
				Set<Class<?>> targetClassList = entry.getValue();
				for (Class<?> targetClass : targetClassList) {
					Proxy proxy = (Proxy) proxyClass.newInstance();
					if (map.containsKey(targetClass)) {
						map.get(targetClass).add(proxy);
					} else {
						ArrayList<Proxy> proxies = new ArrayList<>();
						proxies.add(proxy);
						map.put(targetClass, proxies);
					}
				}
			}
		}

		return map;
	}

	public static Map<Class<?>, Set<Class<?>>> createProxyMap() {
		Map<Class<?>, Set<Class<?>>> map = new HashMap<>();

		//所有继承公共切面的代理类
		Set<Class<?>> classList = ClassHelper.loadClassBySuperClass(ProxyAspect.class);
		if (classList != null && classList.size() > 0) {
			for (Class<?> proxyClass : classList) {
				if (proxyClass.isAnnotationPresent(Aspect.class)) {
					//获取当前代理类定义的注解
					Aspect aspect = proxyClass.getAnnotation(Aspect.class);

					//根据注解加载代理的所有目标类
					Set<Class<?>> targetClassList = loadAspectClasses(aspect);
					//代理类作为key，代理的所有目标类作为value
					map.put(proxyClass, targetClassList);
				}
			}
		}
		return map;
	}

	/**
	 * 根据注解加载所有注解类
	 * @param aspect
	 * @return
	 */
	public static Set<Class<?>> loadAspectClasses(Aspect aspect) {
		Set<Class<?>> classes = new HashSet<>();
		//切面注射类的反射
		Class<? extends Annotation> value = aspect.value();
		if (!value.equals(Aspect.class)) {
			//加载定义了切面注解的类
			classes = ClassHelper.loadClassByAnnotation(value);
		}
		return classes;
	}


}
