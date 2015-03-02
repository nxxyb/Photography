package com.photography.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.photography.mapping.User;
import com.photography.service.IMailService;
import com.photography.service.IUserService;
import com.photography.utils.Constants;
import com.photography.utils.CustomizedPropertyPlaceholderConfigurer;

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
	
	@Autowired
	private IMailService mailService;
	
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
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
	 * 用户注册
	 * @param user
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public ModelAndView register(User user,HttpServletRequest request, Model model){
		try{
			userService.savePojo(user, user);
			mailService.sendConfirmMail(user.getEmail(), user.getId());
			ModelAndView mav=new ModelAndView();
			mav.addObject("email", user.getEmail()); 
	        mav.setViewName("user/register_normal_email"); 
	        return mav;
		}catch(Exception e){
			log.error(e);
			return null;
		}
	}
	
	/**
	 * 邮件确认
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/emailConfirm")
	public ModelAndView emailConfirm(String key,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		try {
			User user = (User) userService.loadPojo(key);
			if (user != null) {
				if(Constants.YES.equals(user.getEnable())){
					mav.addObject("error_message", CustomizedPropertyPlaceholderConfigurer.getContextProperty("error.user.confirmed"));
					mav.setViewName("error/error");
				}
				
				user.setEnable(Constants.YES);
				userService.savePojo(user, user);
				mav.addObject("email", "email");
				mav.setViewName("user/register_normal_email");
			}else{
				mav.addObject("error_message", CustomizedPropertyPlaceholderConfigurer.getContextProperty("error.user.not.found"));
				mav.setViewName("error/error");
			}
		} catch (Exception e) {
			log.error(e);
			mav.addObject("error_message", CustomizedPropertyPlaceholderConfigurer.getContextProperty("error.unknown"));
			mav.setViewName("error/error");
		}
		return mav;
	}
	
	
}
