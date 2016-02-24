package com.photography.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.photography.cache.CacheHandler;
import com.photography.controller.view.UserCouponSettingList;
import com.photography.mapping.UserCouponSetting;
import com.photography.mapping.UserCouponSettingRmb;
import com.photography.service.IUserCouponService;
import com.photography.utils.MessageConstants;

@Controller
@RequestMapping("/admin/userCoupon")
public class AdminUserCouponController extends BaseController {
	
	@Resource
	private IUserCouponService userCouponService;
	
	@Resource
	private CacheHandler cacheHandler;
	
	public void setUserCouponService(IUserCouponService userCouponService) {
		this.userCouponService = userCouponService;
	}
	
	public void setCacheHandler(CacheHandler cacheHandler) {
		this.cacheHandler = cacheHandler;
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
		
		mav.addObject("userCouponSettingRmb", userCouponService.loadPojoByExpression(UserCouponSettingRmb.class, null, null).get(0));
		
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
	public String saveUserCouponSetting(UserCouponSettingList userCouponSettingList,HttpServletRequest request, RedirectAttributes ra) {
		try{
			boolean evictFlag = false;
			if(userCouponSettingList != null && userCouponSettingList.getUserCouponSettingRmb() != null){
				UserCouponSettingRmb userCouponSettingRmb = userCouponService.loadPojoByExpression(UserCouponSettingRmb.class, null, null).get(0);
				if(!userCouponSettingList.getUserCouponSettingRmb().getOneCouponToRmb().equals(userCouponSettingRmb.getOneCouponToRmb())){
					userCouponSettingRmb.setOneCouponToRmb(userCouponSettingList.getUserCouponSettingRmb().getOneCouponToRmb());
					userCouponService.savePojo(userCouponSettingRmb, null);
					evictFlag = true;
				}
			}
			
			if(userCouponSettingList != null && !userCouponSettingList.getUserCouponSettings().isEmpty()){
				for(UserCouponSetting userCouponSetting : userCouponSettingList.getUserCouponSettings()){
					UserCouponSetting userCouponSettingDB = userCouponService.loadPojo(UserCouponSetting.class, userCouponSetting.getId());
					if(!userCouponSettingDB.getNum().equals(userCouponSetting.getNum()) || !userCouponSettingDB.getMessage().equals(userCouponSetting.getMessage())){
						userCouponSettingDB.setNum(userCouponSetting.getNum());
						userCouponSettingDB.setMessage(userCouponSetting.getMessage());
						userCouponService.savePojo(userCouponSettingDB, null);
						evictFlag = true;
					}
				}
			}
			
			if(evictFlag){
				cacheHandler.evictUserCouponSettingCache();
			}
			ra.addFlashAttribute("successMessage", MessageConstants.SAVE_SUCCESS);
		}catch(Exception e){
			handleError(ra, e);
			return "redirect:toUserCouponSetting";
		}
		return "redirect:toUserCouponSetting";
	}
}
