package com.photography.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.photography.mapping.Work;
import com.photography.service.IWorkService;

/**
 * 作品
 * @author 徐雁斌
 * @since 2015-3-13
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Controller
@RequestMapping("/work")
public class WorkController extends BaseController {
	
	private final static Logger log = Logger.getLogger(WorkController.class);
	
	@Autowired
	private IWorkService workService;

	public void setWorkService(IWorkService workService) {
		this.workService = workService;
	}
	
	/**
	 * 作品首页
	 */
	@RequestMapping("/index")
	public String toIndex(String errorMessage,HttpServletRequest request, RedirectAttributes attr) {
		return "work/work";
	}
	
	/**
	 * 作品简介
	 */
	@RequestMapping("/introduction")
	public String toIntroduction(String errorMessage,HttpServletRequest request, RedirectAttributes attr) {
		return "work/work_introduction";
	}
	
	/**
	 * 加载更多
	 */
	@RequestMapping("/loadmore")
	public String loadMore(String errorMessage,HttpServletRequest request, RedirectAttributes attr) {
		return "work/work_loadmore";
	}
	
	/**
	 * 进入详细页面
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/toReview")
	public String toReview(String id,HttpServletRequest request, RedirectAttributes attr){
//		ModelAndView mav = new ModelAndView();
//		return reviewProject(id, request, mav);
		return "work/work_review";
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
		mav.setViewName("work/work_create");
		
		if(!StringUtils.isEmpty(id)){
			mav.addObject("work", workService.loadPojo(Work.class,id));
		}
		return mav;
	}

}