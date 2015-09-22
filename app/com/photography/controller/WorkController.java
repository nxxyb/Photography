package com.photography.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

}
