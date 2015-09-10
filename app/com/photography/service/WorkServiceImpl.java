package com.photography.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.photography.dao.query.Pager;
import com.photography.dao.query.Sort;
import com.photography.exception.ErrorCode;
import com.photography.exception.ServiceException;
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
	public List<Work> getIndexProject() throws ServiceException {
		Pager pager= new Pager();
		pager.setPageSize(8);
		List<Work> works = hibernateDao.getByQuery(Work.class, pager,null, new Sort());
		return works;
	}

}
