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
 * 摄影作品
 * @author Administrator
 *
 */
@Entity(name="work")
public class Work extends BaseMapping {

	private static final long serialVersionUID = -5432595383513302235L;
	
	/**
	 * 名称
	 */
	@Column(name="name")
	private String name;

	/**
	 * 介绍
	 */
	@Column(name="des")
	private String des;
	
	/**
	 * 价格
	 */
	@Column(name="cost")
	private String cost;
	
	/**
	 * 创建用户
	 */
	@ManyToOne
	@JoinColumn(name="create_user")
	@LazyToOne(LazyToOneOption.PROXY)
	private User createUser;
	
	//已订购人数
	@Column(name="joined_number")
	private String joinedNumber;
	
	//浏览次数
	@Column(name="viewed_number")
	private String viewedNumber;
	
	/**
	 * 照片
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="photos")
	private FileGroup photos;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public FileGroup getPhotos() {
		return photos;
	}

	public void setPhotos(FileGroup photos) {
		this.photos = photos;
	}

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public String getJoinedNumber() {
		return joinedNumber;
	}

	public void setJoinedNumber(String joinedNumber) {
		this.joinedNumber = joinedNumber;
	}

	public String getViewedNumber() {
		return viewedNumber;
	}

	public void setViewedNumber(String viewedNumber) {
		this.viewedNumber = viewedNumber;
	}
	
	
}