package com.photography.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.photography.controller.view.ProjectOrderCoupon;
import com.photography.exception.ErrorCode;
import com.photography.exception.ErrorMessage;
import com.photography.exception.ServiceException;
import com.photography.mapping.Project;
import com.photography.mapping.ProjectOrder;
import com.photography.mapping.User;
import com.photography.mapping.UserCoupon;
import com.photography.service.IProjectOrderService;
import com.photography.service.IUserCouponService;
import com.photography.utils.Constants;
import com.photography.utils.DoubleUtil;
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
	public ModelAndView toProjectCheckout(ProjectOrder projectOrder,HttpServletRequest request,RedirectAttributes ra){
		ModelAndView mav = new ModelAndView();
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
			
			mav.addObject("projectOrder", projectOrder);
			mav.addObject("projectOrderCoupon", getEnableUserCoupon(projectOrder));
			
			mav.setViewName("project/project_checkout");
			
			throw new ServiceException(ErrorCode.PROJECT_NOT_EXIST);
			
			
		}catch(Exception e){
			mav = new ModelAndView();
			log.error("ProjectOrderController toProjectCheckout",e);
			handleError(ra, e);
			String projectId = projectOrder.getProject().getId();
			mav.setViewName("redirect:/project/toReview?id=" + projectId);
			return mav;
		}
	}
	
	//根据活计算可以使用的胶卷数量和抵扣钱数
	private ProjectOrderCoupon getEnableUserCoupon(ProjectOrder projectOrder){
		ProjectOrderCoupon projectOrderCoupon = new ProjectOrderCoupon();
		
		//胶卷
		if(projectOrder.getProject() != null && !StringUtils.isEmpty(projectOrder.getProject().getEnableCouponNum())){
			
			//如果用户胶卷数量为空，则按原始价格处理
			if(StringUtils.isEmpty(projectOrder.getUser().getCouponNum())){
				projectOrderCoupon.setEnableCouponNum("0");
				projectOrderCoupon.setUseCouponRmb("0");
			}
			
			//活动限制使用胶卷数量
			int limitCouponNum = Integer.parseInt(projectOrder.getProject().getEnableCouponNum());
			
			//1胶卷兑换人名币数量
			double oneCouponRmb = Double.parseDouble(userCouponService.getUserCouponSettingRmb());
			
			//用户胶卷数量
			int userCouponNum = Integer.parseInt(projectOrder.getUser().getCouponNum());
			
			//订购数量
			double num = Double.parseDouble(projectOrder.getNumber());
			
			//订购单价
			double cost = Double.parseDouble(projectOrder.getProject().getCost());
			
			//订购总量
			double totalCost = DoubleUtil.mul(num, cost);
			
			//免费活动不使用胶卷
			if(totalCost == 0){
				projectOrderCoupon.setIsUseCoupon(Constants.NO);
				projectOrderCoupon.setEnableCouponNum("0");
				projectOrderCoupon.setUseCouponRmb("0");
				return projectOrderCoupon;
			}
			
			//需要胶卷数量
			int enableCouponNum;
			
			//胶卷抵扣的钱数
			double useCouponRmb = 0;
			
			//活动使用胶卷无限制
			if(limitCouponNum == 0){
				
				enableCouponNum =  (int) DoubleUtil.div(totalCost, oneCouponRmb, 0);
				
				//如果用户胶卷小于需要胶卷数量，则按用户胶卷计算
				if(enableCouponNum > userCouponNum){
					enableCouponNum = userCouponNum;
				}
					
				useCouponRmb = DoubleUtil.mul(enableCouponNum, oneCouponRmb);
				
			}else{
				if(limitCouponNum > userCouponNum){
					enableCouponNum = userCouponNum;
				}else{
					enableCouponNum = limitCouponNum;
				}
				
				useCouponRmb = DoubleUtil.mul(enableCouponNum, oneCouponRmb);
				
			}
			
			projectOrderCoupon.setIsUseCoupon(Constants.YES);
			projectOrderCoupon.setEnableCouponNum(Integer.toString(enableCouponNum));
			projectOrderCoupon.setUseCouponRmb(Double.toString(useCouponRmb));
			return projectOrderCoupon;
		}else{
			projectOrderCoupon.setIsUseCoupon(Constants.NO);
			projectOrderCoupon.setEnableCouponNum("0");
			projectOrderCoupon.setUseCouponRmb("0");
			return projectOrderCoupon;
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
