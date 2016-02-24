package com.photography.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.photography.dao.exp.Condition;
import com.photography.exception.ErrorCode;
import com.photography.exception.ServiceException;
import com.photography.mapping.User;
import com.photography.mapping.UserCoupon;
import com.photography.mapping.UserCouponSetting;
import com.photography.mapping.UserCouponSettingRmb;
import com.photography.utils.Constants;
import com.photography.utils.StringUtil;

/**
 * 胶卷
 * @author 徐雁斌
 * @since 2015-10-29
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Service("userCouponService")
public class UserCouponServiceImpl extends BaseServiceImpl implements IUserCouponService {
	
	private final static Logger log = Logger.getLogger(UserCouponServiceImpl.class);

	@Override
	public String addCoupon(UserCoupon userCoupon, User createUser) throws ServiceException{
		if(userCoupon == null){
			log.error("userCoupon is null");
			throw new ServiceException(ErrorCode.COUPON_ADD_EXCEPTION);
		}
		
		if(userCoupon.getCouponNum() == null || userCoupon.getType() == null || userCoupon.getInOrExp() == null || userCoupon.getUser() == null){
			log.error("the userCoupon some parameter is null");
			throw new ServiceException(ErrorCode.COUPON_ADD_EXCEPTION);
		}
		
		int userCouponNum = 0;
		String userCouponNumStr = userCoupon.getUser().getCouponNum();
		if(!StringUtil.isEmpty(userCouponNumStr)){
			userCouponNum = Integer.parseInt(userCouponNumStr);
		}
		
		if(Constants.COUPON_CLASS_INCOME.equals(userCoupon.getInOrExp())){
			userCouponNum = userCouponNum + Integer.parseInt(userCoupon.getCouponNum());
		}else{
			if(userCouponNum < Integer.parseInt(userCoupon.getCouponNum())){
				log.error("the userCoupon not enough");
				throw new ServiceException(ErrorCode.COUPON_ADD_EXCEPTION);
			}
			userCouponNum = userCouponNum - Integer.parseInt(userCoupon.getCouponNum());
		}
		
		//更新用户胶卷
		User user = userCoupon.getUser();
		user.setCouponNum(Integer.toString(userCouponNum));
		savePojo(user, user);
		
		savePojo(userCoupon, createUser);
		
		return Integer.toString(userCouponNum);
	}
	
	/**
	 * 根据类型获取胶卷设置
	 * @param type
	 * @return
	 */
	@Cacheable(value="userCouponSettingCache")
	public UserCouponSetting getUserCouponSetting(String type){
		System.out.println("没用缓存");
		 List<UserCouponSetting> UserCouponSettings =  loadPojoByExpression(UserCouponSetting.class, Condition.eq("type", type), null);
		 if(UserCouponSettings.isEmpty()){
			 return null;
		 }
		 return UserCouponSettings.get(0);
	}
	
	/**
	 * 根据类型获取胶卷设置
	 * @return
	 */
	@Cacheable(value="userCouponSettingCache")
	public List<UserCouponSetting> getAllUserCouponSetting(){
		System.out.println("没用缓存");
		 List<UserCouponSetting> UserCouponSettings =  loadPojoByExpression(UserCouponSetting.class, null, null);
		 return UserCouponSettings;
	}
	
	/**
	 * 1胶卷兑换人名币设置
	 * @return
	 */
	public String getUserCouponSettingRmb(){
		UserCouponSettingRmb userCouponSettingRmb = loadPojoByExpression(UserCouponSettingRmb.class, null, null).get(0);
		return userCouponSettingRmb.getOneCouponToRmb();
	}

}
