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
import com.photography.mapping.User;
import com.photography.mapping.Work;

@Service("workService")
public class WorkServiceImpl extends BaseServiceImpl implements IWorkService {

	@Resource
	private CacheHandler cacheHandler;
	
	@Override
	public List<Work> getRelaWorks(String id,Pager pager){
		Work work = hibernateDao.loadById(Work.class, id);
		if(work == null){
//			throw new ServiceException(ErrorCode.WORK_NOT_EXIST);
		}
		
		List<Work> works = hibernateDao.getByQuery(Work.class, pager,null, new Sort());
		return works;
	}

	@Override
	@Cacheable(value="indexWorkCache")
	public List<Work> getIndexWorks() throws ServiceException {
		List<AdminLb> adminLbs = hibernateDao.getByQuery(AdminLb.class, Condition.eq("type", "4"), new Sort("sort","asc"));
		List<Work> works = new ArrayList<Work>();
		for(AdminLb adminLb : adminLbs){
			works.add(adminLb.getWork());
		}
		return works;
	}
	
	@Override
	public void savePojo(BaseMapping pojo, User user) throws ServiceException {
		//更新轮播缓存
		if(StringUtils.isEmpty(pojo.getId())){
			List<AdminLb> adminLbs = hibernateDao.getByQuery(AdminLb.class, Condition.eq("work.id", pojo.getId()));
			if(!adminLbs.isEmpty()){
				cacheHandler.evictIndexWorkCache();
			}
		}
		
		super.savePojo(pojo, user);
	}
	
	@Override
	public void deletePojo(BaseMapping pojo, User user) throws ServiceException {
		//删除预定记录
//		hibernateDao.executeUpdate("delete work_order where work.id='" + pojo.getId() + "'");
		
		//删除收藏
		hibernateDao.executeUpdate("delete work_collect where work.id='" + pojo.getId() + "'");
		
		//删除评论
		hibernateDao.executeUpdate("delete work_comment where work.id='" + pojo.getId() + "'");
		
		//删除轮播
		List<AdminLb> adminLbs = hibernateDao.getByQuery(AdminLb.class, Condition.eq("work.id", pojo.getId()));
		if(!adminLbs.isEmpty()){
			hibernateDao.delete(adminLbs.get(0));
			cacheHandler.evictIndexWorkCache();
		}
		
		super.deletePojo(pojo, user);
	}

}
