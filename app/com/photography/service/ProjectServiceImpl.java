package com.photography.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.photography.cache.CacheHandler;
import com.photography.dao.exp.Condition;
import com.photography.dao.query.Pager;
import com.photography.dao.query.Sort;
import com.photography.exception.ServiceException;
import com.photography.mapping.AdminLb;
import com.photography.mapping.BaseMapping;
import com.photography.mapping.Project;
import com.photography.mapping.User;

/**
 * 
 * @author 徐雁斌
 * @since 2015-3-13
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Service("projectService")
public class ProjectServiceImpl extends BaseServiceImpl implements IProjectService {
	
	@Resource
	private CacheHandler cacheHandler;

	/* 
	 * @see com.photography.service.IProjectService#getRelaProject(java.lang.String)
	 */
	@Override
	public List<Project> getRelaProject(String id,Pager pager) {
//		Project project = hibernateDao.loadById(Project.class, id);
//		if(project == null){
//			throw new ServiceException(ErrorCode.PROJECT_NOT_EXIST);
//		}
		
		List<Project> projects = hibernateDao.getByQuery(Project.class, pager,null, new Sort());
		return projects;
	}
	
	@Override
	@Cacheable(value="indexProjectCache")
	public List<Project> getIndexProject(String type) throws ServiceException {
		List<AdminLb> adminLbs = hibernateDao.getByQuery(AdminLb.class, Condition.eq("type", type), new Sort("sort","asc"));
		List<Project> projects = new ArrayList<Project>();
		for(AdminLb adminLb : adminLbs){
			projects.add(adminLb.getProject());
		}
		return projects;
	}
	
	@Override
	public void savePojo(BaseMapping pojo, User user) throws ServiceException {
		
		//更新轮播缓存
		if(StringUtils.isEmpty(pojo.getId())){
			List<AdminLb> adminLbs = hibernateDao.getByQuery(AdminLb.class, Condition.eq("project.id", pojo.getId()));
			if(!adminLbs.isEmpty()){
				cacheHandler.evictIndexProjectCache();
			}
		}
		
		super.savePojo(pojo, user);
	}

	@Override
	public void deletePojo(BaseMapping pojo, User user) throws ServiceException {
		//删除预定记录
		hibernateDao.executeUpdate("delete project_order where project.id='" + pojo.getId() + "'");
		
		//删除收藏
		hibernateDao.executeUpdate("delete project_collect where project.id='" + pojo.getId() + "'");
		
		//删除评论
		hibernateDao.executeUpdate("delete project_comment where project.id='" + pojo.getId() + "'");
		
		//删除轮播
		List<AdminLb> adminLbs = hibernateDao.getByQuery(AdminLb.class, Condition.eq("project.id", pojo.getId()));
		if(!adminLbs.isEmpty()){
			hibernateDao.delete(adminLbs.get(0));
			cacheHandler.evictIndexProjectCache();
		}
		
		super.deletePojo(pojo, user);
	}
}
