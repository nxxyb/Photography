package com.photography.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.photography.dao.query.Sort;
import com.photography.mapping.AdminLb;
import com.photography.service.IAdminService;

/**
 * 后台管理-首页
 * @author 徐雁斌
 * @since 2015-11-3
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Controller
@RequestMapping("/admin/home")
public class AdminHomeController extends BaseController {
	
	@Resource
	private IAdminService adminService;

	public void setAdminService(IAdminService adminService) {
		this.adminService = adminService;
	}

	/**
	 * 跳转到轮播列表
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/tolb")
	public ModelAndView toLb(HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("admin/main/home/list_lb");
		return mav;
	}
	
	@RequestMapping(value="/getLbs",produces="application/json;charset=UTF-8")
	@ResponseBody
	public List<AdminLb> getLbs(HttpServletRequest request, Model model) {
		List<AdminLb> adminLbs = adminService.loadPojoByExpression(AdminLb.class, null, new Sort("sort","asc"));
		return adminLbs;
	}
}
