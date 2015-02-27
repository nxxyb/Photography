package com.photography.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.photography.mapping.User;
import com.photography.service.IUserService;

/**
 * 
 * @author 徐雁斌
 * @since 2015-2-3
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
	
	private final static Logger log = Logger.getLogger(UserController.class);
	
	@Autowired
	private IUserService userService;
	
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	/**
	 * 跳转到login页面
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/toLogin")
	public String toLogin(HttpServletRequest request, Model model) {
		return "user/login";
	}
	
	/**
	 * login操作
	 * @param userName
	 * @param password
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(String userName,String password,HttpServletRequest request, Model model){
		try{
			User user = userService.login(userName, password);
			request.getSession().setAttribute("user", user);
		}catch(Exception e){
			log.error(e);
		}
		return "index";
	}
	
	/**
	 * 跳转到register页面
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/toRegister")
	public String toRegister(HttpServletRequest request, Model model) {
		return "user/register_normal";
	}
	
	/**
	 * 跳转到register页面
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/toRegisterPublisher")
	public String toRegisterPublisher(HttpServletRequest request, Model model) {
		return "user/register_publisher";
	}
	
	/**
	 * login操作
	 * @param userName
	 * @param password
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public String register(User user,HttpServletRequest request, Model model){
		try{
			userService.savePojo(user, user);
		}catch(Exception e){
			log.error(e);
		}
		return "index";
	}
	
	
}
