package com.ispring.context.application;

public interface ApplicationContext {
	<T> T getBean(String beanName);
}
