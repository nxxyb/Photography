package com.photography.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.photography.dao.exp.Condition;
import com.photography.dao.exp.Expression;
import com.photography.dao.query.Pager;
import com.photography.dao.query.Sort;
import com.photography.mapping.BlogFriend;
import com.photography.mapping.User;
import com.photography.utils.Constants;

@Service("blogService")
public class BlogServiceImpl extends BaseServiceImpl implements IBlogService {

	@Override
	public List<BlogFriend> getFriends(User user) {
		Expression expression = null;
		if(user != null){
			expression = Condition.eq("createUser.id", user.getId()).or(Condition.eq("friendUser.id", user.getId()));
		}
		return loadPojoByExpression(BlogFriend.class, expression, new Sort("createTime","ASC"));
	}

	@Override
	public Boolean isFriends(User user, User friendUser) {
		Boolean flag = true;
		Expression exp = Condition.and(
				Condition.eq("createUser.id", user.getId()),
				Condition.eq("friendUser.id", friendUser.getId())).or(
				Condition.and(Condition.eq("friendUser.id", user.getId()),
						Condition.eq("createUser.id",friendUser.getId())));
		int friendInt = getCountByQuery(BlogFriend.class, exp);
		if(friendInt == 0){
			flag = false;
		}
		return flag;
	}
	
	@Override
	public BlogFriend getFriend(User user, User friendUser) {
		Expression exp = Condition.and(
				Condition.eq("createUser.id", user.getId()),
				Condition.eq("friendUser.id", friendUser.getId())).or(
				Condition.and(Condition.eq("friendUser.id", user.getId()),
						Condition.eq("createUser.id",friendUser.getId())));
		List<BlogFriend> blogFriends = loadPojoByExpression(BlogFriend.class, exp, null);
		if(!blogFriends.isEmpty()){
			return blogFriends.get(0);
		}else{
			return null;
		}
	}

	@Override
	public List<BlogFriend> getFriends(User user, Expression exp, Pager pager,Sort sort) {
		Expression expression = Condition.eq("createUser.id", user.getId()).or(Condition.eq("friendUser.id", user.getId()));
		if(exp != null){
			expression = expression.and(exp);
		}
		return getPojoList(BlogFriend.class, pager, expression, sort, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getRecommendFriends(User user,Pager pager,String searchText) {
		
		if(user != null){
			List<String> params = new ArrayList<String>();
			params.add(user.getId());
			params.add(user.getId());
			return (List<User>) hibernateDao.find("from user u where u.id not in(select b.friendUser.id from blog_friend b where b.createUser.id = ?) and u.id not in(select b.createUser.id from blog_friend b where b.friendUser.id = ?)",
							params, pager.getCurrentPage(), pager.getPageSize());
		}else{
			return getPojoList(User.class, pager, null, new Sort("createTime","DESC"), null);
		}
	}

}
