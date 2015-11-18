package com.photography.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.photography.dao.exp.Condition;
import com.photography.dao.query.Pager;
import com.photography.dao.query.Sort;
import com.photography.exception.ServiceException;
import com.photography.mapping.AdminLb;
import com.photography.mapping.Work;

@Service("workService")
public class WorkServiceImpl extends BaseServiceImpl implements IWorkService {

	@Override
	public List<Work> getRelaWorks(String id) throws ServiceException {
		Work work = hibernateDao.loadById(Work.class, id);
		if(work == null){
//			throw new ServiceException(ErrorCode.WORK_NOT_EXIST);
		}
		
		Pager pager= new Pager();
		pager.setPageSize(5);
		List<Work> works = hibernateDao.getByQuery(Work.class, pager,null, new Sort());
		return works;
	}

	@Override
	public List<Work> getIndexWorks() throws ServiceException {
		List<AdminLb> adminLbs = hibernateDao.getByQuery(AdminLb.class, Condition.eq("type", "4"), new Sort("sort","asc"));
		List<Work> works = new ArrayList<Work>();
		for(AdminLb adminLb : adminLbs){
			works.add(adminLb.getWork());
		}
		return works;
	}

}
