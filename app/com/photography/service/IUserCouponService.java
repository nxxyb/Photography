package com.photography.service;

import com.photography.exception.ServiceException;
import com.photography.mapping.User;
import com.photography.mapping.UserCoupon;

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

}
