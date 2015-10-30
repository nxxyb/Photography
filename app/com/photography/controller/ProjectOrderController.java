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
import com.photography.exception.ErrorMessage;
import com.photography.exception.ServiceException;
import com.photography.mapping.FileGroup;
import com.photography.mapping.Project;
import com.photography.mapping.ProjectComment;
import com.photography.mapping.ProjectOrder;
import com.photography.mapping.User;
import com.photography.mapping.UserCoupon;
import com.photography.service.IProjectOrderService;
import com.photography.utils.Constants;
import com.photography.utils.MessageConstants;

/**
 * 活动订单
 * @author 徐雁斌
 * @since 2015-5-27
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Controller
@RequestMapping("/projectorder")
public class ProjectOrderController extends BaseController {
	
	private final static Logger log = Logger.getLogger(ProjectOrderController.class);
	
	@Autowired
	private IProjectOrderService projectOrderService;

	public void setProjectOrderService(IProjectOrderService projectOrderService) {
		this.projectOrderService = projectOrderService;
	}
	
	/**
	 * 预定活动页面
	 * @param id
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/toProjectCheckout")
	public ModelAndView toProjectCheckout(ProjectOrder projectOrder,HttpServletRequest request, Model model) throws ServiceException{
		ModelAndView mav = new ModelAndView();
		User user = getSessionUser(request);
		if(projectOrder != null && !StringUtils.isEmpty(projectOrder.getId())){
			projectOrder = projectOrderService.loadPojo(ProjectOrder.class, projectOrder.getId());
		}else{
			if(projectOrder != null && projectOrder.getProject() != null && !StringUtils.isEmpty(projectOrder.getProject().getId())){
				Project project = projectOrderService.loadPojo(Project.class, projectOrder.getProject().getId());
				projectOrder.setProject(project);
			}
			projectOrder.setUser(user);
			projectOrder.setStatus(Constants.USER_ORDER_STATUS_WZF);
			projectOrderService.savePojo(projectOrder, user);
		}
		mav.addObject("projectOrder", projectOrder);
		
		mav.setViewName("project/project_checkout");
		return mav;
	}
	
	/**
	 * 用户支付
	 * @param page
	 * @param request
	 * @param model
	 * @return
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/orderProject",produces = "text/html;charset=UTF-8")
	public ModelAndView orderProject(ProjectOrder projectOrder,HttpServletRequest request) throws ServiceException{
		ModelAndView mav = new ModelAndView();
		
		User user = getSessionUser(request);
		
		ProjectOrder projectOrderDB = projectOrderService.loadPojo(ProjectOrder.class, projectOrder.getId());
		projectOrderDB.setCoupon(projectOrder.getCoupon());
		projectOrderDB.setOriginalPrice(projectOrder.getOriginalPrice());
		projectOrderDB.setUnitPrice(projectOrder.getUnitPrice());
		projectOrderDB.setActualPrice(projectOrder.getActualPrice());
		projectOrderDB.setStatus(Constants.USER_ORDER_STATUS_YZF);
		projectOrderService.savePojo(projectOrderDB, user);

		//使用胶卷,则生成胶卷使用记录
		if(!StringUtils.isEmpty(projectOrder.getCoupon()) && !"0".equals(projectOrder.getCoupon())){
			UserCoupon userCoupon = new UserCoupon();
			userCoupon.setCouponNum(projectOrder.getCoupon());
			userCoupon.setMessage(MessageConstants.COUPON_ORDERPROJECT);
			userCoupon.setType(Constants.COUPON_TYPE_SPEND);
			projectOrderService.savePojo(userCoupon, user);
		}
		
		mav.addObject("projectOrder", projectOrderDB);
		mav.setViewName("project/project_checkout_complete");
		return mav;
	}
	
	/**
	 * 取消订单
	 */
	@RequestMapping(value="/cancelProjectOrder")
	public String cancelProjectOrder(String id,HttpServletRequest request,RedirectAttributes attr){
		try{
			User user = getSessionUser(request);
			if(!StringUtils.isEmpty(id)){
				ProjectOrder projectOrder = projectOrderService.loadPojo(ProjectOrder.class, id);
				if(Constants.USER_ORDER_STATUS_WZF.equals(projectOrder.getStatus())){
					projectOrder.setStatus(Constants.USER_ORDER_STATUS_YQX);
					projectOrderService.savePojo(projectOrder, user);
				}
			}
			
			attr.addFlashAttribute(Constants.SUCCESS_MESSAGE, MessageConstants.SAVE_SUCCESS);
		}catch(Exception e){
			log.error("ProjectController saveProjectComment",e);
			attr.addFlashAttribute(Constants.ERROR_MESSAGE, ErrorMessage.getErrorMessage(e)); 
		}
		
		return "redirect:/userinfo/toUserInfo?type=4";
	}
}
