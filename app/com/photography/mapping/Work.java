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
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.photography.utils.Constants;

/**
 * 摄影作品
 * @author Administrator
 *
 */
@Entity(name="work")
@Indexed(index="work")
public class Work extends BaseMapping {

	private static final long serialVersionUID = -5432595383513302235L;
	
	/**
	 * 名称
	 */
	@Column(name="name")
	@Field(name="name",index=Index.YES,store=Store.YES)
	private String name;

	/**
	 * 介绍
	 */
	@Column(name="des")
	@Field(name="des",index=Index.YES,store=Store.YES)
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
	
	//评论人数
	@Formula("(select count(*) from work_comment wc where wc.work = id)")
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
	
	//审核状态 
	@Column(name="verify")
	private String verify = Constants.VERIFY_NO;
	
	 //审核结果信息
	@Column(name="verify_message")
	private String verifyMessage;

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

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public String getVerifyMessage() {
		return verifyMessage;
	}

	public void setVerifyMessage(String verifyMessage) {
		this.verifyMessage = verifyMessage;
	}
	
	
}
