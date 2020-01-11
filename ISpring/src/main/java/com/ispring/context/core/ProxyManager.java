package com.ispring.context.core;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 动态代理管理器，创建代理对象
 */
public class ProxyManager {

	/**
	 * 根据代理目标类型和多个代理类批量创建cglib动态代理对象
	 * @param targetClass
	 * @param proxyList
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList) {

		return  (T) Enhancer.create(targetClass, new MethodInterceptor() {
			//返回执行链依次执行所有植入的增强处理
			@Override
			public Object intercept(Object targetObject, Method targetMethod,
			                        Object[] methodParams, MethodProxy methodProxy) throws Throwable {
				return new ProxyChain(targetClass, targetObject, targetMethod, methodParams, methodProxy, proxyList)
						.doProxyChain();

			}
		});
	}
}
