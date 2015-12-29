package com.photography.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.photography.dao.exp.Condition;
import com.photography.dao.exp.Expression;
import com.photography.dao.query.Pager;
import com.photography.dao.query.Sort;
import com.photography.exception.ServiceException;
import com.photography.mapping.BaseMapping;
import com.photography.mapping.BlogFriend;
import com.photography.mapping.User;

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
	public List<User> getRecommendFriends(User user,User sessionUser,Pager pager,String searchText) {
		
		if(user != null){
			List<String> params = new ArrayList<String>();
			params.add(user.getId());
			params.add(user.getId());
			params.add(sessionUser.getId());
			String hql = "from user u where u.id not in(select b.friendUser.id from blog_friend b where b.createUser.id = ?) and u.id not in(select b.createUser.id from blog_friend b where b.friendUser.id = ?) and u.id != ? ";
			if(!StringUtils.isEmpty(searchText)){
				hql = hql + " and (u.mobile like ? or u.realName like ?)";
				params.add("%" + searchText + "%");
				params.add("%" + searchText + "%");
			}
			
			List<User> users = (List<User>) hibernateDao.find(hql,params, pager.getCurrentPage(), pager.getPageSize());
			List<Long> counts = (List<Long>) hibernateDao.find("select count(*) " + hql,params);
			pager.setTotalCount(counts.get(0).intValue() + pager.getOffset());
			return users;
		}else{
			return getPojoList(User.class, pager, null, new Sort("createTime","DESC"), null);
		}
	}
	
	@Override
	public void deletePojo(BaseMapping pojo, User user) throws ServiceException {
		//点赞记录
		hibernateDao.executeUpdate("delete love where blog.id='" + pojo.getId() + "'");
		
		//删除评论
		hibernateDao.executeUpdate("delete blog_comment where blog.id='" + pojo.getId() + "'");
		
		super.deletePojo(pojo, user);
	}

}
