package com.photography.mapping;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 作品收藏
 * @author 徐雁斌
 * @since 2015-11-20
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Entity(name="work_collect")
public class WorkCollect extends BaseMapping {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3074515998155719826L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="work")
	private Work work;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user")
	private User user;

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
