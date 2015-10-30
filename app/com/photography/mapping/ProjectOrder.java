package com.photography.mapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.photography.utils.Constants;

/**
 * 订单表
 * @author 徐雁斌
 * @since 2015-5-19
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Entity(name="project_order")
public class ProjectOrder extends BaseMapping {

	private static final long serialVersionUID = 3354872775147024093L;

	//用户
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user")
	private User user;
	
	//活动
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="project")
	private Project project;
	
	//状态 1-未支付 2-已取消  3-已支付
	@Column(name="status")
	private String status;
	
	/**
	 * 数量
	 */
	@Column(name="number")
	private String number;
	
	/**
	 * 单价
	 */
	@Column(name="unit_price")
	private String unitPrice;
	
	/**
	 * 使用胶卷数
	 */
	@Column(name="coupon")
	private String coupon;
	
	/**
	 * 原总价
	 */
	@Column(name="original_price")
	private String originalPrice;
	
	/**
	 * 实际支付总价
	 */
	@Column(name="actual_price")
	private String actualPrice;
	
	/**
	 * 是否评价
	 */
	@Column(name="is_comment")
	private String isComment = Constants.NO;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(String actualPrice) {
		this.actualPrice = actualPrice;
	}

	public String getIsComment() {
		return isComment;
	}

	public void setIsComment(String isComment) {
		this.isComment = isComment;
	}
	
	
}
