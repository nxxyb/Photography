package com.photography.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.photography.exception.ErrorCode;
import com.photography.exception.ErrorMessage;
import com.photography.exception.ServiceException;
import com.photography.mapping.User;
import com.photography.service.IUserService;
import com.photography.utils.Constants;
import com.photography.utils.MD5Util;

@Controller
@RequestMapping("/admin")
public class AdminMainController extends BaseController{
	
private final static Logger log = Logger.getLogger(MainController.class);
	
	@Autowired
	private IUserService userService;
	
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@RequestMapping("/index")
	public ModelAndView toLogin(HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("admin/login");
		return mav;
	}
	
	@RequestMapping("/login")
	public ModelAndView login(String loginName,String password,HttpServletRequest request, Model model) {
		
		//解决回退问题
		if(StringUtils.isEmpty(loginName)){
			User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
			if(sessionUser != null){
				return toMain(request, model);
			}
		}
		
		try {
			User user = userService.login(loginName, MD5Util.md5(password));
			request.getSession().setAttribute(Constants.SESSION_USER_KEY, user);
		} catch (Exception e) {
			log.error("MainController.login(): ServiceException", e);
			ModelAndView mav = new ModelAndView();
			if(e instanceof ServiceException){
				ServiceException se = (ServiceException) e;
				String message = se.getErrorMessage();
				mav.addObject("errorMessage", message); 
				mav.setViewName("admin/login");
			}else{
				log.error("login error",e);
				mav.addObject("errorMessage", ErrorMessage.get(ErrorCode.UNKNOWN_ERROR));
				mav.setViewName("login");
			}
			return mav;
		}
		return toMain(request, model);
	}
	
	@RequestMapping("/toMain")
	public ModelAndView toMain(HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		
		User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		if(sessionUser == null){
			return mav;
		}
		mav.setViewName("admin/main/index");
		return mav;
	}
}
