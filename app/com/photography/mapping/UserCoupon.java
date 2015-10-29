package com.photography.mapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

/**
 * 用户胶卷记录
 * @author 徐雁斌
 * @since 2015-10-29
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Entity(name="user_coupon")
public class UserCoupon extends BaseMapping {
	
	private static final long serialVersionUID = 1658389503056913383L;

	/**
	 * 胶卷数量
	 */
	@Column(name="coupon_num")
	private String couponNum;
	
	/**
	 * 收入/支出
	 */
	@Column(name="type")
	private String type;
	
	/**
	 * 详细信息
	 */
	@Column(name="message")
	private String message;
	
	/**
	 * 用户
	 */
	@ManyToOne
	@JoinColumn(name="user")
	@LazyToOne(LazyToOneOption.PROXY)
	private User user;

	public String getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(String couponNum) {
		this.couponNum = couponNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
