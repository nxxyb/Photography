package com.photography.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.photography.controller.view.ProjectTripList;
import com.photography.dao.exp.Condition;
import com.photography.dao.exp.Expression;
import com.photography.dao.query.Pager;
import com.photography.dao.query.QueryConstants;
import com.photography.dao.query.Sort;
import com.photography.exception.ErrorCode;
import com.photography.exception.ErrorMessage;
import com.photography.exception.ServiceException;
import com.photography.mapping.FileGroup;
import com.photography.mapping.FileInfo;
import com.photography.mapping.Project;
import com.photography.mapping.ProjectTrip;
import com.photography.mapping.User;
import com.photography.service.IProjectOrderService;
import com.photography.service.IProjectService;
import com.photography.utils.Constants;
import com.photography.utils.CustomizedPropertyPlaceholderConfigurer;
import com.photography.utils.FileUtil;
import com.photography.utils.MessageConstants;

/**
 * 活动
 * @author 徐雁斌
 * @since 2015-3-13
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {
	
	private final static Logger log = Logger.getLogger(ProjectController.class);
	
	public final static String PROJECT_FILE = "user.project.file";
	
	@Autowired
	private IProjectService projectService;
	
	@Autowired
	private IProjectOrderService projectOrderService;
	
	public void setProjectService(IProjectService projectService) {
		this.projectService = projectService;
	}
	
	public void setProjectOrderService(IProjectOrderService projectOrderService) {
		this.projectOrderService = projectOrderService;
	}
	
	/**
	 * 活动首页
	 */
	@RequestMapping("/index")
	public String toIndex(String errorMessage,HttpServletRequest request, RedirectAttributes attr) {
		return "project/project";
	}

	/**
	 * 进入新建页面
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/toCreate")
	public ModelAndView toCreate(String id,HttpServletRequest request, Model model){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("project/project_create");
		
		if(!StringUtils.isEmpty(id)){
			mav.addObject("project", projectService.loadPojo(Project.class,id));
		}
		return mav;
	}

	/**
	 * 新建活动
	 * @param project
	 * @param imgFiles
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/create")
	public ModelAndView create(Project project,@RequestParam MultipartFile[] photoPics,@RequestParam MultipartFile[] desPhotoPics,ProjectTripList tripList,HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("project/project_create");
		try{
			User user = getSessionUser(request);
			
			Project projectDb = null;
			if(!StringUtils.isEmpty(project.getId())){
				projectDb = (Project) projectService.loadPojo(Project.class,project.getId());
			}else{
				projectDb = project;
			}
			
        	FileGroup photoGroup = saveAndReturnFile(photoPics, request, user, projectDb.getPhotos());
        	project.setPhotos(photoGroup);
        	
        	FileGroup desPhotoGroup = saveAndReturnFile(desPhotoPics, request, user, projectDb.getDesPhotos());
        	project.setDesPhotos(desPhotoGroup);
        	
        	project.setCreateUser(user);
        	
        	projectService.savePojo(project, user);
        	
        	//处理行程
        	if(tripList != null && tripList.getTrips() != null && !tripList.getTrips().isEmpty()){
        		for(ProjectTrip projectTrip : tripList.getTrips()){
        			ProjectTrip projectTripDb = null;
        			if(!StringUtils.isEmpty(projectTrip.getId())){
        				projectTripDb = projectService.loadPojo(ProjectTrip.class,projectTrip.getId());
        			}else{
        				projectTripDb = projectTrip;
        			}
        			FileGroup desPhotos = saveAndReturnFile(projectTrip.getDesPhotoPics(), request, user, projectTrip.getDesPhotos());
        			projectTripDb.setDesPhotos(desPhotos);
        			projectTripDb.setTitle(projectTrip.getTitle());
        			projectTripDb.setDes(projectTrip.getDes());
        			projectTripDb.setProject(project);
        			projectService.savePojo(projectTripDb, user);
        		}
        	}
        	
        	mav.addObject("project", (Project) projectService.loadPojo(Project.class,project.getId()));
        	
        	mav.addObject(Constants.SUCCESS_MESSAGE, MessageConstants.SAVE_SUCCESS);
			
		}catch(Exception e){
			log.error(e);
			handleErrorModelAndView(mav, e);
		}
		return mav;
	}

	/**
	 * @param filePath     文件保存路径
	 * @param relativePath 文件相对路径
	 * @param files        文件对象
	 * @return
	 * @throws Exception
	 * @author 徐雁斌
	 */
	private FileGroup saveAndReturnFile(MultipartFile[] files,HttpServletRequest request,User user,FileGroup fileGroup) throws Exception {
		
		if(fileGroup == null){
			fileGroup = new FileGroup();
		}
		//绝对路径
		String filePath = request.getSession().getServletContext().getRealPath((String)
    			CustomizedPropertyPlaceholderConfigurer.getContextProperty(PROJECT_FILE))  + File.separator + user.getMobile();       	
    	//相对路径
    	String relativePath = CustomizedPropertyPlaceholderConfigurer.getContextProperty(PROJECT_FILE) + user.getMobile();
  
    	
		for(int i=0;i<files.length;i++){
			MultipartFile file = files[i];
			if(file.isEmpty()){
				continue;
			}
			
			FileInfo fileInfo = new FileInfo();
			fileInfo.setExt(FileUtil.getFileExtension(file.getOriginalFilename()));
			fileInfo.setRealName(file.getOriginalFilename());
			String fileName = UUID.randomUUID() + "." + fileInfo.getExt();
			fileInfo.setRealPath(relativePath + "/" + fileName);
			fileInfo.setFileGroup(fileGroup);
			
			fileGroup.getFileInfos().add(fileInfo);
			
			
			FileUtil.saveFileByName(filePath, file, fileName);
		}
		
		projectService.savePojo(fileGroup, user);
		
		return fileGroup;
	}
	
	/**
	 * 进入新建页面
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/toReview")
	public String toReview(String id,HttpServletRequest request, RedirectAttributes attr){
//		ModelAndView mav = new ModelAndView();
//		return reviewProject(id, request, mav);
		return "project/project_review";
	}

	/**
	 * @param id
	 * @param request
	 * @param mav
	 * @return
	 * @author 徐雁斌
	 */
	private ModelAndView reviewProject(String id, HttpServletRequest request, ModelAndView mav) {
		
		mav.setViewName("project/project_review");
		
		//查找浏览的活动
		Project project = (Project) projectService.loadPojo(Project.class,id);
		if(project == null){
			mav.addObject("errorMessage", ErrorMessage.get(ErrorCode.PROJECT_NOT_EXIST));
		}
		mav.addObject("project", project);
		try {
			//根据用户确定是否显示预定按钮
			User user = getSessionUser(request);
			mav.addObject("isCanYd",projectOrderService.isCanYd(user.getId(), id));
			mav.addObject("rela_projects", projectService.getRelaProject(id));
			
			//更新浏览次数
			String viewNumber = project.getViewedNumber();
			if(viewNumber == null || "".equals(viewNumber)){
				viewNumber = "0";
			}
			project.setViewedNumber(Integer.toString(Integer.parseInt(viewNumber) + 1));
			projectService.savePojo(project, user);
		} catch (ServiceException e) {
			log.error("ProjectController.toReview(): ServiceException", e);
			mav.addObject("errorMessage", e.getErrorMessage()); 
		}
		
		return mav;
	}
	
	/**
	 * 预定活动
	 * @param id
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/orderProject")
	public ModelAndView orderProject(String id,HttpServletRequest request, Model model){
		ModelAndView mav = new ModelAndView();
		try {
			//根据用户确定是否显示预定按钮
			User user = getSessionUser(request);
			projectOrderService.saveOrderProject(user.getId(), id);
			mav.addObject("successMessage", MessageConstants.ORDER_SUCCESS);
		} catch (ServiceException e) {
			log.error("ProjectController.orderProject(): ServiceException", e);
			mav.addObject("errorMessage", ErrorMessage.get(e.getErrorCode()));
		}
		
		return reviewProject(id, request, mav);
	}
	
	/**
	 * 获得用户发布的活动列表
	 * @param page
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getUserCreatedProject",produces = "text/html;charset=UTF-8")
	public ModelAndView getUserCreatedProject(String page,HttpServletRequest request, Model model){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("user/person_info/project_fb_item");
		try{
			User user = getSessionUser(request);
			
			//取得订单信息
			Pager pager= new Pager();
			pager.setPageSize(Constants.PAGER_PROJECT_FB);
			pager.setCurrentPage(Integer.parseInt(page));
			Expression exp = Condition.eq("createUser.id", user.getId());
			List<Project> projects = projectService.getPojoList(Project.class, pager, exp, new Sort("createTime",QueryConstants.DESC), user);
			mav.addObject("projects", projects);
		} catch (ServiceException e) {
			log.error("ProjectController.orderProject(): ServiceException", e);
			mav.addObject("errorMessage", ErrorMessage.get(e.getErrorCode()));
		}
		return mav;
	}
	
	/**
	 * 删除活动
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/delete")
	public ModelAndView delete(String id,HttpServletRequest request, Model model){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("project/project_create");
		
		if(!StringUtils.isEmpty(id)){
			mav.addObject("project", projectService.loadPojo(Project.class,id));
		}
		
		Project project = (Project) projectService.loadPojo(Project.class,id);
		try {
			projectService.deletePojo(project, null);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping(value="/test")
	public ModelAndView test(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		Project project = (Project) projectService.loadPojo(Project.class,"297ea9d44d31093d014d311c03cb000d");
		mv.addObject("project", project);
		mv.addObject("rela_projects",projectService.loadPojoByExpression(Project.class, null, null));
		mv.setViewName("project/project_review");
		return mv;
	}
	
	@RequestMapping(value="/test1")
	@ResponseBody
	public String test1(Pager page,HttpServletRequest request){
		System.out.println("page:" + page.getCurrentPage());
		return "<div class=\"scroll\">"
				        + "2222222222222222222<br>"    
				        + "2222222222222222222<br>"    
				        + "2222222222222222222<br>"    
				        + "2222222222222222222<br>"    
				        + "2222222222222222222<br>"    
				        + "2222222222222222222<br>"    
				        + "2222222222222222222<br>"    
				        + "2222222222222222222<br>"    
				        + "2222222222222222222<br>"    
				        + "2222222222222222222<br>"    
				        + "2222222222222222222<br>"    
				        + "2222222222222222222<br>"    
				        + "2222222222222222222<br>"    
				        + "2222222222222222222<br>"    
				        + "2222222222222222222<br>"    
				        + "2222222222222222222<br>"    
				        + "2222222222222222222<br>"    
		        + "</div>";
	}
	
	
}
