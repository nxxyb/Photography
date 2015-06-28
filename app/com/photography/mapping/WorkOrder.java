package com.photography.mapping;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class WorkOrder extends BaseMapping {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4745360438275788334L;

	//用户
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user")
	private User user;
	
	//活动
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="work")
	private Work work;
	
	//状态 1-未支付 2-已取消  3-已支付
	@Column(name="status")
	private String status;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
