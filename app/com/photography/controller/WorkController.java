package com.photography.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.photography.exception.ErrorCode;
import com.photography.exception.ServiceException;
import com.photography.mapping.FileGroup;
import com.photography.mapping.User;
import com.photography.mapping.Work;
import com.photography.service.IWorkService;
import com.photography.utils.Constants;
import com.photography.utils.MessageConstants;

/**
 * 作品
 * @author 徐雁斌
 * @since 2015-3-13
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Controller
@RequestMapping("/work")
public class WorkController extends BaseController {
	
	public final static String WORK_FILE = "user.work.file";
	
	private final static Logger log = Logger.getLogger(WorkController.class);
	
	@Autowired
	private IWorkService workService;

	public void setWorkService(IWorkService workService) {
		this.workService = workService;
	}
	
	/**
	 * 作品首页
	 */
	@RequestMapping("/index")
	public String toIndex(String errorMessage,HttpServletRequest request, RedirectAttributes attr) {
		return "work/work";
	}
	
	/**
	 * 作品简介
	 */
	@RequestMapping("/introduction")
	public String toIntroduction(String errorMessage,HttpServletRequest request, RedirectAttributes attr) {
		return "work/work_introduction";
	}
	
	/**
	 * 加载更多
	 */
	@RequestMapping("/loadmore")
	public String loadMore(String errorMessage,HttpServletRequest request, RedirectAttributes attr) {
		return "work/work_loadmore";
	}
	
	/**
	 * 进入详细页面
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/toReview")
	public String toReview(String id,HttpServletRequest request, RedirectAttributes attr){
//		ModelAndView mav = new ModelAndView();
//		return reviewProject(id, request, mav);
		return "work/work_review";
	}
	
	/**
	 * 进入新建页面
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/toCreate")
	public ModelAndView toCreate(String id,HttpServletRequest request, Model model){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("work/work_create");
		
		if(!StringUtils.isEmpty(id)){
			mav.addObject("work", workService.loadPojo(Work.class,id));
		}
		return mav;
	}
	
	/**
	 * 新建作品
	 * @param project
	 * @param imgFiles
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/create")
	public String create(Work work, @RequestParam MultipartFile[] photos,HttpServletRequest request, RedirectAttributes ra) {
		
		Work workDb = null;
		try {
			User user = getSessionUser(request);
			if(!StringUtils.isEmpty(work.getId())){
				workDb = (Work) workService.loadPojo(Work.class,work.getId());
			}else{
				workDb = work;
			}
			
			//判断是否上传滚动图片
			if(!checkFiles(photos, workDb.getPhotos())){
				throw new ServiceException(ErrorCode.WORK_PHOTO_NO_UPLOAD);
			}
			
        	FileGroup photoGroup = saveAndReturnFile(photos, request, user, workDb.getPhotos(),WORK_FILE,workService);
        	workDb.setPhotos(photoGroup);
        	workDb.setName(work.getName());
        	workDb.setDes(work.getDes());
        	workDb.setCreateUser(user);
        	
        	workService.savePojo(workDb, user);
        	ra.addFlashAttribute("type", "8");
        	ra.addFlashAttribute(Constants.SUCCESS_MESSAGE, MessageConstants.SAVE_SUCCESS);
			
		}catch(Exception e){
			log.error("WorkController create",e);
			handleError(ra, e);
			return "redirect:toCreate?id=" + workDb.getId();
		}
		return "redirect:/userinfo/toUserInfo";
	}

}
