package com.photography.mapping;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 活动收藏表
 * @author 徐雁斌
 * @since 2015-10-28
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Entity(name="project_collect")
public class ProjectCollect extends BaseMapping {
	
	private static final long serialVersionUID = -5750391794756946302L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="project")
	private Project project;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user")
	private User user;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
