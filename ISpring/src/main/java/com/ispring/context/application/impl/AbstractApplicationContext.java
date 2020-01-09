package com.ispring.context.application.impl;

import com.ispring.context.application.ApplicationContext;
import com.ispring.context.factory.DefaultBeanFactory;

public class AbstractApplicationContext implements ApplicationContext {


	private DefaultBeanFactory beanFactory;


//	public AbstractApplicationContext(String packageName) {
//		this.packageName = packageName;
//	}

	public void scan(String... packages) {
		beanFactory = new DefaultBeanFactory();
		beanFactory.scanBean(packages);
	}

	public void refresh() {

		// 产生工厂
		obtainFreshBeanFactory();

		// 处理 AOP操作
		aopHandler();
	}

	private void obtainFreshBeanFactory(){
		// 实例化 DefaultListableBeanFactory
		// 实例化工厂中的bean
		try {
			beanFactory.createBeanInstance();
			beanFactory.setBeanAttribute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void aopHandler() {
		//动态代理处理AOP事务
		beanFactory.afterBeanCreate();
	}


	public <T> T getBean(String id)  {

		return beanFactory.getBean(id);
	}

}
