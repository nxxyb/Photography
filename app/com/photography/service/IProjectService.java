package com.photography.service;

import java.util.List;

import com.photography.exception.ServiceException;
import com.photography.mapping.Project;

/**
 * 
 * @author 徐雁斌
 * @since 2015-3-13
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
public interface IProjectService extends IBaseService {
	
	/**
	 * 获取相关活动
	 * @param id
	 * @return
	 * @author 徐雁斌
	 */
	public List<Project> getRelaProject(String id) throws ServiceException;
}
