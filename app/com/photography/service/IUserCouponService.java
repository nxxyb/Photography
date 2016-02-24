package com.photography.service;

import java.util.List;

import com.photography.exception.ServiceException;
import com.photography.mapping.User;
import com.photography.mapping.UserCoupon;
import com.photography.mapping.UserCouponSetting;

/**
 * 
 * @author 徐雁斌
 * @since 2015-10-29
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
public interface IUserCouponService extends IBaseService {
	
	/**
	 * 生成胶卷使用记录
	 * @param userCoupon
	 * @param createUser 创建用户
	 * @return 返回用户胶卷数
	 * @throws ServiceException
	 */
	public String addCoupon(UserCoupon userCoupon,User createUser) throws ServiceException;
	
	/**
	 * 根据类型获取胶卷设置(缓存)
	 * @param type
	 * @return
	 */
	public UserCouponSetting getUserCouponSetting(String type);
	
	/**
	 * 获取全部胶卷配置信息(缓存)
	 * @return
	 */
	public List<UserCouponSetting> getAllUserCouponSetting();
	
	/**
	 * 1胶卷兑换人名币设置(缓存)
	 * @return 1胶卷兑换人名币数量
	 */
	public String getUserCouponSettingRmb();

}
