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
import com.photography.mapping.Work;
import com.photography.service.IAdminService;
import com.photography.service.IWorkService;
import com.photography.utils.Constants;

/**
 * 后台作品管理
 * @author 徐雁斌
 * @since 2015-11-18
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Controller
@RequestMapping("/admin/work")
public class AdminWorkController extends BaseController {

	@Resource
	private IAdminService adminService;
	
	@Autowired
	private IWorkService workService;

	public void setAdminService(IAdminService adminService) {
		this.adminService = adminService;
	}
	
	public void setWorkService(IWorkService workService) {
		this.workService = workService;
	}

	/**
	 * 跳转到活动审核列表
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/toWorkShList")
	public ModelAndView toWorkShList(HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		List<Work> works =  adminService.loadPojoByExpression(Work.class, Condition.eq("verify", Constants.VERIFY_ING), new Sort("createTime","desc"));
		mav.addObject("works", works);
		mav.setViewName("admin/main/work/list_sh");
		return mav;
	}
	
	/**
	 * 跳转到用户审核表单
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/toWorkShForm")
	public ModelAndView toWorkShForm(String id,HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		Work work =  adminService.loadPojo(Work.class, id);
		mav.addObject("work", work);
		mav.setViewName("admin/main/work/form_sh");
		return mav;
	}
	
	/**
	 * 审核通过操作
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/workSh")
	public String workSh(Work work,HttpServletRequest request, RedirectAttributes ra) {
		try{
			if(!StringUtils.isEmpty(work.getId())){
				Work workDB =  adminService.loadPojo(Work.class, work.getId());
				workDB.setVerify(work.getVerify());
				workDB.setVerifyMessage(work.getVerifyMessage());
				adminService.savePojo(workDB, null);
				
			}
		}catch(Exception e){
			handleError(ra, e);
		}
		return "redirect:toWorkShList";
	}
	
	/**
	 * 跳转到活动管理列表
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/toWorkGlList")
	public ModelAndView toWorkGlList(HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		List<Work> works =  adminService.loadPojoByExpression(Work.class, null, new Sort("createTime","desc"));
		mav.addObject("works", works);
		mav.setViewName("admin/main/work/list_gl");
		return mav;
	}
	
	/**
	 * 删除操作
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/deleteWork")
	public String deleteWork(String id,HttpServletRequest request, RedirectAttributes ra) {
		try{
			if(!StringUtils.isEmpty(id)){
				Work work = adminService.loadPojo(Work.class, id);
				if(work != null){
					workService.deletePojo(work, null);
				}
			}
		}catch(Exception e){
			handleError(ra, e);
		}
		return "redirect:toWorkGlList";
	}
}
