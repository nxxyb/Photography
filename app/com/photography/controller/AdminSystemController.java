package com.photography.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.photography.cache.CacheHandler;
import com.photography.service.IIndexSearchService;
import com.photography.utils.MessageConstants;

@Controller
@RequestMapping("/admin/system")
public class AdminSystemController extends BaseController {
	
	@Resource
	private IIndexSearchService indexSearchService;
	
	@Resource
	private CacheHandler cacheHandler;
	
	/**
	 * 跳转到胶卷设置页面
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/toIndexAndCache")
	public ModelAndView toIndexAndCache(HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("admin/main/system/index_cache");
		return mav;
	}
	
	/**
	 * 建立索引
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/createIndex")
	public String createIndex(HttpServletRequest request, RedirectAttributes ra) {
		indexSearchService.createIndex();
		ra.addFlashAttribute("successMessage", MessageConstants.SAVE_SUCCESS);
		return "redirect:toIndexAndCache";
	}
	
	/**
	 * 清除活动缓存
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/clearProjectCache")
	public String clearProjectCache(HttpServletRequest request, RedirectAttributes ra) {
		cacheHandler.evictIndexProjectCache();
		ra.addFlashAttribute("successMessage", MessageConstants.SAVE_SUCCESS);
		return "redirect:toIndexAndCache";
	}
	
	/**
	 * 清除作品缓存
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/clearWorkCache")
	public String clearWorkCache(HttpServletRequest request, RedirectAttributes ra) {
		cacheHandler.evictIndexWorkCache();
		ra.addFlashAttribute("successMessage", MessageConstants.SAVE_SUCCESS);
		return "redirect:toIndexAndCache";
	}
	
	/**
	 * 清除胶卷设置缓存
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping("/clearUserCouponSettingCache")
	public String clearUserCouponSettingCache(HttpServletRequest request, RedirectAttributes ra) {
		cacheHandler.evictUserCouponSettingCache();
		ra.addFlashAttribute("successMessage", MessageConstants.SAVE_SUCCESS);
		return "redirect:toIndexAndCache";
	}

}
