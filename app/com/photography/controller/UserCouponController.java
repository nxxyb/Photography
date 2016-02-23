package com.photography.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.photography.dao.exp.Condition;
import com.photography.dao.exp.Expression;
import com.photography.exception.ServiceException;
import com.photography.mapping.User;
import com.photography.mapping.UserCoupon;
import com.photography.mapping.UserCouponSetting;
import com.photography.service.IUserCouponService;
import com.photography.utils.Constants;
import com.photography.utils.DateUtil;

@Controller
@RequestMapping("/userCoupon")
public class UserCouponController extends BaseController {
	
	@Autowired
	private IUserCouponService userCouponService;

	public IUserCouponService getUserCouponService() {
		return userCouponService;
	}

	public void setUserCouponService(IUserCouponService userCouponService) {
		this.userCouponService = userCouponService;
	}
	
	/**
	 * 签到
	 * @param request
	 * @param model
	 * @return
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/checkIn")
	@ResponseBody
	public String checkIn(HttpServletRequest request, Model model){
		
		User user = getSessionUser(request);
		if(user == null){
			return null;
		}
		
		UserCoupon userCoupon = new UserCoupon();
		userCoupon.setCouponNum(userCouponService.getUserCouponSetting(Constants.COUPON_TYPE_GIVE_SIGN).getNum());
		userCoupon.setInOrExp(Constants.COUPON_CLASS_INCOME);
		userCoupon.setType(Constants.COUPON_TYPE_GIVE_SIGN);
		userCoupon.setUser(user);
		try {
			String userCouponNum = userCouponService.addCoupon(userCoupon, user);
			user.setCouponNum(userCouponNum);
			setSessionUser(request, user);
			return userCouponNum;
		} catch (ServiceException e) {
			return user.getCouponNum();
		}
	}
	
	/**
	 * 查询用户是否签到
	 * @param request
	 * @param model
	 * @return
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/isCheckIn")
	@ResponseBody
	public String isCheckIn(HttpServletRequest request, Model model){
		
		User user = getSessionUser(request);
		if(user == null){
			return Constants.YES;
		}
		List<Expression> exps = new ArrayList<Expression>();
		exps.add(Condition.eq("user.id", user.getId()));
		exps.add(Condition.eq("type", Constants.COUPON_TYPE_GIVE_SIGN));
		exps.add(Condition.between("createTime", DateUtil.getDayBegin(), DateUtil.getDayEnd()));
		int count = userCouponService.getCountByQuery(UserCoupon.class, Condition.and(exps));
		if(count > 0){
			return Constants.YES;
		}else{
			return Constants.NO;
		}
	}
	
	/**
	 * 获取胶卷配置信息
	 * @param request
	 * @param model
	 * @return
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/getCouponSetting")
	@ResponseBody
	public List<UserCouponSetting> getCouponSetting(HttpServletRequest request, Model model){
		return userCouponService.getAllUserCouponSetting();
	}

}
