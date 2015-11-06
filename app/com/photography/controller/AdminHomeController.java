package com.photography.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.photography.dao.exp.Condition;
import com.photography.dao.exp.Expression;
import com.photography.dao.query.Sort;
import com.photography.mapping.AdminLb;
import com.photography.mapping.Project;
import com.photography.mapping.User;
import com.photography.service.IAdminService;
import com.photography.utils.Constants;

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
	 * @param type 1:轮播  2：活动推荐  3：热门活动
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/tolb")
	public ModelAndView toLb(String type,HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		List<AdminLb> adminLbs = adminService.loadPojoByExpression(AdminLb.class, Condition.eq("type", type), new Sort("sort","asc"));
		
		Expression exp = Condition.ne("status", Constants.PROJECT_STATUS_WSH);
		if(!adminLbs.isEmpty()){
			for(AdminLb adminLb : adminLbs){
				exp = exp.and(Condition.ne("id", adminLb.getProject().getId()));
			}
		}
		List<Project> projects = adminService.loadPojoByExpression(Project.class, exp, new Sort("createTime","desc"));
		mav.addObject("type", type);
		mav.addObject("adminLbs", adminLbs);
		mav.addObject("projects", projects);
		mav.setViewName("admin/main/home/list_lb");
		return mav;
	}
	
	/**
	 * 跳转添加轮播表单
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/toLbForm")
	public ModelAndView toLbForm(String id,String type,HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		if(!StringUtils.isEmpty(id)){
			Project project = adminService.loadPojo(Project.class, id);
			mav.addObject("project", project);
		}
		mav.addObject("type", type);
		mav.setViewName("admin/main/home/form_lb");
		return mav;
	}
	
	/**
	 * 删除轮播
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/deleteLb")
	public String deleteLb(String id,String type,HttpServletRequest request, RedirectAttributes ra) {
		try{
			if(!StringUtils.isEmpty(id)){
				AdminLb adminLb = adminService.loadPojo(AdminLb.class, id);
				if(adminLb != null){
					adminService.deletePojo(adminLb, null);
				}
			}
		}catch(Exception e){
			handleError(ra, e);
		}
		return "redirect:tolb?type=" + type;
	}
	
	/**
	 * 添加轮播
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/addLb")
	public String addLb(AdminLb adminLb,HttpServletRequest request, RedirectAttributes ra) {
		try{
			if(!StringUtils.isEmpty(adminLb.getProject().getId())){
				Project project = adminService.loadPojo(Project.class, adminLb.getProject().getId());
				adminLb.setProject(project);
				adminService.savePojo(adminLb, null);
			}
		}catch(Exception e){
			handleError(ra, e);
		}
		return "redirect:tolb?type=" + adminLb.getType();
	}
	
}
