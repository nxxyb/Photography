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

import com.photography.exception.ErrorCode;
import com.photography.exception.ErrorMessage;
import com.photography.exception.ServiceException;
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
	
	@Autowired
	private IProjectService projectService;
	
	public void setProjectOrderService(IProjectOrderService projectOrderService) {
		this.projectOrderService = projectOrderService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}
	
	public void setProjectService(IProjectService projectService) {
		this.projectService = projectService;
	}

	/**
	 * 用户中心页面
	 * type: 1 个人资料  2 认证信息 3 我的收藏 4 我的订单 5 优惠卷 6 修改密码
	 * @param request
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/toUserInfo")
	public ModelAndView toUserInfo(String type,HttpServletRequest request) throws ServiceException {
		ModelAndView mv = new ModelAndView();
		User user = getSessionUser(request);
		user = userService.loadPojo(User.class, user.getId());
		mv.addObject("user", user);
		
		String page = "user/user_info";
		if(type.equals("1")){
			page = "user/user_info";
		}else if(type.equals("2")){
			page = "user/user_auth";
		}else if(type.equals("3")){
			page = "user/user_collect";
		}else if(type.equals("4")){
			page = "user/user_bill";
		}else if(type.equals("5")){
			page = "user/user_coupon";
		}else if(type.equals("6")){
			page = "user/user_changepw";
		}else if(type.equals("7")){
			page = "user/user_project";
		}else if(type.equals("8")){
			page = "user/user_work";
		}
		mv.setViewName(page);
		return mv;
	}
	
//	/**
//	 * 切换tab，异步加载内容
//	 * @param request
//	 * @return
//	 * @author 徐雁斌
//	 */
//	@RequestMapping(value="/changeTab")
//	public ModelAndView changeTab(String tabName,HttpServletRequest request) {
//		ModelAndView mv = new ModelAndView();
//		mv.setViewName("user/person_info/" + tabName);
//		
//		User user = (User) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
//		if(user == null){
//			return mv;
//		}
//		
//		if("project_order".equals(tabName)){
//			//取得订单信息
//			Pager pager= new Pager();
//			pager.setPageSize(Constants.PAGER_PROJECT_ORDER);
//			Expression exp = Condition.eq("user.id", user.getId());
//			List<ProjectOrder> projectOrders = projectOrderService.getPojoList(ProjectOrder.class, pager, exp, new Sort("createTime",QueryConstants.DESC), user);
//			mv.addObject("pager", pager);
//			mv.addObject("projectOrders", projectOrders);
//		}else if("project_fb".equals(tabName)){
//			//取得订单信息
//			Pager pager= new Pager();
//			pager.setPageSize(Constants.PAGER_PROJECT_FB);
//			Expression exp = Condition.eq("createUser.id", user.getId());
//			List<Project> projects = projectService.getPojoList(Project.class, pager, exp, new Sort("createTime",QueryConstants.DESC), user);
//			mv.addObject("pager", pager);
//			mv.addObject("projects", projects);
//		}
//		
//		return mv;
//	}
	
//	/**
//	 * 修改头像
//	 * @param request
//	 * @param model
//	 * @return
//	 * @author 徐雁斌
//	 */
//	@RequestMapping(value="/updateHeadPhoto")
//	@ResponseBody
//	public String updateHeadPhoto(MultipartFile headFile,HttpServletRequest request, Model model){
//		User user = (User) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
//		if(user == null){
//			return (String) ErrorMessage.get(ErrorCode.SESSION_TIMEOUT);
//		}
//		String filePath = request.getSession().getServletContext().getRealPath((String)
//    			CustomizedPropertyPlaceholderConfigurer.getContextProperty("user.confirm.file"))  + File.separator + user.getEmail();
//    	
//    	//数据库存储相对路径
//    	String relativePath = CustomizedPropertyPlaceholderConfigurer.getContextProperty("user.confirm.file") + user.getEmail();
//    	File userFile = new File(filePath);
//    	if(!userFile.exists()){
//    		userFile.mkdir();
//    	}
//    	
//    	try{
//			FileUtils.copyInputStreamToFile(headFile.getInputStream(), new File(filePath, headFile.getOriginalFilename()));
//			
//			user.setHeadPic(relativePath + "/" + headFile.getOriginalFilename());
//			userService.savePojo(user, user);
//    	}catch(Exception e){
//    		return (String) ErrorMessage.get(ErrorCode.UNKNOWN_ERROR);
//    	}
//    	request.getSession().setAttribute(Constants.SESSION_USER_KEY, user);
//    	return Constants.YES;
//	}
	
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
	public ModelAndView updateUserInfo(User user,HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			User userDB = (User) userService.loadPojo(User.class,user.getId());
			userDB.setBirthDay(user.getBirthDay());
			userDB.setRealName(user.getRealName());
			userDB.setEmail(user.getEmail());
			userDB.setQqNumber(user.getQqNumber());
			userDB.setSex(user.getSex());
    	
			userService.savePojo(userDB, userDB);
			setSessionUser(request, userDB);
			mv.addObject("user", userDB);
			mv.addObject(Constants.SUCCESS_MESSAGE, MessageConstants.SAVE_SUCCESS);
    	}catch(Exception e){
    		handleErrorModelAndView(mv, e);
    		mv.addObject("user", user);
    	}
    	mv.setViewName("user/user_info");
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
	public ModelAndView updateAuthInfo(User user,MultipartFile comfirmFile,HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			User userDB = (User) userService.loadPojo(User.class,user.getId());
			userDB.setIdCard(user.getIdCard());
			
			if(!comfirmFile.isEmpty()){
				String filePath = request.getSession().getServletContext().getRealPath((String)
		    			CustomizedPropertyPlaceholderConfigurer.getContextProperty("user.confirm.file"))  + File.separator + userDB.getMobile();
		    	
		    	//数据库存储相对路径
		    	String relativePath = CustomizedPropertyPlaceholderConfigurer.getContextProperty("user.confirm.file") + userDB.getMobile();
		    	File userFile = new File(filePath);
		    	if(!userFile.exists()){
		    		userFile.mkdir();
		    	}
		    	FileUtils.copyInputStreamToFile(comfirmFile.getInputStream(), new File(filePath, comfirmFile.getOriginalFilename()));
		    	userDB.setComfirmPic(relativePath + "/" + comfirmFile.getOriginalFilename());
			}
	    	userDB.setVerify(Constants.VERIFY_ING);
			userService.savePojo(userDB, userDB);
			
			setSessionUser(request, userDB);
			mv.addObject("user", userDB);
			mv.addObject(Constants.SUCCESS_MESSAGE, MessageConstants.SAVE_SUCCESS);
    	}catch(Exception e){
    		handleErrorModelAndView(mv, e);
    		mv.addObject("user", user);
    	}
    	mv.setViewName("user/user_auth");
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
	public ModelAndView updateUserPasswordInfo(User user,String verifyCode,HttpServletRequest request, Model model){
		ModelAndView mv = new ModelAndView();
		try{
			//处理验证码
			
			User userDB = (User) userService.loadPojo(User.class,user.getId());
			userDB.setPassword(MD5Util.md5(user.getPassword()));
			userService.savePojo(userDB, userDB);
			
			setSessionUser(request, userDB);
			mv.addObject("user", userDB);
			mv.addObject(Constants.SUCCESS_MESSAGE, MessageConstants.PWD_SAVE_SUCCESS);
    	}catch(Exception e){
    		handleErrorModelAndView(mv, e);
    		mv.addObject("user", user);
    	}
    	mv.setViewName("user/user_changepw");
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
		mv.setViewName("user/person_info/sms");
		return mv;
	}

}
