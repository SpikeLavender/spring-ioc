package com.ispring.context.aop;

import com.ispring.context.annotation.Aspect;
import com.ispring.context.annotation.Controller;
import com.ispring.context.core.ProxyAspect;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect(Controller.class)
public class AspectController extends ProxyAspect {

	@Override
	public void before(Class<?> targetClass, Method method, Object[] args) {
		super.before(targetClass, method, args);
		System.out.println(targetClass.getName() + "-" + method.getName() + "植入前置增强");
	}

	@Override
	public void after(Class<?> targetClass, Method method, Object[] args, Object result) {
		super.after(targetClass, method, args, result);
		System.out.println(targetClass.getName() + "-" + method.getName() + "植入后置增强");

	}
}
