package com.photography.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.photography.dao.exp.Condition;
import com.photography.dao.exp.Expression;
import com.photography.dao.query.Pager;
import com.photography.exception.ErrorCode;
import com.photography.exception.ErrorMessage;
import com.photography.mapping.ProjectOrder;
import com.photography.mapping.User;
import com.photography.service.IMailService;
import com.photography.service.IProjectOrderService;
import com.photography.service.IProjectService;
import com.photography.service.IUserService;
import com.photography.utils.Constants;
import com.photography.utils.CustomizedPropertyPlaceholderConfigurer;
import com.photography.utils.MD5Util;
import com.photography.utils.MessageConstants;

/**
 * 用户中心
 * @author 徐雁斌
 * @since 2015-3-12
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Controller
@RequestMapping("/userinfo")
public class UserInfoController extends BaseController {
	
	private final static Logger log = Logger.getLogger(UserInfoController.class);
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IMailService mailService;
	
	@Autowired
	private IProjectOrderService projectOrderService;
	
	public void setProjectOrderService(IProjectOrderService projectOrderService) {
		this.projectOrderService = projectOrderService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}
	
	/**
	 * 用户中心页面
	 * @param request
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/toUserInfo")
	public ModelAndView toUserInfo(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		
		//取得订单信息
		Pager pager= new Pager();
		pager.setPageSize(5);
		Expression exp = null;
		List<ProjectOrder> projectOrders = projectOrderService.getPojoList(ProjectOrder.class, pager, exp, null, null);
		mv.addObject("projectOrders", projectOrders);
		
		mv.setViewName("user/person_info/person_info");
		return mv;
	}
	
	/**
	 * 修改头像
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/updateHeadPhoto")
	@ResponseBody
	public String updateHeadPhoto(MultipartFile headFile,HttpServletRequest request, Model model){
		User user = (User) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		if(user == null){
			return (String) ErrorMessage.get(ErrorCode.SESSION_TIMEOUT);
		}
		String filePath = request.getSession().getServletContext().getRealPath((String)
    			CustomizedPropertyPlaceholderConfigurer.getContextProperty("user.confirm.file"))  + File.separator + user.getEmail();
    	
    	//数据库存储相对路径
    	String relativePath = CustomizedPropertyPlaceholderConfigurer.getContextProperty("user.confirm.file") + user.getEmail();
    	File userFile = new File(filePath);
    	if(!userFile.exists()){
    		userFile.mkdir();
    	}
    	
    	try{
			FileUtils.copyInputStreamToFile(headFile.getInputStream(), new File(filePath, headFile.getOriginalFilename()));
			
			user.setHeadPic(relativePath + "/" + headFile.getOriginalFilename());
			userService.savePojo(user, user);
    	}catch(Exception e){
    		return (String) ErrorMessage.get(ErrorCode.UNKNOWN_ERROR);
    	}
    	request.getSession().setAttribute(Constants.SESSION_USER_KEY, user);
    	return Constants.YES;
	}
	
	/**
	 * 修改头像
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/updateComfirmPhoto")
	@ResponseBody
	public String updateComfirmPhoto(MultipartFile comfirmFile,HttpServletRequest request, Model model){
		User user = (User) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		if(user == null){
			return ErrorMessage.get(ErrorCode.SESSION_TIMEOUT);
		}
		String filePath = request.getSession().getServletContext().getRealPath((String)
    			CustomizedPropertyPlaceholderConfigurer.getContextProperty("user.confirm.file"))  + File.separator + user.getEmail();
    	
    	//数据库存储相对路径
    	String relativePath = CustomizedPropertyPlaceholderConfigurer.getContextProperty("user.confirm.file") + user.getEmail();
    	File userFile = new File(filePath);
    	if(!userFile.exists()){
    		userFile.mkdir();
    	}
    	
    	try{
			FileUtils.copyInputStreamToFile(comfirmFile.getInputStream(), new File(filePath, comfirmFile.getOriginalFilename()));
			
			user.setComfirmPic(relativePath + "/" + comfirmFile.getOriginalFilename());
			userService.savePojo(user, user);
    	}catch(Exception e){
    		return (String) ErrorMessage.get(ErrorCode.UNKNOWN_ERROR);
    	}
    	request.getSession().setAttribute(Constants.SESSION_USER_KEY, user);
    	return Constants.YES;
	}
	
	/**
	 * 修改用户基本信息
	 * @param user
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/updateUserInfo")
	public ModelAndView updateUserInfo(User user,HttpServletRequest request, Model model){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("user/person_info/person_info");
		
		if(user ==null || user.getId() == null){
			mv.addObject("errorMessage", ErrorMessage.get(ErrorCode.SESSION_TIMEOUT));
			return mv;
		}
		
		User userDB = (User) userService.loadPojo(User.class,user.getId());
		
		if(userDB == null){
			mv.addObject("errorMessage", ErrorMessage.get(ErrorCode.SESSION_TIMEOUT));
			return mv;
		}
		
		userDB.setBirthDay(user.getBirthDay());
//		userDB.setCity(user.getCity());
//		userDB.setCompanyName(user.getCompanyName());
//		userDB.setCounty(user.getCounty());
//		userDB.setEmail(user.getEmail());
//		userDB.setIdCard(user.getIdCard());
		userDB.setMobile(user.getMobile());
		userDB.setNackName(user.getNackName());
		userDB.setPersonSignature(user.getPersonSignature());
//		userDB.setProvince(user.getProvince());
		userDB.setQqNumber(user.getQqNumber());
//		userDB.setRealName(user.getRealName());
		userDB.setSex(user.getSex());
    	
    	try{
			userService.savePojo(userDB, userDB);
			mv.addObject("href", "info");
			mv.addObject("successMessage", MessageConstants.SAVE_SUCCESS);
    	}catch(Exception e){
    		mv.addObject("errorMessage", ErrorMessage.get(ErrorCode.UNKNOWN_ERROR));
    	}
    	request.getSession().setAttribute(Constants.SESSION_USER_KEY, userDB);
    	return mv;
	}
	
	/**
	 * 修改用户认证信息
	 * @param user
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/updateAuthInfo")
	public ModelAndView updateAuthInfo(User user,HttpServletRequest request, Model model){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("user/person_info/person_info");
		
		if(user ==null || user.getId() == null){
			mv.addObject("errorMessage", ErrorMessage.get(ErrorCode.SESSION_TIMEOUT));
			return mv;
		}
		
		User userDB = (User) userService.loadPojo(User.class,user.getId());
		if(userDB == null){
			mv.addObject("errorMessage", ErrorMessage.get(ErrorCode.SESSION_TIMEOUT));
			return mv;
		}
			
//		userDB.setBirthDay(user.getBirthDay());
//		userDB.setCity(user.getCity());
//		userDB.setCompanyName(user.getCompanyName());
//		userDB.setCounty(user.getCounty());
//		userDB.setEmail(user.getEmail());
		userDB.setIdCard(user.getIdCard());
		userDB.setMobile(user.getMobile());
//		userDB.setNackName(user.getNackName());
//		userDB.setPersonSignature(user.getPersonSignature());
//		userDB.setProvince(user.getProvince());
//		userDB.setQqNumber(user.getQqNumber());
		userDB.setRealName(user.getRealName());
    	
    	try{
			userService.savePojo(userDB, userDB);
			mv.addObject("href", "auth");
			mv.addObject("successMessage", MessageConstants.SAVE_SUCCESS);
    	}catch(Exception e){
    		mv.addObject("errorMessage", ErrorMessage.get(ErrorCode.UNKNOWN_ERROR));
    	}
    	request.getSession().setAttribute(Constants.SESSION_USER_KEY, userDB);
    	return mv;
	}
	
	/**
	 * 修改密码
	 * @param user
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/updateUserPasswordInfo")
	public ModelAndView updateUserPasswordInfo(String id,String oldPassword, String password,HttpServletRequest request, Model model){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("user/person_info/person_info");
		
		if(id == null){
			mv.addObject("errorMessage", ErrorMessage.get(ErrorCode.SESSION_TIMEOUT));
			return mv;
		}
		
		User userDB = (User) userService.loadPojo(User.class,id);
		
		if(userDB == null){
			mv.addObject("errorMessage", ErrorMessage.get(ErrorCode.SESSION_TIMEOUT));
			return mv;
		}
		
		if(!userDB.getPassword().equals(MD5Util.md5(oldPassword))){
			mv.addObject("errorMessage", ErrorMessage.get(ErrorCode.USER_PWD_NOT_MATCH));
			return mv;
		}
		
		userDB.setPassword(password);
    	
    	try{
			userService.savePojo(userDB, userDB);
			mv.addObject("href", "modifyps");
			mv.addObject("successMessage", MessageConstants.SAVE_SUCCESS);
    	}catch(Exception e){
    		mv.addObject("errorMessage", ErrorMessage.get(ErrorCode.UNKNOWN_ERROR));
    	}
    	request.getSession().setAttribute(Constants.SESSION_USER_KEY, userDB);
    	return mv;
	}
	
	/**
	 * 测试
	 * @param request
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/test")
	public ModelAndView test(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("user/person_info/person_info");
		return mv;
	}

}
