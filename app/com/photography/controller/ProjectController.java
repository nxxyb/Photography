package com.photography.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.photography.mapping.Project;
import com.photography.mapping.ProjectCollect;
import com.photography.mapping.ProjectComment;
import com.photography.mapping.ProjectOrder;
import com.photography.mapping.ProjectTrip;
import com.photography.mapping.User;
import com.photography.service.IProjectService;
import com.photography.utils.Constants;
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
	
	public void setProjectService(IProjectService projectService) {
		this.projectService = projectService;
	}
	
	/**
	 * 活动首页
	 */
	@RequestMapping("/index")
	public ModelAndView toIndex(Pager pager,String type,HttpServletRequest request,Model model) {
		ModelAndView mav = new ModelAndView();
		Expression exp = null;
		if(!StringUtils.isEmpty(type)){
			exp = Condition.eq("type", type);
			mav.addObject("type", type);
		}
		List<Project> projects = projectService.getPojoList(Project.class, pager, exp, new Sort("createTime",QueryConstants.DESC),null);
		mav.addObject("projects", projects);
		mav.setViewName("project/project");
		return mav;
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
	public String create(Project project, @RequestParam MultipartFile[] photoPics, @RequestParam MultipartFile[] desPhotoPics, 
			ProjectTripList tripList, HttpServletRequest request, RedirectAttributes ra) {
		
		try {
			User user = getSessionUser(request);
			
			Project projectDb = null;
			if(!StringUtils.isEmpty(project.getId())){
				projectDb = (Project) projectService.loadPojo(Project.class,project.getId());
			}else{
				projectDb = project;
			}
			
			//判断是否上传滚动图片
			if(!checkFiles(photoPics, projectDb.getPhotos())){
				throw new ServiceException(ErrorCode.PROJECT_PHOTO_NO_UPLOAD);
			}
        	FileGroup photoGroup = saveAndReturnFile(photoPics, request, user, projectDb.getPhotos(),PROJECT_FILE,projectService);
        	project.setPhotos(photoGroup);
        	
        	FileGroup desPhotoGroup = saveAndReturnFile(desPhotoPics, request, user, projectDb.getDesPhotos(),PROJECT_FILE,projectService);
        	project.setDesPhotos(desPhotoGroup);
        	
        	if(StringUtils.isEmpty(project.getId())){
        		project.setCreateUser(user);
        	}
        	
        	projectService.savePojo(project, user);
        	
        	//处理行程
        	List<ProjectTrip> oldProjectTrips = new ArrayList<ProjectTrip>();
        	if(!StringUtils.isEmpty(project.getId())){
        		oldProjectTrips = projectService.loadPojoByExpression(ProjectTrip.class, Condition.eq("project.id", projectDb.getId()), new Sort("createTime","ASC"));
        	}
        	
        	List<ProjectTrip> addProjectTrips = new ArrayList<ProjectTrip>();
        	if(tripList != null && tripList.getTrips() != null && !tripList.getTrips().isEmpty()){
        		for(ProjectTrip projectTrip : tripList.getTrips()){
        			ProjectTrip projectTripDb = null;
        			if(!StringUtils.isEmpty(projectTrip.getId())){
        				projectTripDb = projectService.loadPojo(ProjectTrip.class,projectTrip.getId());
        			}else{
        				projectTripDb = projectTrip;
        			}
        			FileGroup desPhotos = saveAndReturnFile(projectTrip.getDesPhotoPics(), request, user, projectTripDb.getDesPhotos(),PROJECT_FILE,projectService);
        			projectTripDb.setDesPhotos(desPhotos);
        			projectTripDb.setTitle(projectTrip.getTitle());
        			projectTripDb.setDes(projectTrip.getDes());
        			projectTripDb.setProject(project);
        			projectService.savePojo(projectTripDb, user);
        			addProjectTrips.add(projectTripDb);
        		}
        		
        		//删除以前的行程
        		
        		for(ProjectTrip pt:oldProjectTrips){
        			if(!addProjectTrips.contains(pt)){
//        				oldProjectTrips.remove(pt);
        				projectService.deletePojo(pt, user);
        			}
        		}
        		project.setProjectTrips(addProjectTrips);	
        		
        	}
        	project.setVerify(Constants.VERIFY_ING);
        	projectService.savePojo(project, user);
        	ra.addFlashAttribute("type", "7");
        	ra.addFlashAttribute(Constants.SUCCESS_MESSAGE, MessageConstants.SAVE_SUCCESS);
			
		}catch(Exception e){
			log.error("ProjectController create",e);
			handleError(ra, e);
			return "redirect:toCreate?id=" + project.getId();
		}
		return "redirect:/userinfo/toUserInfo";
	}
	
	/**
	 * 进入查看页面
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/toReview")
	public ModelAndView toReview(String id,HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("project/project_review");
		if(!StringUtils.isEmpty(id)){
			Project project = (Project) projectService.loadPojo(Project.class,id);
			mav.addObject("project", project);
			
			mav.addObject("pjNum", projectService.getCountByQuery(ProjectComment.class, Condition.eq("project.id", project.getId())));
			mav.addObject("ydNum", projectService.getCountByQuery(ProjectOrder.class, Condition.eq("project.id", project.getId()).and(Condition.eq("status", Constants.USER_ORDER_STATUS_YZF))));
		}
		return mav;
	}
	
	/**
	 * 获取用户评论列表
	 * @param request
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/getProjectComment")
	public ModelAndView getProjectComment(String projectId,Pager pager,HttpServletRequest request) throws ServiceException {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("project/project_comment_item");
		List<ProjectComment> projectComments = projectService.getPojoList(ProjectComment.class, pager, Condition.eq("project.id", projectId), new Sort("createTime",QueryConstants.DESC),null);
		mv.addObject("projectId", projectId);
		mv.addObject("pager", pager);
		mv.addObject("projectComments", projectComments);
		return mv;
	}
	
	/**
	 * 提交评论
	 */
	@RequestMapping(value="/saveProjectComment")
	public String saveProjectComment(ProjectComment projectComment, @RequestParam MultipartFile[] photoPics,HttpServletRequest request,RedirectAttributes attr){
		String projectId = null;
		try{
			User user = getSessionUser(request);
			if(!StringUtils.isEmpty(projectComment.getProject().getId())){
				projectId = projectComment.getProject().getId();
				Project project = (Project) projectService.loadPojo(Project.class,projectId);
				projectComment.setProject(project);
			}
			
			projectComment.setCreateUser(user);
			
			FileGroup photos = saveAndReturnFile(photoPics, request, user, projectComment.getPhotos(),PROJECT_FILE,projectService);
			projectComment.setPhotos(photos);
			
			projectService.savePojo(projectComment, user);
			
			attr.addFlashAttribute(Constants.SUCCESS_MESSAGE, MessageConstants.SAVE_SUCCESS);
		}catch(Exception e){
			log.error("ProjectController saveProjectComment",e);
			attr.addFlashAttribute(Constants.ERROR_MESSAGE, ErrorMessage.getErrorMessage(e)); 
		}
		
		return "redirect:toReview?id=" + projectId;
	}
	
	/**
	 * 获取用户预定活动列表
	 * @param request
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/getProjectOrder")
	public ModelAndView getProjectOrder(String projectId,Pager pager,HttpServletRequest request) throws ServiceException {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("project/project_order_item");
		List<ProjectOrder> projectOrders = projectService.getPojoList(ProjectOrder.class, pager, Condition.eq("project.id", projectId).and(Condition.eq("status", Constants.USER_ORDER_STATUS_YZF)), new Sort("createTime",
				QueryConstants.DESC), null);
		mv.addObject("projectId", projectId);
		mv.addObject("pager", pager);
		mv.addObject("projectOrders", projectOrders);
		return mv;
	}
	
	
	/**
	 * 收藏活动
	 * @param id
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/addCollect")
	public String addCollect(String id,HttpServletRequest request,RedirectAttributes attr){
		try{
			User user = getSessionUser(request);
			if(!StringUtils.isEmpty(id)){
				List<ProjectCollect> projectCollects = projectService.loadPojoByExpression(ProjectCollect.class, 
						Condition.and(Condition.eq("project.id", id), Condition.eq("user.id", user.getId())), null);
				if(projectCollects.isEmpty()){
					ProjectCollect projectCollect = new ProjectCollect();
					projectCollect.setProject(projectService.loadPojo(Project.class, id));
					projectCollect.setUser(user);
					projectService.savePojo(projectCollect, user);
				}
			}
			attr.addFlashAttribute(Constants.SUCCESS_MESSAGE, MessageConstants.COLLECT_SUCCESS);
		}catch(Exception e){
			log.error("addCollect error",e);
			handleError(attr, e);
		}
		return "redirect:toReview?id=" + id;
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
