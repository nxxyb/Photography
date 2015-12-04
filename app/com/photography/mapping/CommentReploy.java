package com.photography.mapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

/**
 * 评论回复表
 * @author Administrator
 *
 */
@Entity(name="comment_reploy")
public class CommentReploy extends BaseMapping {
	
	private static final long serialVersionUID = 7475312775490987981L;

	/**
	 * 用户
	 */
	@ManyToOne
	@JoinColumn(name="create_user")
	@LazyToOne(LazyToOneOption.PROXY)
	private User createUser;
	
	/**
	 * 回复内容
	 */
	@Column(name="content")
	private String content;
	
	/**
	 * 活动评论
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="project_comment")
	private ProjectComment projectComment;
	
	/**
	 * 作品评论
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="work_comment")
	private WorkComment workComment;
	
	/**
	 * 派文评论
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="blog_comment")
	private BlogComment blogComment;

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ProjectComment getProjectComment() {
		return projectComment;
	}

	public void setProjectComment(ProjectComment projectComment) {
		this.projectComment = projectComment;
	}

	public WorkComment getWorkComment() {
		return workComment;
	}

	public void setWorkComment(WorkComment workComment) {
		this.workComment = workComment;
	}

	public BlogComment getBlogComment() {
		return blogComment;
	}

	public void setBlogComment(BlogComment blogComment) {
		this.blogComment = blogComment;
	}
	
	
}
