package com.photography.mapping;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

@Entity(name="blog_comment")
public class BlogComment extends BaseMapping {

	private static final long serialVersionUID = -7092379237929225313L;
	
	/**
	 * 用户
	 */
	@ManyToOne
	@JoinColumn(name="create_user")
	@LazyToOne(LazyToOneOption.PROXY)
	private User createUser;
	
	//派文
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="blog")
	private Blog blog;
	
	/**
	 * 评论内容
	 */
	@Column(name="content")
	private String content;
	
	/**
	 * 评论回复
	 */
	@OneToMany(mappedBy="blogComment",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy(value= "create_time ASC")
	private List<CommentReploy> commentReploys;

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<CommentReploy> getCommentReploys() {
		return commentReploys;
	}

	public void setCommentReploys(List<CommentReploy> commentReploys) {
		this.commentReploys = commentReploys;
	}

	
}
