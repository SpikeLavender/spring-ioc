package com.ispring.context.application.impl;

import com.ispring.context.application.ApplicationContext;
import com.ispring.context.factory.DefaultBeanFactory;

public class AbstractApplicationContext implements ApplicationContext {


	private DefaultBeanFactory beanFactory;

	private String packageName;

	public AbstractApplicationContext(String packageName) {
		this.packageName = packageName;
	}

	public void refresh() {
		// 产生工厂
		obtainFreshBeanFactory();

		// 实例化工厂中的bean
		try {
			createBean();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 处理 AOP操作
		aopHandler();
	}

	private void obtainFreshBeanFactory(){
		// 实例化 DefaultListableBeanFactory
		beanFactory = new DefaultBeanFactory(this.packageName);
		beanFactory.loadBean();
	}


	private void createBean() throws ReflectiveOperationException {
		beanFactory.createBeanInstance();
		beanFactory.setBeanAttribute();

	}

	private void aopHandler() {
		//动态代理处理AOP事务
		beanFactory.afterBeanCreate();
	}


	public <T> T getBean(String id)  {

		return beanFactory.getBean(id);
	}

}
