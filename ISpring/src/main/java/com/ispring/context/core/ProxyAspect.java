package com.ispring.context.core;

import com.ispring.context.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * 定义切面，实现代理接口，其他类型切面直接继承该切面父类
 */
public abstract class ProxyAspect implements Proxy {

	@Override
	public Object doProxy(ProxyChain proxyChain) {
		Object result = null;

		Class<?> targetClass = proxyChain.getTargetClass();

		Method targetMethod = proxyChain.getTargetMethod();

		Object[] methodParams = proxyChain.getMethodParams();

		try {
			if (isNeedProxy(targetMethod)) {
				//织入增强处理
				before(targetClass, targetMethod, methodParams);
				//代理执行链
				result = proxyChain.doProxyChain();

				after(targetClass, targetMethod, methodParams, result);
			} else {
				result = proxyChain.doProxyChain();
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * todo: 控制要织入方法还是类？
	 * @param method
	 * @return
	 */
	private boolean isNeedProxy(Method method) {
		return method.isAnnotationPresent(Transactional.class);
	}

	/**
	 * 封装通用的增强处理方法（代理目标类，代理方法，方法参数，执行返回值）
	 * 前置增强
	 * @param targetClass
	 * @param method
	 * @param args
	 */
	public void before(Class<?> targetClass, Method method, Object[] args) {
		//前置增强处理
		System.out.println("========前置增强========");
	}

	/**
	 * 后置增强
	 * @param targetClass
	 * @param method
	 * @param args
	 * @param result
	 */
	public void after(Class<?> targetClass, Method method, Object[] args, Object result) {
		//后置增强处理
		System.out.println("========后置增强========");
	}
}
