package com.photography.service;

import com.photography.exception.ServiceException;
import com.photography.mapping.User;



/**
 * 
 * @author 徐雁斌
 * @since 2015-2-4
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
public interface IUserService extends IBaseService{
	
	/**
	 * 登录
	 * @param userName
	 * @param Password
	 * @author 徐雁斌
	 */
	public User login(String mobile,String password) throws ServiceException;
	
	/**
	 * 通过手机号查询用户
	 * @param mobile
	 * @return
	 * @author 徐雁斌
	 */
	public User getByMobile(String mobile);

}
