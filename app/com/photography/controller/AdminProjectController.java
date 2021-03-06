package com.photography.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.photography.dao.exp.Condition;
import com.photography.dao.query.Sort;
import com.photography.mapping.Project;
import com.photography.service.IAdminService;
import com.photography.service.IProjectService;
import com.photography.utils.Constants;

/**
 * 后台活动管理
 * @author 徐雁斌
 * @since 2015-11-6
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Controller
@RequestMapping("/admin/project")
public class AdminProjectController extends BaseController {

	@Resource
	private IAdminService adminService;
	
	@Autowired
	private IProjectService projectService;
	
	public void setProjectService(IProjectService projectService) {
		this.projectService = projectService;
	}

	public void setAdminService(IAdminService adminService) {
		this.adminService = adminService;
	}
	
	/**
	 * 跳转到活动审核列表
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/toProjectShList")
	public ModelAndView toProjectShList(HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		List<Project> projects =  adminService.loadPojoByExpression(Project.class, Condition.eq("verify", Constants.VERIFY_ING), new Sort("createTime","desc"));
		mav.addObject("projects", projects);
		mav.setViewName("admin/main/project/list_sh");
		return mav;
	}
	
	/**
	 * 跳转到用户审核表单
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/toProjectShForm")
	public ModelAndView toProjectShForm(String id,HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		Project project =  adminService.loadPojo(Project.class, id);
		mav.addObject("project", project);
		mav.setViewName("admin/main/project/form_sh");
		return mav;
	}
	
	/**
	 * 审核通过操作
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/projectSh")
	public String projectSh(Project project,HttpServletRequest request, RedirectAttributes ra) {
		try{
			if(!StringUtils.isEmpty(project.getId())){
				Project projectDB =  adminService.loadPojo(Project.class, project.getId());
				projectDB.setVerify(project.getVerify());
				projectDB.setVerifyMessage(project.getVerifyMessage());
				adminService.savePojo(projectDB, null);
				
			}
		}catch(Exception e){
			handleError(ra, e);
		}
		return "redirect:toProjectShList";
	}
	
	/**
	 * 跳转到活动管理列表
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/toProjectGlList")
	public ModelAndView toProjectGlList(HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		List<Project> projects =  adminService.loadPojoByExpression(Project.class, null, new Sort("createTime","desc"));
		mav.addObject("projects", projects);
		mav.setViewName("admin/main/project/list_gl");
		return mav;
	}
	
	/**
	 * 删除操作
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/deleteProject")
	public String deleteProject(String id,HttpServletRequest request, RedirectAttributes ra) {
		try{
			if(!StringUtils.isEmpty(id)){
				Project project = adminService.loadPojo(Project.class, id);
				if(project != null){
					projectService.deletePojo(project, null);
				}
			}
		}catch(Exception e){
			handleError(ra, e);
		}
		return "redirect:toProjectGlList";
	}
}
