package com.ispring.context.core;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

public class ProxyChain {

	//代理的目标类
	private Class<?> targetClass;

	//代理的目标实例
	private Object targetObject;

	//代理的目标方法
	private Method targetMethod;

	//代理的目标方法参数
	private Object[] methodParams;

	//代理方法
	private MethodProxy methodProxy;

	//所有代理对象
	private List<Proxy> proxyList;

	//执行链执行索引
	private int proxyExecuteIndex;

	private int executeIndex = 0;

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public Object getTargetObject() {
		return targetObject;
	}

	public Method getTargetMethod() {
		return targetMethod;
	}

	public Object[] getMethodParams() {
		return methodParams;
	}

	public MethodProxy getMethodProxy() {
		return methodProxy;
	}

	public List<Proxy> getProxyList() {
		return proxyList;
	}

	public int getProxyExecuteIndex() {
		return proxyExecuteIndex;
	}

	//构造函数，注入代理参数
	public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, Object[] methodParams,
	                  MethodProxy methodProxy, List<Proxy> proxyList) {
		this.targetClass = targetClass;
		this.targetObject = targetObject;
		this.targetMethod = targetMethod;
		this.methodParams = methodParams;
		this.methodProxy = methodProxy;
		this.proxyList = proxyList;
		this.proxyExecuteIndex = 0;
	}

	public Object doProxyChain() throws Throwable {
		Object methodResult;
		if (proxyExecuteIndex < proxyList.size()) {
			Proxy proxy = proxyList.get(proxyExecuteIndex);
			proxyExecuteIndex++;
			methodResult = proxy != null ? proxy.doProxy(this) : null;
			//proxyExecuteIndex++;
		}
		else {
			methodResult = methodProxy.invokeSuper(targetObject, methodParams);
		}
		return methodResult;
	}

}
