package com.ispring.context.demo.ioc;


import com.ispring.context.annotation.Service;
import com.ispring.context.annotation.Transactional;

@Service
public class IoCService {

	@Transactional
	public void test() {
		System.out.println("yuan fang fa");
	}
}
