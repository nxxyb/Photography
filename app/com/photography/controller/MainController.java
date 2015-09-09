package com.photography.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.photography.exception.ServiceException;
import com.photography.service.IProjectService;
import com.photography.service.IWorkService;

/**
 * 
 * @author 徐雁斌
 * @since 2015-2-4
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Controller
@RequestMapping("/")
public class MainController extends BaseController{
	
private final static Logger log = Logger.getLogger(MainController.class);
	
	@Autowired
	private IProjectService projectService;
	
	@Autowired
	private IWorkService workService;
	
	public void setProjectService(IProjectService projectService) {
		this.projectService = projectService;
	}
	
	public void setWorkService(IWorkService workService) {
		this.workService = workService;
	}

	@RequestMapping("/index")
	public ModelAndView toIndex(String type,HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.addObject("projects", projectService.getIndexProject(type));
			mav.addObject("works", workService.getIndexProject());
		} catch (ServiceException e) {
			log.error("MainController.toIndex(): ServiceException", e);
		}
		mav.setViewName("index");
		return mav;
	}
	
//	private List<Project> getIndexProjects(String type) throws ServiceException{
//			return  ;
		
//		Map<String,List<Project>> projectMap = new LinkedHashMap<String, List<Project>>();
//		projectMap.put(Constants.PROJECT_TYPE_CITYINSIDE_NAME, projectService.getIndexProject(Constants.PROJECT_TYPE_CITYINSIDE));
//		projectMap.put(Constants.PROJECT_TYPE_CITYOUTSIDE_NAME, projectService.getIndexProject(Constants.PROJECT_TYPE_CITYOUTSIDE));
//		projectMap.put(Constants.PROJECT_TYPE_SAIDPAT_NAME, projectService.getIndexProject(Constants.PROJECT_TYPE_SAIDPAT));
//		projectMap.put(Constants.PROJECT_TYPE_ONETOONE_NAME, projectService.getIndexProject(Constants.PROJECT_TYPE_ONETOONE));
//		return projectMap;
//	}
}
