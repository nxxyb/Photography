package com.photography.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.photography.exception.ServiceException;
import com.photography.mapping.User;
import com.photography.service.IUserService;

/**
 * 
 * @author 徐雁斌
 * @since 2015-2-6
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
public class UserTest extends BaseTest {
	
	@Autowired
	private IUserService userService;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	@Test
	public void insert() throws ServiceException{
		User user = new User();
		user.setLoginName("loginName");
		user.setPassword("password");
		
		userService.savePojo(user, user);
	}
	
	@Test
	public void get(){
		User user = (User) userService.loadPojo(User.class,"ce5a35d5-b004-11e4-93b0-0002040816af");
		System.out.println(user.getLoginName());
	}
	

}
