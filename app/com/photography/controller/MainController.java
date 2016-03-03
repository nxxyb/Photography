package com.photography.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.photography.dao.query.Pager;
import com.photography.dao.query.Sort;
import com.photography.exception.ServiceException;
import com.photography.mapping.Project;
import com.photography.mapping.User;
import com.photography.mapping.Work;
import com.photography.service.IIndexSearchService;
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
	
	@Autowired
	private IIndexSearchService indexSearchService;
	
	public void setProjectService(IProjectService projectService) {
		this.projectService = projectService;
	}
	
	public void setWorkService(IWorkService workService) {
		this.workService = workService;
	}

	@RequestMapping("/index")
	public ModelAndView toIndex(String errorMessage,HttpServletRequest request, RedirectAttributes attr) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		mav.addObject("lbProjects", projectService.getIndexProject("1"));
		mav.addObject("tjProjects", projectService.getIndexProject("2"));
		mav.addObject("rmProjects", projectService.getIndexProject("3"));
		mav.addObject("rmWorks", workService.getIndexWorks());
		
		mav.setViewName("/index");
		return mav;
	}
	
	@RequestMapping("/search")
	public ModelAndView toSearch(String searchString,HttpServletRequest request, RedirectAttributes attr) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		User user = getSessionUser(request);
		
		List<Project> projects =  indexSearchService.getIndexPojoList(Project.class, new Pager(), new String[]{"name","des"}, searchString, new Sort("createTime","desc"), user);
		mav.addObject("projects", projects);
		
		List<Work> works =  indexSearchService.getIndexPojoList(Work.class, new Pager(), new String[]{"name","des"}, searchString, new Sort("createTime","desc"), user);
		mav.addObject("works", works);
		
		mav.setViewName("/search/search");
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
