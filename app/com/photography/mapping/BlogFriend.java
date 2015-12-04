package com.photography.mapping;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

/**
 * 派友
 * @author Administrator
 * 
 */
@Entity(name="blog_friend")
public class BlogFriend extends BaseMapping{

	private static final long serialVersionUID = -1173988539634668959L;

	/**
	 * 创建用户
	 */
	@ManyToOne
	@JoinColumn(name="create_user")
	@LazyToOne(LazyToOneOption.PROXY)
	private User createUser;
	
	/**
	 * 好友用户
	 */
	@ManyToOne
	@JoinColumn(name="friend_user")
	@LazyToOne(LazyToOneOption.PROXY)
	private User friendUser;

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public User getFriendUser() {
		return friendUser;
	}

	public void setFriendUser(User friendUser) {
		this.friendUser = friendUser;
	}
	
	
}
