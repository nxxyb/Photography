package com.photography.service;

import com.photography.exception.ServiceException;


/**
 * 
 * @author 徐雁斌
 * @since 2015-5-19
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
public interface IProjectOrderService extends IBaseService {

	/**
	 * 判断用户是否可以预定该活动
	 * @param userId
	 * @param projectId
	 * @return 1-是  0-否
	 * @author 徐雁斌
	 */
	public String isCanYd(String userId,String projectId);
	
	
	/**
	 * 预定活动
	 * @param userId
	 * @param projectId
	 * @author 徐雁斌
	 */
	public void saveOrderProject(String userId, String projectId) throws ServiceException;
}
