package com.photography.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.photography.exception.ServiceException;
import com.photography.mapping.Project;
import com.photography.mapping.User;
import com.photography.service.IProjectService;
import com.photography.utils.Constants;
import com.photography.utils.CustomizedPropertyPlaceholderConfigurer;
import com.photography.utils.FileUtil;

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
	 * 进入新建页面
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/toCreate")
	public ModelAndView toCreate(HttpServletRequest request, Model model){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("project/project_info");
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
	public ModelAndView create(Project project,@RequestParam MultipartFile[] imgFiles,HttpServletRequest request, Model model){
		ModelAndView mav = new ModelAndView();
		try{
			User user = (User) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
			projectService.savePojo(project, user);
			
			//绝对路径
			String filePath = request.getSession().getServletContext().getRealPath((String)
        			CustomizedPropertyPlaceholderConfigurer.getContextProperty(PROJECT_FILE))  + File.separator + user.getEmail();       	
        	//相对路径
        	String relativePath = CustomizedPropertyPlaceholderConfigurer.getContextProperty(PROJECT_FILE) + user.getEmail();
        	StringBuffer fileStrs = new StringBuffer();
        	for(MultipartFile file : imgFiles){
        		FileUtil.saveFile(filePath, file);
        		fileStrs.append(relativePath + "/" + file.getOriginalFilename());
        		fileStrs.append(",");
        	}
        	project.setPhotos(fileStrs.toString());
        	projectService.savePojo(project, user);
			
			mav.setViewName("project/project_info");
		}catch(Exception e){
			if(e instanceof ServiceException){
				ServiceException se = (ServiceException) e;
				String message = se.getErrorMessage();
				mav.addObject("errorMessage", message); 
				mav.setViewName("user/login");
			}else{
				log.error("login error",e);
				mav.addObject("error_message", CustomizedPropertyPlaceholderConfigurer.getContextProperty("error.unknown"));
				mav.setViewName("error/error");
			}
		}
		return mav;
	}

}
