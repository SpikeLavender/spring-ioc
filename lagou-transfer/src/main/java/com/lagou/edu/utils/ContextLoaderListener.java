package com.lagou.edu.utils;

import com.ispring.context.application.impl.AnnotationApplicationContext;
import com.ispring.context.application.ApplicationContext;
import com.lagou.edu.servlet.TransferServlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {

		ApplicationContext applicationContext = new AnnotationApplicationContext("com.lagou.edu");
		TransferServlet transferServlet = applicationContext.getBean("transferServlet");
		System.out.println(transferServlet);
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}
}
