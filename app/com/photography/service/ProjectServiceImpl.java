package com.photography.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.photography.dao.exp.Condition;
import com.photography.dao.query.Pager;
import com.photography.dao.query.Sort;
import com.photography.exception.ErrorCode;
import com.photography.exception.ServiceException;
import com.photography.mapping.Project;

/**
 * 
 * @author 徐雁斌
 * @since 2015-3-13
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@SuppressWarnings("unchecked")
@Service("projectService")
public class ProjectServiceImpl extends BaseServiceImpl implements IProjectService {

	/* 
	 * @see com.photography.service.IProjectService#getRelaProject(java.lang.String)
	 */
	@Override
	public List<Project> getRelaProject(String id) throws ServiceException {
		Project project = hibernateDao.loadById(Project.class, id);
		if(project == null){
			throw new ServiceException(ErrorCode.PROJECT_NOT_EXIST);
		}
		
		Pager pager= new Pager();
		pager.setPageSize(5);
		List<Project> projects = hibernateDao.getByQuery(Project.class, pager,null, new Sort());
		return projects;
	}
	
	@Override
	public List<Project> getIndexProject(String type) throws ServiceException {
		Pager pager= new Pager();
		pager.setPageSize(4);
		List<Project> projects = hibernateDao.getByQuery(Project.class, pager,Condition.eq("type", type), new Sort());
		return projects;
	}

}
