package com.photography.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
import com.photography.utils.Constants;

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
		
		//将查询内容添加到session中
		request.getSession().setAttribute(Constants.SESSION_SEARCHSTRING, searchString);
		
		if(!StringUtils.isEmpty(searchString)){
		
			List<Project> projects =  indexSearchService.getIndexPojoList(Project.class, new Pager(), new String[]{"name","des"}, searchString, new Sort("createTime","desc"), user);
			mav.addObject("projects", projects);
			
			List<Work> works =  indexSearchService.getIndexPojoList(Work.class, new Pager(Constants.PAGER_SIZE_6), new String[]{"name","des"}, searchString, new Sort("createTime","desc"), user);
			mav.addObject("works", works);
		}else{
			mav.addObject("projects", new ArrayList<Project>());
			mav.addObject("works", new ArrayList<Work>());
		}
		
		mav.setViewName("/search/search");
		return mav;
	}
	
	/**
	 * 搜索作品 加载更多
	 */
	@RequestMapping("/searchWorkLoadMore")
	public ModelAndView searchWorkLoadMore(String block,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		User user = getSessionUser(request);
		String searchString = (String) request.getSession().getAttribute(Constants.SESSION_SEARCHSTRING);
		Pager pager = new Pager(Integer.parseInt(block)+1,Constants.PAGER_SIZE_6);
		List<Work> works =  indexSearchService.getIndexPojoList(Work.class, pager, new String[]{"name","des"}, searchString, new Sort("createTime","desc"), user);
		mav.addObject("works", works);
		mav.addObject("pager", pager);
		mav.addObject("block", block);
		mav.setViewName("work/work_loadmore");
		return mav;
	}
	
	/**
	 * 搜索作品 加载更多
	 */
	@RequestMapping("/searchProjectLoadMore")
	public ModelAndView searchProjectLoadMore(String block,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		User user = getSessionUser(request);
		String searchString = (String) request.getSession().getAttribute(Constants.SESSION_SEARCHSTRING);
		Pager pager = new Pager(Integer.parseInt(block)+1,Constants.PAGER_SIZE_6);
		List<Project> projects =  indexSearchService.getIndexPojoList(Project.class, pager, new String[]{"name","des"}, searchString, new Sort("createTime","desc"), user);
		mav.addObject("projects", projects);
		mav.addObject("pager", pager);
		mav.addObject("block", block);
		mav.setViewName("work/work_loadmore");
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
