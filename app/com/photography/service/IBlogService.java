package com.photography.service;

import java.util.List;

import com.photography.dao.exp.Expression;
import com.photography.dao.query.Pager;
import com.photography.dao.query.Sort;
import com.photography.mapping.BlogFriend;
import com.photography.mapping.User;

public interface IBlogService extends IBaseService {
	
	/**
	 * 获取指定用户全部派友
	 * @param user
	 * @return
	 */
	public List<BlogFriend> getFriends(User user);
	
	/**
	 * 判断指定用户是否为派友
	 * @param user
	 * @return
	 */
	public Boolean isFriends(User user,User friendUser);
	
	/**
	 * 得到指定用户派友
	 * @param user
	 * @return
	 */
	public BlogFriend getFriend(User user,User friendUser);
	
	/**
	 * 获取用户派友
	 * @param user
	 * @return
	 */
	public List<BlogFriend> getFriends(User user,Expression exp,Pager pager,Sort sort);
	
	/**
	 * 获取指定用户推荐派友用户
	 * @param user
	 * @return
	 */
	public List<User> getRecommendFriends(User user,User sessionUser,Pager pager,String searchText);

}
