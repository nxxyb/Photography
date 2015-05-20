package com.photography.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.photography.exception.ErrorCode;
import com.photography.exception.ErrorMessage;
import com.photography.exception.ServiceException;
import com.photography.mapping.User;
import com.photography.service.IMailService;
import com.photography.service.IUserService;
import com.photography.utils.Constants;
import com.photography.utils.MD5Util;

/**
 * 用户登录、注册
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
	public ModelAndView login(String email,String password,HttpServletRequest request, Model model){
		ModelAndView mav = new ModelAndView();
		try{
			User user = userService.login(email, MD5Util.md5(password));
			request.getSession().setAttribute(Constants.SESSION_USER_KEY, user);
			mav.setViewName("redirect:/index");
		}catch(Exception e){
			if(e instanceof ServiceException){
				ServiceException se = (ServiceException) e;
				String message = se.getErrorMessage();
				mav.addObject("errorMessage", message); 
				mav.setViewName("user/login");
			}else{
				log.error("login error",e);
				mav.addObject("errorMessage", ErrorMessage.get(ErrorCode.UNKNOWN_ERROR));
				mav.setViewName("user/login");
			}
		}
		return mav;
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
		return "user/register/register_normal";
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
			user.setPassword(MD5Util.md5(user.getPassword()));
			userService.savePojo(user, user);
			mailService.sendConfirmMail(user.getEmail(), user.getId());
			mav.addObject("email", user.getEmail()); 
	        mav.setViewName("user/register/register_normal_email");
		}catch(Exception e){
			log.error(e);
			mav.addObject("error_message", ErrorMessage.get(ErrorCode.UNKNOWN_ERROR));
			mav.setViewName("error/error");
		}
		return mav;
	}
	
	/**
	 * 重新发送确认邮件
	 * @param email
	 * @param type  1-普通用户  2 发布用户
	 * @param request
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/reSendEmail")
	public ModelAndView reSendEmail(String email,HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		try{
			User user = userService.getByEmail(email);
			if(user != null){
				mailService.sendConfirmMail(user.getEmail(), user.getId());
				mav.addObject("email", email); 
				if(Constants.USER_TYPE_NORMAL.equals(user.getType())){
					mav.setViewName("user/register/register_normal_email");
				}else{
					mav.setViewName("user/register/register_publisher_email");
				}
			}else{
				mav.addObject("error_message", ErrorMessage.get(ErrorCode.USER_NOT_EXIST));
				mav.setViewName("error/error");
			}
		}catch(Exception e){
			log.error(e);
			mav.addObject("error_message", ErrorMessage.get(ErrorCode.UNKNOWN_ERROR));
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
			User user = (User) userService.loadPojo(User.class,key);
			if (user != null) {
				user.setEnable(Constants.YES);
				userService.savePojo(user, user);
				mav.addObject("user", user);
				if(Constants.USER_TYPE_NORMAL.equals(user.getType())){
					mav.setViewName("user/register/register_normal_email_confirm");
				}else{
					mav.setViewName("user/register/register_publisher_email_confirm");
				}
			}else{
				mav.addObject("error_message", ErrorMessage.get(ErrorCode.USER_NOT_EXIST));
				mav.setViewName("error/error");
			}
		} catch (Exception e) {
			log.error(e);
			mav.addObject("error_message", ErrorMessage.get(ErrorCode.UNKNOWN_ERROR));
			mav.setViewName("error/error");
		}
		return mav;
	}
	
//	/**
//	 * 跳转到register页面(活动发布用户)
//	 * @param request
//	 * @param model
//	 * @return
//	 * @author 徐雁斌
//	 */
//	@RequestMapping("/toRegisterPublisher")
//	public String toRegisterPublisher(HttpServletRequest request, Model model) {
//		return "user/register/register_publisher";
//	}
//	
//	/**
//	 * 用户注册(活动发布用户)
//	 * @param user
//	 * @param request
//	 * @param model
//	 * @return
//	 * @author 徐雁斌
//	 */
//	@RequestMapping(value="/registerPublisher",method=RequestMethod.POST)
//	public ModelAndView registerPublisher(User user,MultipartFile file,HttpServletRequest request, Model model){
//		ModelAndView mav=new ModelAndView();
//        try {
//        	String filePath = request.getSession().getServletContext().getRealPath((String)
//        			CustomizedPropertyPlaceholderConfigurer.getContextProperty("user.confirm.file"))  + File.separator + user.getEmail();
//        	
//        	//数据库存储相对路径
//        	String relativePath = CustomizedPropertyPlaceholderConfigurer.getContextProperty("user.confirm.file") + user.getEmail();
//        	FileUtil.saveFile(filePath, file);
//			
//			user.setComfirmPic(relativePath + "/" + file.getOriginalFilename());
//			userService.savePojo(user, user);
//			mailService.sendConfirmMail(user.getEmail(), user.getId());
//			
//			mav.addObject("email", user.getEmail());
//	        mav.setViewName("user/register/register_publisher_email"); 
//		} catch (Exception e) {
//			log.error("UserController.registerPublisher(): Exception", e);
//			if(e instanceof ServiceException){
//				ServiceException se = (ServiceException) e;
//				String message = se.getErrorMessage();
//				mav.addObject("errorMessage", message); 
//				mav.setViewName("error/error");
//			}else{
//				log.error("login error",e);
//				mav.addObject("error_message", ErrorMessage.get(ErrorCode.UNKNOWN_ERROR));
//				mav.setViewName("error/error");
//			}
//		} 
//        return mav;
//	}
	
	/**
	 * 检查邮件地址是否已经注册
	 * @param email
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/checkEmail" ,produces="application/json;charset=UTF-8")  
    @ResponseBody
    public String validataUser(@RequestParam String email){  
		String result = "{\"valid\":true}";
		User user = userService.getByEmail(email);
		if(user != null){
			result = Constants.NO;
		}
        return result;
    }
	
	/**
	 * 退出登录
	 * @param request
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/logout")
	public ModelAndView logout(HttpServletRequest request) {
		ModelAndView mav=new ModelAndView();
		request.getSession().invalidate();
		mav.setViewName("index");
		return mav;
	}
	
	@RequestMapping("/test")
	public ModelAndView test(HttpServletRequest request,Model model) {
		ModelAndView mav=new ModelAndView();
		mav.addObject("html_template", "info.html");
		mav.setViewName("user/register/register_normal_email_confirm");
		return mav;
	}
	
	
	
	
}
