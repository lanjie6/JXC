package com.java1234.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统首页请求控制器
 * @author 兰杰 2018.10.18
 * @version 1.0
 *
 */
@Controller
public class IndexController {
	
	/**
	 * 进入登录页面
	 * @return 重定向至登录页面
	 */
	@RequestMapping("/")
	public String toIndex(){
		
		return "redirect:login.html";
	}
	
}
