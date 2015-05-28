package com.photography.mapping;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

/**
 * 活动评论
 * @author xyb
 *
 */
@Entity(name="project_comment")
public class ProjectComment extends BaseMapping {

	private static final long serialVersionUID = -6740812798723196737L;
	
	/**
	 * 用户
	 */
	@ManyToOne
	@JoinColumn(name="create_user")
	@LazyToOne(LazyToOneOption.PROXY)
	private User createUser;
	
	//活动
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="project")
	private Project project;
	
	/**
	 * 评论类型
	 * 1-好评  2-中评  3-差评
	 */
	@Column(name="type")
	private String type;
	
	/**
	 * 评论图片
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="photos")
	private FileGroup photos;
	
	/**
	 * 评论内容
	 */
	@Column(name="content")
	private String content;

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public FileGroup getPhotos() {
		return photos;
	}

	public void setPhotos(FileGroup photos) {
		this.photos = photos;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	
}
