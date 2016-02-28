package com.photography.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.photography.dao.IHibernateDao;
import com.photography.dao.exp.Expression;
import com.photography.dao.query.Pager;
import com.photography.dao.query.Sort;
import com.photography.exception.ServiceException;
import com.photography.mapping.BaseMapping;
import com.photography.mapping.User;

/**
 * 
 * @author 徐雁斌
 * @since 2015-2-6
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Service("baseService")
public class BaseServiceImpl implements IBaseService {
	
	@Autowired
	protected IHibernateDao hibernateDao;
	
	public void setHibernateDao(IHibernateDao hibernateDao) {
		this.hibernateDao = hibernateDao;
	}

	/* 
	 * @see com.photography.service.IBaseService#loadPojo(java.lang.String)
	 */
	public <T> T loadPojo(Class<T> clazz, String id) {
		return hibernateDao.getById(clazz, id);
	}

	/* 
	 * @see com.photography.service.IBaseService#loadPojoByExpression(com.photography.dao.exp.Expression, com.photography.dao.query.Sort)
	 */
	public <T> List<T> loadPojoByExpression(Class<T> clazz, Expression expression, Sort sort) {
		return hibernateDao.getByQuery(clazz, expression,sort);
	}
	
	@Override
	public <T> T loadOnePojoByExpression(Class<T> clazz, Expression expression,Sort sort) {
		List<T> t = hibernateDao.getByQuery(clazz, expression,sort);
		if(t!= null && !t.isEmpty()){
			return t.get(0);
		}else{
			return null;
		}
	}

	/* 
	 * @see com.photography.service.IBaseService#savePojo(com.photography.mapping.BaseMapping, com.photography.mapping.User)
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void savePojo(BaseMapping pojo, User user) throws ServiceException {
		if("".equals(pojo.getId())){
			pojo.setId(null);
		}
//		hibernateDao.saveOrUpdate(pojo);
		hibernateDao.merge(pojo);

	}

	/* 
	 * @see com.photography.service.IBaseService#deletePojo(com.photography.mapping.BaseMapping, com.photography.mapping.User)
	 */
	public void deletePojo(BaseMapping pojo, User user) throws ServiceException {
		hibernateDao.delete(pojo);
	}

	/* 
	 * @see com.photography.service.IBaseService#getPojoList(com.photography.dao.query.Pager, com.photography.dao.exp.Expression, com.photography.dao.query.Sort, com.photography.mapping.User)
	 */
	public <T> List<T> getPojoList(Class<T> clazz, Pager pager, Expression expression, Sort sort, User user) {
		return hibernateDao.getByQuery(clazz, pager, expression, sort);
	}
	
	public <T> int getCountByQuery(Class<T> entityType, Expression expression) {
		return hibernateDao.getCountByQuery(entityType,expression);
	}
	
	/* 
	 * @see com.photography.service.IBaseService#addUpdateLog(com.photography.mapping.BaseMapping, com.photography.mapping.User)
	 */
	public void addUpdateLog(BaseMapping BaseMapping, User user) {
		// TODO Auto-generated method stub

	}

}
