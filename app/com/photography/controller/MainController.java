package com.photography.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author 徐雁斌
 * @since 2015-2-4
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Controller
@RequestMapping("/")
public class MainController extends BaseController{
	
	@RequestMapping("/index")
	public String toIndex(HttpServletRequest request, Model model) {
		return "index";
	}
}
