package com.ispring.context.demo.aop;

import com.ispring.context.annotation.Autowired;
import com.ispring.context.annotation.Controller;
import com.ispring.context.demo.ioc.IoCService;

@Controller
public class TestController {

	@Autowired
	private IoCService ioCService;

	public void show() {
		ioCService.test();
	}
}
