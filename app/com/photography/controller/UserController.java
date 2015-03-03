package com.photography.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
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
	 * 跳转到register页面(爱好者用户)
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
	 * 用户注册(爱好者用户)
	 * @param user
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public ModelAndView register(User user,HttpServletRequest request, Model model){
		ModelAndView mav = new ModelAndView();
		try{
			user.setType(Constants.USER_TYPE_NORMAL);
			userService.savePojo(user, user);
			mailService.sendConfirmMail(user.getEmail(), user.getId());
			mav.addObject("email", user.getEmail()); 
	        mav.setViewName("user/register_normal_email");
		}catch(Exception e){
			log.error(e);
			mav.addObject("error_message", CustomizedPropertyPlaceholderConfigurer.getContextProperty("error.unknown"));
			mav.setViewName("error/error");
		}
		return mav;
	}
	
	/**
	 * 邮件确认(爱好者用户)
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
				if(!Constants.NO.equals(user.getEnable())){
					mav.addObject("error_message", CustomizedPropertyPlaceholderConfigurer.getContextProperty("error.user.confirmed"));
					mav.setViewName("error/error");
				}else{
					user.setEnable(Constants.YES);
					userService.savePojo(user, user);
					mav.addObject("email", "email");
					mav.setViewName("user/register_normal_email_confirm");
				}
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
	
	/**
	 * 跳转到register页面(活动发布用户)
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
	 * 用户注册(活动发布用户)
	 * @param user
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/registerPublisher",method=RequestMethod.POST)
	public ModelAndView registerPublisher(User user,MultipartFile file,HttpServletRequest request, Model model){
//		try{
//			user.setType(Constants.USER_TYPE_PUBLISH);
//			userService.savePojo(user, user);
//			mailService.sendConfirmMail(user.getEmail(), user.getId());
//			ModelAndView mav=new ModelAndView();
//			mav.addObject("email", user.getEmail()); 
//	        mav.setViewName("user/register_normal_email"); 
//	        return mav;
//		}catch(Exception e){
//			log.error(e);
//			return null;
//		}
		
		String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload/register");  
        try {
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, file.getOriginalFilename()));
		} catch (IOException e) {
			log.error("UserController.registerPublisher(): IOException", e);
		} 
		return null;
	}
	
	@RequestMapping("/test")
	public String test(HttpServletRequest request,Model model) {
		return "user/register_publisher";
	}
	
}
