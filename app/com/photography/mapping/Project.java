package com.photography.mapping;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import com.photography.utils.Constants;

/**
 * 活动
 * @author 徐雁斌
 * @since 2015-2-5
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Entity(name="project")
public class Project extends BaseMapping{
	
	private static final long serialVersionUID = -5155878212794168022L;

	/**
	 * 活动名称
	 */
	@Column(name="name")
	private String name;
	
	/**
	 * 活动类型
	 * 1-市内采风 2-远郊采风 3-说拍就拍 4-一对一
	 */
	@Column(name="type")
	private String type;
	
	/**
	 * 活动开始时间
	 */
	@Column(name="start_time")
	private Date startTime;
	
	/**
	 * 活动时长
	 */
	@Column(name="time_length")
	private String timeLength;
	
	/**
	 * 活动人数
	 */
	@Column(name="people_num")
	private String peopleNum;
	
	/**
	 * 模特数量
	 */
	@Column(name="model_num")
	private String modelNum;
	
	/**
	 * 活动地点
	 */
	@Column(name="place")
	private String place;
	
	/**
	 * 集合地点
	 */
	@Column(name="venue_place")
	private String venuePlace;
	
	/**
	 * 省
	 */
	@Column(name="province")
	private String province;
	
	/**
	 * 市
	 */
	@Column(name="city")
	private String city;
	
	/**
	 * 区
	 */
	@Column(name="county")
	private String county;
	
	/**
	 * 联系方式
	 */
	@Column(name="contact")
	private String contact;
	
	/**
	 * 活动介绍
	 */
	@Column(name="des")
	private String des;
	
	/**
	 * 行程介绍
	 */
	@Column(name="xc_des")
	private String xcDes;
	
	/**
	 * 费用介绍
	 */
	@Column(name="fee_des")
	private String feeDes;
	
	/**
	 * 活动费用
	 */
	@Column(name="cost")
	private String cost;
	
	/**
	 * 最低开办人数
	 */
	@Column(name="less_num")
	private String lessNum;
	
	/**
	 * 活动照片
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="photos")
	private FileGroup photos;
	
	/**
	 * 场地、模特 照片
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="model_photos")
	private FileGroup modelPhotos;
	
	/**
	 * 创建用户
	 */
	@ManyToOne
	@JoinColumn(name="create_user")
	@LazyToOne(LazyToOneOption.PROXY)
	private User createUser;
	
	//已参加人数
	@Column(name="joined_number")
	private String joinedNumber;
	
	//浏览次数
	@Column(name="viewed_number")
	private String viewedNumber;
	
	//项目状态 1-未开始 2-已开始 3-已结束
	@Column(name="status")
	private String status = Constants.PROJECT_STATUS_WKS;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getTimeLength() {
		return timeLength;
	}

	public void setTimeLength(String timeLength) {
		this.timeLength = timeLength;
	}

	public String getPeopleNum() {
		return peopleNum;
	}

	public void setPeopleNum(String peopleNum) {
		this.peopleNum = peopleNum;
	}

	public String getModelNum() {
		return modelNum;
	}

	public void setModelNum(String modelNum) {
		this.modelNum = modelNum;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getVenuePlace() {
		return venuePlace;
	}

	public void setVenuePlace(String venuePlace) {
		this.venuePlace = venuePlace;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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

	public String getLessNum() {
		return lessNum;
	}

	public void setLessNum(String lessNum) {
		this.lessNum = lessNum;
	}

	public FileGroup getPhotos() {
		return photos;
	}

	public void setPhotos(FileGroup photos) {
		this.photos = photos;
	}

	public FileGroup getModelPhotos() {
		return modelPhotos;
	}

	public void setModelPhotos(FileGroup modelPhotos) {
		this.modelPhotos = modelPhotos;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getXcDes() {
		return xcDes;
	}

	public void setXcDes(String xcDes) {
		this.xcDes = xcDes;
	}

	public String getFeeDes() {
		return feeDes;
	}

	public void setFeeDes(String feeDes) {
		this.feeDes = feeDes;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
