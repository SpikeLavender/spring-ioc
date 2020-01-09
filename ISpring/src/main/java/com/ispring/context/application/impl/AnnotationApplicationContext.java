package com.ispring.context.application.impl;



public class AnnotationApplicationContext extends AbstractApplicationContext {

	public AnnotationApplicationContext(String packageName) {
		super(packageName);
		//设置配置

		// 完成Spring容器的初始化
		refresh();
	}
}
