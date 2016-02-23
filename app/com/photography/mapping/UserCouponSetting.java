package com.photography.mapping;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 胶卷设置
 * @author Administrator
 *
 */
@Entity(name="user_coupon_setting")
public class UserCouponSetting extends BaseMapping {
	
	private static final long serialVersionUID = -1052407841592367800L;

	/**
	 * 胶卷类型
	 * Constants.COUPON_TYPE_*
	 */
	@Column(name="type")
	private String type;
	
	/**
	 * 奖励个数
	 */
	@Column(name="num")
	private String num;
	
	/**
	 * 描述
	 */
	@Column(name="message")
	private String message;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
