package com.ispring.context.factory;

import com.ispring.context.aop.TestController;
import com.ispring.context.helper.AopHelper;
import com.ispring.context.helper.BeanHelper;
import com.ispring.context.helper.ClassHelper;
import com.ispring.context.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

public class Test1 {
	public static void main(String[] args) {
		Set<Class<?>> classes=new HashSet<>();
		classes.add(ClassHelper.class);
		classes.add(BeanHelper.class);

		//TODO TCF AOP 基于 cglib动态代理
		classes.add(AopHelper.class);

		//TODO TCF IOC/DI
		//classes.add(IoCHelper.class);
		for(Class<?> cls : classes)
		{
			ClassUtil.loadClass(cls.getName());
		}
		TestController instance = (TestController)BeanHelper.getInstance(TestController.class);
		instance.show();
		//new TestController().show();
	}
}
