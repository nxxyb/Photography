package com.photography.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.photography.dao.exp.Expression;
import com.photography.dao.query.Pager;
import com.photography.dao.query.QueryConstants;
import com.photography.dao.query.Sort;
import com.photography.exception.ServiceException;
import com.photography.mapping.Blog;
import com.photography.mapping.FileGroup;
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
	public ModelAndView toIndex(Pager pager,HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		Expression exp = null;
		List<Blog> blogs = blogService.getPojoList(Blog.class, pager, exp, new Sort("createTime",QueryConstants.DESC),null);
		mav.addObject("blogs", blogs);
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
	public ModelAndView toInfo(HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		
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
	public ModelAndView toFriend(HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		
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
	public ModelAndView toFriendSearch(HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		
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
	public ModelAndView toMy(HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("/friend/blog_my");
		return mav;
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
}
