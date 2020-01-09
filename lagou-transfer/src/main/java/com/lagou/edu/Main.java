package com.lagou.edu;

import com.ispring.context.annotation.AnnotationApplicationContext;
import com.lagou.edu.service.TransferService;
import org.junit.Test;


public class Main {

	@Test
	public void test2() throws Exception {
		AnnotationApplicationContext applicationContext = new AnnotationApplicationContext("com.lagou.edu");
		TransferService account =(TransferService) applicationContext.getBean("transferService");
		System.out.println(account);
		account.transfer("6029621011001", "6029621011000", 1000);
	}
}
