package com.photography.mapping;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

/**
 * 派文
 * @author Administrator
 *
 */
@Entity(name="blog")
public class Blog extends BaseMapping {

	private static final long serialVersionUID = -4645510010960351563L;

	/**
	 * 内容
	 */
	@Column(name="content")
	private String content;
	
	/**
	 * 创建用户
	 */
	@ManyToOne
	@JoinColumn(name="create_user")
	@LazyToOne(LazyToOneOption.PROXY)
	private User createUser;
	
	/**
	 * 图片
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="photos")
	private FileGroup photos;
	
	/**
	 * 视频地址
	 */
	@Column(name="video")
	private String video;
	
	/**
	 * 点赞个数
	 */
	@Formula("(select count(*) from love l where l.blog = id)")
	private String loveNum;
	
	/**
	 * 评论个数
	 */
	@Formula("(select count(*) from blog_comment bc where bc.blog = id)")
	private String commentNum;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public FileGroup getPhotos() {
		return photos;
	}

	public void setPhotos(FileGroup photos) {
		this.photos = photos;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getLoveNum() {
		return loveNum;
	}

	public void setLoveNum(String loveNum) {
		this.loveNum = loveNum;
	}

	public String getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}
	
	
}
