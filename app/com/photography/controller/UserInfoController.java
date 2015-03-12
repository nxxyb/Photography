package com.photography.controller;

import java.io.File;

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

import com.photography.mapping.User;
import com.photography.service.IMailService;
import com.photography.service.IUserService;
import com.photography.utils.Constants;
import com.photography.utils.CustomizedPropertyPlaceholderConfigurer;

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
		mv.addObject("html_template", "info.html");
		mv.setViewName("user/person_info/person_info");
		return mv;
	}
	
	/**
	 * 修改头像
	 * @param user
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
			return (String) CustomizedPropertyPlaceholderConfigurer.getContextProperty("error.user.invild.session");
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
    		return (String) CustomizedPropertyPlaceholderConfigurer.getContextProperty("error.unknown");
    	}
    	request.getSession().setAttribute(Constants.SESSION_USER_KEY, user);
    	return Constants.YES;
	}
	
	/**
	 * 修改头像
	 * @param user
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
			return (String) CustomizedPropertyPlaceholderConfigurer.getContextProperty("error.user.invild.session");
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
    		return (String) CustomizedPropertyPlaceholderConfigurer.getContextProperty("error.unknown");
    	}
    	request.getSession().setAttribute(Constants.SESSION_USER_KEY, user);
    	return Constants.YES;
	}

}
