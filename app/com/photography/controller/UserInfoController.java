package com.photography.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.photography.dao.exp.Condition;
import com.photography.dao.exp.Expression;
import com.photography.dao.query.Pager;
import com.photography.dao.query.QueryConstants;
import com.photography.dao.query.Sort;
import com.photography.exception.ErrorCode;
import com.photography.exception.ErrorMessage;
import com.photography.exception.ServiceException;
import com.photography.mapping.Project;
import com.photography.mapping.ProjectCollect;
import com.photography.mapping.ProjectOrder;
import com.photography.mapping.User;
import com.photography.mapping.Work;
import com.photography.mapping.WorkCollect;
import com.photography.service.IMailService;
import com.photography.service.IProjectOrderService;
import com.photography.service.IProjectService;
import com.photography.service.IUserService;
import com.photography.utils.Constants;
import com.photography.utils.CustomizedPropertyPlaceholderConfigurer;
import com.photography.utils.DoubleUtil;
import com.photography.utils.ImageUtils;
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
	 * type: 1 个人资料  2 认证信息 3 我的收藏 4 我的订单 5 优惠卷 6 修改密码  7 发布活动  8 发布作品
	 * @param request
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/toUserInfo")
	public ModelAndView toUserInfo(@ModelAttribute("type")String type,HttpServletRequest request) throws ServiceException {
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
			mv.addObject("dzfNum", projectOrderService.getCountByQuery(ProjectOrder.class, Condition.eq("user.id", user.getId()).and(Condition.eq("status", Constants.USER_ORDER_STATUS_WZF))));
			mv.addObject("dpjNum", projectOrderService.getCountByQuery(ProjectOrder.class, Condition.eq("user.id", user.getId()).
					and(Condition.eq("status", Constants.USER_ORDER_STATUS_YZF)).and(Condition.or(Condition.eq("isComment",null),Condition.eq("isComment",Constants.NO)))));
			page = "user/user_order";
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
	
	/**
	 * 获取用户发布活动列表
	 * type: 1-进行中  2-历史
	 * @param request
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/getUserFBProject")
	public ModelAndView getUserFBProject(String type,Pager pager,HttpServletRequest request) throws ServiceException {
		ModelAndView mv = new ModelAndView();
		
		User user = getSessionUser(request);
		List<Project> projects = null;
		if("1".equals(type)){
			Expression exp = Condition.and(Condition.gt("startTime", new Date()),Condition.eq("createUser.id", user.getId()));
			projects = projectService.getPojoList(Project.class, pager, exp, new Sort("createTime",QueryConstants.DESC),user);
			mv.setViewName("user/user_project_going_item");
		}else{
			Expression exp = Condition.and(Condition.le("startTime", new Date()),Condition.eq("createUser.id", user.getId()));
			projects = projectService.getPojoList(Project.class, pager, exp, new Sort("createTime",QueryConstants.DESC),user);
			mv.setViewName("user/user_project_history_item");
		}
		mv.addObject("pager", pager);
		mv.addObject("projects", projects);
		return mv;
	}
	
	/**
	 * 用户删除活动
	 * @param request
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/deleteUserFBProject")
	public String deleteUserFBProject(String id,HttpServletRequest request,RedirectAttributes ra) throws ServiceException {
		Project project = projectService.loadPojo(Project.class, id);
		if(project != null){
			projectService.deletePojo(project, getSessionUser(request));
		}
		ra.addFlashAttribute("type", "7");
		return "redirect:toUserInfo";
	}
	
	/**
	 * 获取收藏活动列表
	 * @param request
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/getUserProjectCollect")
	public ModelAndView getUserProjectCollect(Pager pager,HttpServletRequest request) throws ServiceException {
		ModelAndView mv = new ModelAndView();
		
		User user = getSessionUser(request);
		List<ProjectCollect> projectCollects = null;
		Expression exp =Condition.eq("user.id", user.getId());
		projectCollects = projectService.getPojoList(ProjectCollect.class, pager, exp, new Sort("createTime",QueryConstants.DESC),user);
		mv.setViewName("user/user_collect_project_item");

		mv.addObject("pager", pager);
		mv.addObject("projectCollects", projectCollects);
		return mv;
	}
	
	/**
	 * 用户删除收藏
	 * @param request
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/deleteUserProjectCollect")
	public String deleteUserProjectCollect(String id,HttpServletRequest request,RedirectAttributes ra) throws ServiceException {
		ProjectCollect projectCollect = projectService.loadPojo(ProjectCollect.class, id);
		if(projectCollect != null){
			projectService.deletePojo(projectCollect, getSessionUser(request));
		}
		ra.addFlashAttribute("type", "3");
		return "redirect:toUserInfo";
	}
	
	/**
	 * 获取收藏活动列表
	 * @param request
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/getUserWorkCollect")
	public ModelAndView getUserWorkCollect(Pager pager,HttpServletRequest request) throws ServiceException {
		ModelAndView mv = new ModelAndView();
		
		User user = getSessionUser(request);
		List<WorkCollect> workCollects = null;
		Expression exp =Condition.eq("user.id", user.getId());
		workCollects = projectService.getPojoList(WorkCollect.class, pager, exp, new Sort("createTime",QueryConstants.DESC),user);
		mv.setViewName("user/user_collect_work_item");

		mv.addObject("pager", pager);
		mv.addObject("workCollects", workCollects);
		return mv;
	}
	
	/**
	 * 用户删除收藏
	 * @param request
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/deleteUserWorkCollect")
	public String deleteUserWorkCollect(String id,HttpServletRequest request,RedirectAttributes ra) throws ServiceException {
		WorkCollect workCollect = projectService.loadPojo(WorkCollect.class, id);
		if(workCollect != null){
			projectService.deletePojo(workCollect, getSessionUser(request));
		}
		ra.addFlashAttribute("type", "3");
		return "redirect:toUserInfo";
	}
	
	/**
	 * 获取活动订单列表
	 * @param type 1 全部   2 待支付  3 待评价
	 * @param request
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/getUserProjectOrder")
	public ModelAndView getUserProjectOrder(String type,Pager pager,HttpServletRequest request) throws ServiceException {
		ModelAndView mv = new ModelAndView();
		
		User user = getSessionUser(request);
		List<ProjectOrder> projectOrders = null;
		Expression exp = Condition.eq("user.id", user.getId());
		if("1".equals(type)){
			
		}else if("2".equals(type)){
			exp = exp.and(Condition.eq("status", Constants.USER_ORDER_STATUS_WZF));
		}else if("3".equals(type)){
			exp = exp.and(Condition.eq("status", Constants.USER_ORDER_STATUS_YZF)).and(Condition.or(Condition.eq("isComment",null),Condition.eq("isComment",Constants.NO)));
		}
		projectOrders = projectOrderService.getPojoList(ProjectOrder.class, pager, exp, new Sort("createTime",QueryConstants.DESC),user);
		mv.setViewName("user/user_order_item");
		mv.addObject("type",type);
		mv.addObject("pager", pager);
		mv.addObject("projectOrders", projectOrders);
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
	
	/**
	 * 修改头像
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/updateHeadPhoto")
	@ResponseBody
	public String updateHeadPhoto(MultipartFile headFile,String avatar_data,HttpServletRequest request, Model model){
		User user = (User) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		if(user == null){
			return (String) ErrorMessage.get(ErrorCode.SESSION_TIMEOUT);
		}
		String filePath = request.getSession().getServletContext().getRealPath((String)
    			CustomizedPropertyPlaceholderConfigurer.getContextProperty("user.confirm.file"))  + File.separator + user.getMobile();
    	
    	//数据库存储相对路径
    	String relativePath = CustomizedPropertyPlaceholderConfigurer.getContextProperty("user.confirm.file") + user.getMobile();
    	File userFile = new File(filePath);
    	if(!userFile.exists()){
    		userFile.mkdir();
    	}
    	
    	try{
    		File originalFile= new File(filePath, headFile.getOriginalFilename());
			FileUtils.copyInputStreamToFile(headFile.getInputStream(), originalFile);
			
			Map<String, Object> map = new ObjectMapper().readValue(avatar_data, Map.class);
			
			Double x = DoubleUtil.objectToDouble(map.get("x"));
			Double y = DoubleUtil.objectToDouble(map.get("y"));
			Double height = DoubleUtil.objectToDouble(map.get("height"));
			Double width = DoubleUtil.objectToDouble(map.get("width"));
			
//			Double rotate = Double.parseDouble(map.get("rotate"));
			
			File originalFileCut = new File(filePath, "cut_" + headFile.getOriginalFilename());
			ImageUtils.cut(originalFile.getPath(), originalFileCut.getPath(), x.intValue(), y.intValue(), width.intValue(), height.intValue());
			
			user.setHeadPic(relativePath + "/" + "cut_" + headFile.getOriginalFilename());
			userService.savePojo(user, user);
			setSessionUser(request, user);
    	}catch(Exception e){
    		e.printStackTrace();
    		String errorMessage = getErrorMessage(e);
    		return "{\"state\":0,\"message\":\"" + errorMessage + "\",\"result\":\"\"}";
    		 
    	}
    	request.getSession().setAttribute(Constants.SESSION_USER_KEY, user);
    	return "{\"state\":200,\"message\":\"\",\"result\":\"" + request.getContextPath() + "/" + user.getHeadPic() + "\"}";
	}
	
	/**
	 * 修改认证信息
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
    			CustomizedPropertyPlaceholderConfigurer.getContextProperty("user.confirm.file"))  + File.separator + user.getMobile();
    	
    	//数据库存储相对路径
    	String relativePath = CustomizedPropertyPlaceholderConfigurer.getContextProperty("user.confirm.file") + user.getMobile();
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
    	setSessionUser(request, user);
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
			userDB.setPersonSignature(user.getPersonSignature());
    	
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
	    	userDB.setVerifyTime(new Date());
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
	
	//作品
	/**
	 * 获取用户作品列表
	 * @param request
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/getUserWork")
	public ModelAndView getUserWork(Pager pager,HttpServletRequest request) throws ServiceException {
		ModelAndView mv = new ModelAndView();
		User user = getSessionUser(request);
		List<Work> works = null;
		Expression exp =Condition.eq("createUser.id", user.getId());
		works = projectService.getPojoList(Work.class, pager, exp, new Sort("createTime",QueryConstants.DESC),user);
		mv.setViewName("user/user_work_item");

		mv.addObject("pager", pager);
		mv.addObject("works", works);
		return mv;
	}
	
	/**
	 * 用户删除作品
	 * @param request
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/deleteUserWork")
	public String deleteUserWork(String id,HttpServletRequest request,RedirectAttributes ra) throws ServiceException {
		Work work = projectService.loadPojo(Work.class, id);
		if(work != null){
			projectService.deletePojo(work, getSessionUser(request));
		}
		ra.addFlashAttribute("type", "8");
		return "redirect:toUserInfo";
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
