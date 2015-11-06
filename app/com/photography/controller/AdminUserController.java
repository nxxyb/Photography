package com.photography.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.photography.dao.exp.Condition;
import com.photography.dao.query.Sort;
import com.photography.mapping.User;
import com.photography.service.IAdminService;
import com.photography.utils.Constants;
import com.photography.utils.MD5Util;

/**
 * 
 * @author 徐雁斌
 * @since 2015-11-5
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Controller
@RequestMapping("/admin/user")
public class AdminUserController extends BaseController {
	
	@Resource
	private IAdminService adminService;

	public void setAdminService(IAdminService adminService) {
		this.adminService = adminService;
	}
	
	/**
	 * 跳转到用户审核列表
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/toUserShList")
	public ModelAndView toUserShList(HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		List<User> users =  adminService.loadPojoByExpression(User.class, Condition.eq("verify", Constants.VERIFY_ING), new Sort("verifyTime","desc"));
		mav.addObject("users", users);
		mav.setViewName("admin/main/user/list_sh");
		return mav;
	}
	
	/**
	 * 跳转到用户审核列表
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/toUserShForm")
	public ModelAndView toUserShForm(String id,HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		User user =  adminService.loadPojo(User.class, id);
		mav.addObject("user", user);
		mav.setViewName("admin/main/user/form_sh");
		return mav;
	}
	
	/**
	 * 审核操作
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/userSh")
	public String userSh(User user,HttpServletRequest request, RedirectAttributes ra) {
		try{
			if(user != null && !StringUtils.isEmpty(user.getId())){
				User userDB =  adminService.loadPojo(User.class, user.getId());
				userDB.setVerify(user.getVerify());
				userDB.setVerifyMessage(user.getVerifyMessage());
				adminService.savePojo(userDB, null);
				
			}
		}catch(Exception e){
			handleError(ra, e);
			return "redirect:toUserShForm?id=" + user.getId();
		}
		return "redirect:toUserShList";
	}
	
	/**
	 * 跳转到用户管理列表
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/toUserGlList")
	public ModelAndView toUserGlList(HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		List<User> users =  adminService.loadPojoByExpression(User.class, null, new Sort("createTime","desc"));
		mav.addObject("users", users);
		mav.setViewName("admin/main/user/list_gl");
		return mav;
	}
	
	/**
	 * 跳转到用户管理表单
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/toUserGlForm")
	public ModelAndView toUserGlForm(String id,HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		if(!StringUtils.isEmpty(id)){
			User user =  adminService.loadPojo(User.class, id);
			mav.addObject("user", user);
		}
		mav.setViewName("admin/main/user/form_gl");
		return mav;
	}
	
	/**
	 * 用户保存操作
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/userSave")
	public String userSave(User user,HttpServletRequest request, RedirectAttributes ra) {
		try{
			if(user != null && !StringUtils.isEmpty(user.getId())){
				User userDB =  adminService.loadPojo(User.class, user.getId());
				userDB.setRealName(user.getRealName());
				userDB.setMobile(user.getMobile());
				userDB.setEnable(user.getEnable());
				userDB.setType(user.getType());
				if(!StringUtils.isEmpty(user.getPassword())){
					userDB.setPassword(MD5Util.md5(user.getPassword()));
				}
				adminService.savePojo(userDB, null);
				
			}else{
				user.setPassword(MD5Util.md5(user.getPassword()));
				adminService.savePojo(user, null);
			}
		}catch(Exception e){
			handleError(ra, e);
			return "redirect:toUserGlForm?id=" + user.getId();
		}
		return "redirect:toUserGlList";
	}
	
	/**
	 * 删除操作
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/deleteUser")
	public String deleteUser(String id,HttpServletRequest request, RedirectAttributes ra) {
		try{
			if(!StringUtils.isEmpty(id)){
				User user = adminService.loadPojo(User.class, id);
				if(user != null){
					adminService.deletePojo(user, null);
				}
			}
		}catch(Exception e){
			handleError(ra, e);
			return "redirect:toUserGlForm?id=" + id;
		}
		return "redirect:toUserGlList";
	}

}
