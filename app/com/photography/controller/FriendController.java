package com.photography.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.photography.exception.ServiceException;

/**
 * 派友
 * @author 徐雁斌
 * @since 2015-11-20
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Controller
@RequestMapping("/friend")
public class FriendController extends BaseController {

	@RequestMapping("/index")
	public ModelAndView toIndex(String errorMessage,HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("/friend/blog");
		return mav;
	}
	
	@RequestMapping("/toReview")
	public ModelAndView toReview(String id,HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("/friend/blog_review");
		return mav;
	}
	
	@RequestMapping("/toFriend")
	public ModelAndView toFriend(HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("/friend/blog_friend");
		return mav;
	}
	
	@RequestMapping("/toFriendSearch")
	public ModelAndView toFriendSearch(HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("/friend/blog_friend_search");
		return mav;
	}
}
