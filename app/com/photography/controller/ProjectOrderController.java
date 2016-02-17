package com.photography.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.photography.exception.ErrorMessage;
import com.photography.exception.ServiceException;
import com.photography.mapping.Project;
import com.photography.mapping.ProjectOrder;
import com.photography.mapping.User;
import com.photography.mapping.UserCoupon;
import com.photography.service.IProjectOrderService;
import com.photography.service.IUserCouponService;
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
	
	@Autowired
	private IUserCouponService userCouponService;

	public void setProjectOrderService(IProjectOrderService projectOrderService) {
		this.projectOrderService = projectOrderService;
	}
	
	public void setUserCouponService(IUserCouponService userCouponService) {
		this.userCouponService = userCouponService;
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
	public String toProjectCheckout(ProjectOrder projectOrder,HttpServletRequest request,RedirectAttributes attr){
		try{
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
			attr.addFlashAttribute("user", projectOrderService.loadPojo(User.class, user.getId()));
			attr.addFlashAttribute("projectOrder", projectOrder);
			
			return "project/project_checkout";
		}catch(Exception e){
			log.error("ProjectController toProjectCheckout",e);
			handleError(attr, e);
			String projectId = projectOrder.getProject().getId();
			return "redirect:/project/toReview?id=" + projectId;
		}
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
	public ModelAndView orderProject(ProjectOrder projectOrder,HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		try{
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
				userCoupon.setInOrExp(Constants.COUPON_CLASS_SPEND);
				userCoupon.setType(Constants.COUPON_TYPE_CONSUME_ORDERPROJECT);
				userCoupon.setUser(user);
				String userCouponNum = userCouponService.addCoupon(userCoupon, user);
				user.setCouponNum(userCouponNum);
			}
			
			mav.addObject("projectOrder", projectOrderDB);
			mav.setViewName("project/project_checkout_complete");
		}catch(Exception e){
			log.error("ProjectController orderProject",e);
			handleErrorModelAndView(mav, e);
			mav.addObject("projectOrder", projectOrder);
			mav.setViewName("project/project_checkout");
		}
		
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
