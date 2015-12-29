package com.photography.controller;

import java.util.List;

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

import com.photography.dao.exp.Condition;
import com.photography.dao.exp.Expression;
import com.photography.dao.query.Pager;
import com.photography.dao.query.QueryConstants;
import com.photography.dao.query.Sort;
import com.photography.exception.ErrorCode;
import com.photography.exception.ErrorMessage;
import com.photography.exception.ServiceException;
import com.photography.mapping.FileGroup;
import com.photography.mapping.User;
import com.photography.mapping.Work;
import com.photography.mapping.WorkCollect;
import com.photography.mapping.WorkComment;
import com.photography.service.IBaseService;
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
	
	@Autowired
	private IBaseService baseService;

	public void setWorkService(IWorkService workService) {
		this.workService = workService;
	}
	
	public void setBaseService(IBaseService baseService) {
		this.baseService = baseService;
	}

	/**
	 * 作品首页
	 */
	@RequestMapping("/index")
	public ModelAndView toIndex(Pager pager,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		pager.setPageSize(9);
		Expression exp = null;
		List<Work> works = workService.getPojoList(Work.class, pager, exp, new Sort("createTime",QueryConstants.DESC),null);
		mav.addObject("works", works);
		mav.setViewName("work/work");
		return mav;
	}
	
	/**
	 * 作品简介
	 */
	@RequestMapping("/introduction")
	public ModelAndView toIntroduction(String id,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(!StringUtils.isEmpty(id)){
			Work work = workService.loadPojo(Work.class, id);
			mav.addObject("work", work);
		}
		mav.setViewName("work/work_introduction");
		return mav;
	}
	
	/**
	 * 加载更多
	 */
	@RequestMapping("/loadmore")
	public ModelAndView loadMore(String block,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		Pager pager = new Pager(Integer.parseInt(block)+1,9);
		Expression exp = null;
		List<Work> works = workService.getPojoList(Work.class, pager, exp, new Sort("createTime",QueryConstants.DESC),null);
		mav.addObject("works", works);
		mav.addObject("pager", pager);
		mav.addObject("block", block);
		mav.setViewName("work/work_loadmore");
		return mav;
	}
	
	/**
	 * 进入详细页面
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/toReview")
	public ModelAndView toReview(String id,HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("work/work_review");
		if(!StringUtils.isEmpty(id)){
			Work work = (Work) workService.loadPojo(Work.class,id);
			mav.addObject("work", work);
		}
		List<Work> xgWorks = workService.getRelaWorks(id, new Pager(1,3));
		mav.addObject("xgWorks", xgWorks);
		//mav.addObject("path", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/work/toReview?id=" + id);
		return mav;
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
	 * @param work
	 * @param photoPics
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/create")
	public String create(Work work, @RequestParam MultipartFile[] photoPics,HttpServletRequest request, RedirectAttributes ra) {
		
		Work workDb = null;
		try {
			User user = getSessionUser(request);
			if(!StringUtils.isEmpty(work.getId())){
				workDb = (Work) workService.loadPojo(Work.class,work.getId());
			}else{
				workDb = work;
			}
			
			//判断是否上传滚动图片
			if(!checkFiles(photoPics, workDb.getPhotos())){
				throw new ServiceException(ErrorCode.WORK_PHOTO_NO_UPLOAD);
			}
			
        	FileGroup photoGroup = saveAndReturnFile(photoPics, request, user, workDb.getPhotos(),WORK_FILE,workService);
        	workDb.setPhotos(photoGroup);
        	workDb.setName(work.getName());
        	workDb.setDes(work.getDes());
        	
        	if(StringUtils.isEmpty(work.getId())){
        		workDb.setCreateUser(user);
        	}
        	
        	workDb.setVerify(Constants.VERIFY_ING);
        	
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
	
	
	/**
	 * 获取用户评论列表
	 * @param request
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/getWorkComment")
	public ModelAndView getWorkComment(String workId,Pager pager,HttpServletRequest request) throws ServiceException {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("work/work_comment_item");
		List<WorkComment> workComments = baseService.getPojoList(WorkComment.class, pager, Condition.eq("work.id", workId), new Sort("createTime",QueryConstants.DESC),null);
		mv.addObject("workId", workId);
		mv.addObject("pager", pager);
		mv.addObject("workComments", workComments);
		return mv;
	}
	
	/**
	 * 提交评论
	 */
	@RequestMapping(value="/saveWorkComment")
	public String saveWorkComment(WorkComment workComment, @RequestParam MultipartFile[] photoPics,HttpServletRequest request,RedirectAttributes attr){
		String workId = null;
		try{
			User user = getSessionUser(request);
			if(!StringUtils.isEmpty(workComment.getWork().getId())){
				workId = workComment.getWork().getId();
				Work work = (Work) baseService.loadPojo(Work.class,workId);
				workComment.setWork(work);
			}
			
			workComment.setCreateUser(user);
			
			FileGroup photos = saveAndReturnFile(photoPics, request, user, workComment.getPhotos(),WORK_FILE,baseService);
			workComment.setPhotos(photos);
			
			baseService.savePojo(workComment, user);
			
			attr.addFlashAttribute(Constants.SUCCESS_MESSAGE, MessageConstants.SAVE_SUCCESS);
		}catch(Exception e){
			log.error("WorkController saveWorkComment",e);
			attr.addFlashAttribute(Constants.ERROR_MESSAGE, ErrorMessage.getErrorMessage(e)); 
		}
		
		return "redirect:toReview?id=" + workId;
	}
	
	/**
	 * 收藏活动
	 * @param id
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/addCollect")
	public String addCollect(String id,HttpServletRequest request,RedirectAttributes attr){
		try{
			User user = getSessionUser(request);
			if(!StringUtils.isEmpty(id)){
				List<WorkCollect> workCollects = baseService.loadPojoByExpression(WorkCollect.class, 
						Condition.and(Condition.eq("work.id", id), Condition.eq("user.id", user.getId())), null);
				if(workCollects.isEmpty()){
					WorkCollect workCollect = new WorkCollect();
					workCollect.setWork(workService.loadPojo(Work.class, id));
					workCollect.setUser(user);
					baseService.savePojo(workCollect, user);
				}
			}
			attr.addFlashAttribute(Constants.SUCCESS_MESSAGE, MessageConstants.COLLECT_SUCCESS);
		}catch(Exception e){
			log.error("addCollect error",e);
			handleError(attr, e);
		}
		return "redirect:toReview?id=" + id;
	}

}
