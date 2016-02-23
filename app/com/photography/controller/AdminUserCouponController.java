package com.photography.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.photography.mapping.UserCouponSetting;
import com.photography.service.IUserCouponService;

@Controller
@RequestMapping("/admin/userCoupon")
public class AdminUserCouponController extends BaseController {
	
	@Resource
	private IUserCouponService userCouponService;
	
	public void setUserCouponService(IUserCouponService userCouponService) {
		this.userCouponService = userCouponService;
	}


	/**
	 * 跳转到胶卷设置页面
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/toUserCouponSetting")
	public ModelAndView toUserCouponSetting(HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		List<UserCouponSetting> userCouponSettings =  userCouponService.loadPojoByExpression(UserCouponSetting.class, null, null);
		mav.addObject("userCouponSettings", userCouponSettings);
		mav.setViewName("admin/main/coupon/coupon_setting");
		return mav;
	}
	
	/**
	 * 保存胶卷设置
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/saveUserCouponSetting")
	public String saveUserCouponSetting(List<UserCouponSetting> userCouponSettings,HttpServletRequest request, RedirectAttributes ra) {
		try{
			if(userCouponSettings != null && !userCouponSettings.isEmpty()){
				
				
			}
		}catch(Exception e){
			handleError(ra, e);
			return "redirect:toUserCouponSetting";
		}
		return "redirect:toUserCouponSetting";
	}
}
