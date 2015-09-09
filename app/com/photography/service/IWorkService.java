package com.photography.service;

import java.util.List;

import com.photography.exception.ServiceException;
import com.photography.mapping.Work;

/**
 * 作品
 * @author Administrator
 *
 */
public interface IWorkService extends IBaseService {

	/**
	 * 获取相关活动
	 * @param id
	 * @return
	 * @author 徐雁斌
	 */
	public List<Work> getRelaWorks(String id) throws ServiceException;
	
	/**
	 * 获取首页活动
	 * @return
	 * @throws ServiceException
	 * @author 徐雁斌
	 */
	public List<Work> getIndexProject() throws ServiceException;
}
