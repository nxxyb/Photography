package com.photography.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.photography.dao.exp.Condition;
import com.photography.exception.ErrorCode;
import com.photography.exception.ServiceException;
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

	/* 
	 * @see com.photography.service.IUserService#login(java.lang.String, java.lang.String)
	 */
	public User login(String mobile, String password) throws ServiceException {
		User user = null;
		List<User> userList = (List<User>) hibernateDao.getByQuery(User.class, Condition.eq("mobile", mobile));
		if(userList.size()>0) {
			user = userList.get(0);
		}
		if (user == null) {
			throw new ServiceException(ErrorCode.USER_NOT_EXIST);
		}

		if (!password.equalsIgnoreCase(user.getPassword())) {
			throw new ServiceException(ErrorCode.USER_PWD_NOT_MATCH);
		}
		
		if(user.getEnable() != null && Constants.NO.equals(user.getEnable())){
			throw new ServiceException(ErrorCode.USER_NOT_ENABLE);
		}
		
//		if(user.getVerify() != null && Constants.NO.equals(user.getVerify())){
//			throw new ServiceException(ErrorCode.USER_NOT_VERIFY);
//		}
		
		user.setLastUpdateTime(new Date());
		hibernateDao.saveOrUpdate(user);
		
		return user;
	}

	/* 
	 * @see com.photography.service.IUserService#getByEmail(java.lang.String)
	 */
	@Override
	public User getByMobile(String mobile) {
		List<User> users = hibernateDao.getByQuery(User.class, Condition.eq("mobile", mobile));
		if(users != null && !users.isEmpty()){
			return users.get(0);
		}
		return null;
	}
	
	

}
