package com.photography.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.photography.dao.exp.Condition;
import com.photography.dao.exp.Expression;
import com.photography.dao.query.Pager;
import com.photography.dao.query.QueryConstants;
import com.photography.dao.query.Sort;
import com.photography.exception.ErrorCode;
import com.photography.exception.ErrorMessage;
import com.photography.exception.ServiceException;
import com.photography.mapping.Blog;
import com.photography.mapping.BlogComment;
import com.photography.mapping.BlogFriend;
import com.photography.mapping.CommentReploy;
import com.photography.mapping.FileGroup;
import com.photography.mapping.Love;
import com.photography.mapping.User;
import com.photography.service.IBlogService;
import com.photography.utils.Constants;
import com.photography.utils.MessageConstants;

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
	
	private final static Logger log = Logger.getLogger(FriendController.class);
	
	public final static String BLOG_FILE = "user.blog.file";
	
	@Autowired
	private IBlogService blogService;

	public void setBlogService(IBlogService blogService) {
		this.blogService = blogService;
	}

	/**
	 * 派友首页
	 * @param errorMessage
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping("/index")
	public ModelAndView toIndex(String searchBlog,Pager pager,HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		pager.setPageSize(6);
		Expression exp = null;
		if(!StringUtils.isEmpty(searchBlog)){
			exp = Condition.like("content", "%" + searchBlog + "%");
		}
		List<Blog> blogs = blogService.getPojoList(Blog.class, pager, exp, new Sort("createTime",QueryConstants.DESC),null);
		mav.addObject("blogs", blogs);
		mav.addObject("pager", pager);
		mav.addObject("searchBlog", searchBlog);
		
		//热门派文
		Sort rmSort = new Sort();
		rmSort.addSort("loveNum", "DESC");
		rmSort.addSort("commentNum", "DESC");
		List<Blog> reBlogs = blogService.getPojoList(Blog.class, new Pager(), null, new Sort("createTime",QueryConstants.DESC),null);
		mav.addObject("reBlogs", reBlogs);
		
		//最新派文
		List<Blog> zxBlogs = blogService.getPojoList(Blog.class, new Pager(), null, new Sort("createTime",QueryConstants.DESC),null);
		mav.addObject("zxBlogs", zxBlogs);
		
		//派友推荐
		List<User> recommendFriends = blogService.getRecommendFriends(getSessionUser(request),getSessionUser(request),new Pager(),null);
		mav.addObject("recommendFriends", recommendFriends);
		
		User sessionUser = getSessionUser(request);
		if(sessionUser != null){
			setSessionUser(request, blogService.loadPojo(User.class, sessionUser.getId()));
		}
		
		mav.setViewName("/friend/blog");
		return mav;
	}
	
	/**
	 * 查看派文
	 * @param id
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping("/toReview")
	public ModelAndView toReview(String id,HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		Blog blog = blogService.loadPojo(Blog.class, id);
		mav.addObject("blog", blog);
		
		List<BlogComment> blogComments = blogService.loadPojoByExpression(BlogComment.class, Condition.eq("blog.id", id), new Sort("createTime","DESC"));
		mav.addObject("blogComments", blogComments);
		
		//热门派文
		Sort rmSort = new Sort();
		rmSort.addSort("loveNum", "DESC");
		rmSort.addSort("commentNum", "DESC");
		List<Blog> reBlogs = blogService.getPojoList(Blog.class, new Pager(), null, new Sort("createTime",QueryConstants.DESC),null);
		mav.addObject("reBlogs", reBlogs);
		
		//最新派文
		List<Blog> zxBlogs = blogService.getPojoList(Blog.class, new Pager(), null, new Sort("createTime",QueryConstants.DESC),null);
		mav.addObject("zxBlogs", zxBlogs);
		
		//派友推荐
		List<User> recommendFriends = blogService.getRecommendFriends(getSessionUser(request),getSessionUser(request),new Pager(),null);
		mav.addObject("recommendFriends", recommendFriends);
		
		User sessionUser = getSessionUser(request);
		if(sessionUser != null){
			setSessionUser(request, blogService.loadPojo(User.class, sessionUser.getId()));
		}
		
		mav.setViewName("/friend/blog_review");
		return mav;
	}
	
	/**
	 * 基本信息
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping("/toInfo")
	public ModelAndView toInfo(String userId,HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		User user = null;
		if(!StringUtils.isEmpty(userId)){
			user = blogService.loadPojo(User.class, userId);
			mav.addObject("user", user);
		}
		User sessionUser = getSessionUser(request);
		if(sessionUser != null && user != null && !sessionUser.getId().equals(user.getId())){
			boolean isFriend = blogService.isFriends(user, sessionUser);
			if(isFriend){
				mav.addObject("isFriend", Constants.YES);
			}else{
				mav.addObject("isFriend", Constants.NO);
			}
		}
		mav.addObject("userId", userId);
		mav.setViewName("/friend/info");
		return mav;
	}
	
	/**
	 * 派友
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping("/toFriend")
	public ModelAndView toFriend(String userId,Pager pager,HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		List<BlogFriend> blogFriends = new ArrayList<BlogFriend>();
		User currentUser = null;
		if(!StringUtils.isEmpty(userId)){
			currentUser = blogService.loadPojo(User.class, userId);
			blogFriends = blogService.getFriends(currentUser, null, pager, new Sort("createTime","ASC"));
		}
		mav.addObject("blogFriends", blogFriends);
		mav.addObject("currentUser", currentUser);
		mav.addObject("userId", userId);
		mav.addObject("pager",pager);
		mav.setViewName("/friend/blog_friend");
		return mav;
	}
	
	/**
	 * 查找派友
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping("/toFriendSearch")
	public ModelAndView toFriendSearch(String searchText,String userId,Pager pager,HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		User user = null;
		if(!StringUtils.isEmpty(userId)){
			user = blogService.loadPojo(User.class, userId);
		}
		List<User> users = blogService.getRecommendFriends(user,getSessionUser(request),pager,searchText);
		mav.addObject("users", users);
		mav.addObject("userId", userId);
		mav.addObject("searchText", searchText);
		mav.setViewName("/friend/blog_friend_search");
		return mav;
	}
	
	/**
	 * 我的派文
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping("/toMy")
	public ModelAndView toMy(String userId,Pager pager,HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		pager.setPageSize(6);
		Expression exp = null;
		if(!StringUtils.isEmpty(userId)){
			exp = Condition.eq("createUser.id", userId);
		}
		List<Blog> blogs = blogService.getPojoList(Blog.class, pager, exp, new Sort("createTime",QueryConstants.DESC),null);
		mav.addObject("blogs", blogs);
		mav.addObject("pager", pager);
		mav.addObject("userId", userId);
		mav.setViewName("/friend/blog_my");
		return mav;
	}
	
	/**
	 * 评论派文
	 * @param blogComment
	 * @param request
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/deleteBlog")
	public String deleteBlog(String userId,String blogId, HttpServletRequest request, RedirectAttributes ra) {
		try{
			if(!StringUtils.isEmpty(blogId)){
				Blog blog = blogService.loadPojo(Blog.class,blogId);
				blogService.deletePojo(blog, getSessionUser(request));
			}
		}catch(Exception e){
			log.error("BlogController deleteBlog",e);
			handleError(ra, e);
		}
		return "redirect:toMy?userId=" + userId;
	}
	
	/**
	 * 新建派文
	 * @param project
	 * @param photoPics
	 * @param request
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/create")
	public String create(Blog blog, @RequestParam MultipartFile[] photoPics, HttpServletRequest request, RedirectAttributes ra) {
		try {
			User user = getSessionUser(request);
			
			Blog blogDb = null;
			if(!StringUtils.isEmpty(blog.getId())){
				blogDb = (Blog) blogService.loadPojo(Blog.class,blog.getId());
			}else{
				blogDb = blog;
			}
			
			
        	FileGroup photoGroup = saveAndReturnFile(photoPics, request, user, blogDb.getPhotos(),BLOG_FILE,blogService);
        	blogDb.setPhotos(photoGroup);
        	
        	if(StringUtils.isEmpty(blogDb.getId())){
        		blogDb.setCreateUser(user);
        	}
        	
        	blogService.savePojo(blogDb, user);
        	
        	ra.addFlashAttribute(Constants.SUCCESS_MESSAGE, MessageConstants.SAVE_SUCCESS);
			
		}catch(Exception e){
			log.error("BlogController create",e);
			handleError(ra, e);
			return "redirect:index";
		}
		return "redirect:index";
	}
	
	/**
	 * 评论派文
	 * @param blogComment
	 * @param request
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/saveComment")
	public String saveComment(BlogComment blogComment, HttpServletRequest request, RedirectAttributes ra) {
		try{
			if(!StringUtils.isEmpty(blogComment.getBlog().getId())){
				Blog blog = blogService.loadPojo(Blog.class,blogComment.getBlog().getId());
				blogComment.setBlog(blog);
				blogComment.setCreateUser(getSessionUser(request));
				blogService.savePojo(blogComment, getSessionUser(request));
			}
		}catch(Exception e){
			log.error("BlogController create",e);
			handleError(ra, e);
			return "redirect:toReview?id=" + blogComment.getBlog().getId();
		}
		return "redirect:toReview?id=" + blogComment.getBlog().getId();
	}
	
	/**
	 * 回复评论
	 * @param commentReploy
	 * @param request
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/reployComment")
	public String reployComment(CommentReploy commentReploy, HttpServletRequest request, RedirectAttributes ra) {
		BlogComment blogComment = null;
		try{
			if(!StringUtils.isEmpty(commentReploy.getBlogComment().getId())){
				blogComment = blogService.loadPojo(BlogComment.class,commentReploy.getBlogComment().getId());
				commentReploy.setBlogComment(blogComment);
				commentReploy.setCreateUser(getSessionUser(request));
				blogService.savePojo(commentReploy, getSessionUser(request));
			}
		}catch(Exception e){
			log.error("BlogController create",e);
			handleError(ra, e);
			if(blogComment == null){
				return "redirect:index";
			}else{
				return "redirect:toReview?id=" + blogComment.getBlog().getId();
			}
		}
		return "redirect:toReview?id=" + blogComment.getBlog().getId();
	}
	
	/**
	 * 获取用户信息弹出框
	 * @param userId
	 * @param request
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/getPopover")
	public ModelAndView getPopover(String userId, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		User user = null;
		String isFriend = Constants.YES;
		try{
			if(!StringUtils.isEmpty(userId)){
				user = blogService.loadPojo(User.class, userId);
			}
			mav.addObject("friendUser", user);
			
			User sessionUser = getSessionUser(request);
			if(sessionUser != null && !sessionUser.getId().equals(user.getId())){
				if(!blogService.isFriends(user, sessionUser)){
					isFriend = Constants.NO;
				}
			}
			mav.addObject("isFriend", isFriend);
		}catch(Exception e){
			log.error("BlogController create",e);
			return null;
		}
		mav.setViewName("/friend/blog_friend_popover");
		return mav;
	}
	
	/**
	 * 关注用户
	 * @param userId
	 * @param request
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/doFriend")
	@ResponseBody
	public String doFriend(String userId, HttpServletRequest request) {
		User user = null;
		try{
			if(!StringUtils.isEmpty(userId)){
				user = blogService.loadPojo(User.class, userId);
			}
			
			User sessionUser = getSessionUser(request);
			if(sessionUser != null && user != null && !sessionUser.getId().equals(user.getId())){
				BlogFriend bf = new BlogFriend();
				bf.setCreateUser(sessionUser);
				bf.setFriendUser(user);
				blogService.savePojo(bf, sessionUser);
			}
		}catch(Exception e){
			log.error("BlogController create",e);
			return getErrorMessage(e);
		}
		return Constants.YES;
	}
	
	/**
	 * 取消关注用户
	 * @param userId
	 * @param request
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/doCancelFriend")
	@ResponseBody
	public String doCancelFriend(String userId, HttpServletRequest request) {
		User user = null;
		try{
			if(!StringUtils.isEmpty(userId)){
				user = blogService.loadPojo(User.class, userId);
			}
			
			User sessionUser = getSessionUser(request);
			if(sessionUser != null && user != null && !sessionUser.getId().equals(user.getId())){
				BlogFriend bf = blogService.getFriend(sessionUser, user);
				blogService.deletePojo(bf, sessionUser);
			}
		}catch(Exception e){
			log.error("BlogController create",e);
			return getErrorMessage(e);
		}
		return Constants.YES;
	}
	
	/**
	 * 点赞
	 * @param blogFriendId
	 * @param request
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/clickLike",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String clickLike(String blogId, HttpServletRequest request) {
		try{
			User sessionUser = getSessionUser(request);
			if(sessionUser == null){
				return ErrorMessage.get(ErrorCode.SESSION_TIMEOUT);
			}
			
			if(!StringUtils.isEmpty(blogId)){
				int count = blogService.getCountByQuery(Love.class, Condition.and(Condition.eq("createUser.id", sessionUser.getId()),Condition.eq("blog.id", blogId)));
				if(count <= 0){
					Love love = new Love();
					love.setCreateUser(sessionUser);
					love.setBlog(blogService.loadPojo(Blog.class, blogId));
					blogService.savePojo(love, sessionUser);
				}else{
					return MessageConstants.HAVE_CLICK_LOVE;
				}
			}
		}catch(Exception e){
			log.error("BlogController create",e);
			return getErrorMessage(e);
		}
		return Constants.YES;
	}
	
	
	
	/**
	 * 取消关注用户
	 * @param userId
	 * @param request
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/cancelFriend")
	public String cancelFriend(String userId,String blogFriendId, HttpServletRequest request,RedirectAttributes ra) {
		try{
			if(!StringUtils.isEmpty(blogFriendId)){
				BlogFriend blogFriend = blogService.loadPojo(BlogFriend.class, blogFriendId);
				blogService.deletePojo(blogFriend, null);
			}
			ra.addFlashAttribute(Constants.SUCCESS_MESSAGE, MessageConstants.CANCEL_FRIEND_SUCCESS);
		}catch(Exception e){
			log.error("BlogController create",e);
			handleError(ra, e);
		}
		return "redirect:toFriend?userId=" +userId;
	}
	
	/**
	 * 关注用户
	 * @param userId
	 * @param request
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/friend")
	public String friend(String userId,String friendUserId, HttpServletRequest request,RedirectAttributes ra) {
		String result = doFriend(friendUserId,request);
		if(Constants.YES.equals(result)){
			ra.addFlashAttribute(Constants.SUCCESS_MESSAGE, MessageConstants.FRIEND_SUCCESS);
		}else{
			ra.addFlashAttribute(Constants.ERROR_MESSAGE, result);
		}
		return "redirect:toFriendSearch?userId=" +userId;
	}
}
