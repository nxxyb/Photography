package com.photography.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.photography.dao.exp.Condition;
import com.photography.dao.exp.Expression;
import com.photography.dao.query.Pager;
import com.photography.dao.query.QueryConstants;
import com.photography.dao.query.Sort;
import com.photography.exception.ErrorMessage;
import com.photography.exception.ServiceException;
import com.photography.mapping.ProjectOrder;
import com.photography.mapping.User;
import com.photography.service.IProjectOrderService;
import com.photography.utils.Constants;

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
	 * 获得用户订购列表
	 * @param page
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getUserProjectOrder",produces = "text/html;charset=UTF-8")
	public ModelAndView getUserProjectOrder(String page,HttpServletRequest request, Model model){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("user/person_info/project_order_item");
		try{
			User user = getSessionUser(request);
			
			//取得订单信息
			Pager pager= new Pager();
			pager.setPageSize(Constants.PAGER_PROJECT_ORDER);
			pager.setCurrentPage(Integer.parseInt(page));
			Expression exp = Condition.eq("user.id", user.getId());
			List<ProjectOrder> projectOrders = projectOrderService.getPojoList(ProjectOrder.class, pager, exp, new Sort("createTime",QueryConstants.DESC), user);
			mav.addObject("projectOrders", projectOrders);
		} catch (ServiceException e) {
			log.error("ProjectController.getUserProjectOrder(): ServiceException", e);
			mav.addObject("errorMessage", ErrorMessage.get(e.getErrorCode()));
		}
		return mav;
	}
}
