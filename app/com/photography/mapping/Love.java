package com.photography.mapping;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

/**
 * 点赞
 * @author Administrator
 *
 */
@Entity(name="love")
public class Love extends BaseMapping {

	private static final long serialVersionUID = 574198202599109998L;

	/**
	 * 用户
	 */
	@ManyToOne
	@JoinColumn(name="create_user")
	@LazyToOne(LazyToOneOption.PROXY)
	private User createUser;
	
	/**
	 * 活动
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="project")
	private Project project;
	
	/**
	 * 作品
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="work")
	private Work work;
	
	/**
	 * 派文
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="blog")
	private Blog blog;

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}
	
	
}
