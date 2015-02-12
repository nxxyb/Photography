package com.photography.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.photography.dao.IHibernateDao;
import com.photography.dao.IUserDao;
import com.photography.dao.query.QueryConstants;
import com.photography.dao.query.QueryCriterions;
import com.photography.exception.ErrorCode;
import com.photography.exception.ServiceException;
import com.photography.mapping.BaseMapping;
import com.photography.mapping.User;
import com.photography.utils.Constants;

/**
 * 
 * @author 徐雁斌
 * @since 2015-2-4
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements IUserService {
	
	@Autowired
	private IUserDao userDao;

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	/* 
	 * @see com.photography.service.IBaseService#getPojoClass()
	 */
	public Class<? extends BaseMapping> getPojoClass() {
		return User.class;
	}

	/* 
	 * @see com.photography.service.BaseServiceImpl#getDao()
	 */
	@Override
	public IHibernateDao getDao() {
		return userDao;
	}

	/* 
	 * @see com.photography.service.IUserService#login(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	public User login(String userName, String password) throws ServiceException {
		QueryCriterions hc = new QueryCriterions();
		hc.and("loginName", userName, QueryConstants.EQ);
		User user = null;
		List<User> userList = (List<User>) userDao.getByQuery(User.class, hc.toExpression());
		if(userList.size()>0) {
			user = userList.get(0);
		}
		if (user == null) {
			throw new ServiceException(ErrorCode.USER_NOT_EXIST);
		}

		if (!password.equals(user.getPassword())) {
			throw new ServiceException(ErrorCode.USER_PWD_NOT_MATCH);
		}
		
		if(user.getEnable() != null && Constants.NO.equals(user.getEnable())){
			throw new ServiceException(ErrorCode.USER_NOT_ENABLE);
		}
		
		if(user.getVerify() != null && Constants.NO.equals(user.getVerify())){
			throw new ServiceException(ErrorCode.USER_NOT_VERIFY);
		}
		
		user.setLastUpdateTime(new Date());
		userDao.saveOrUpdate(user);
		
		return user;
	}
	
	

}
