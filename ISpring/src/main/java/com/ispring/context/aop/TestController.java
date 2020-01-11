package com.ispring.context.aop;

import com.ispring.context.annotation.Controller;
import com.ispring.context.annotation.Transactional;

@Controller
public class TestController {

	@Transactional
	public void show() {
		System.out.println("=========原方法=========");
	}
}
