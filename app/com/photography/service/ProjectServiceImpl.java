package com.photography.service;

import org.springframework.stereotype.Service;

import com.photography.dao.IHibernateDao;
import com.photography.dao.IProjectDao;
import com.photography.mapping.BaseMapping;
import com.photography.mapping.Project;

/**
 * 
 * @author 徐雁斌
 * @since 2015-3-13
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Service("projectService")
public class ProjectServiceImpl extends BaseServiceImpl implements IProjectService {
	
	private IProjectDao projectDao;

	public void setProjectDao(IProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	/* 
	 * @see com.photography.service.IBaseService#getPojoClass()
	 */
	@Override
	public Class<? extends BaseMapping> getPojoClass() {
		return Project.class;
	}

	/* 
	 * @see com.photography.service.BaseServiceImpl#getDao()
	 */
	@Override
	public IHibernateDao getDao() {
		return projectDao;
	}

}
