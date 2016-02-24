package com.photography.mapping;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name="user_coupon_setting_rmb")
public class UserCouponSettingRmb extends BaseMapping {
	
	private static final long serialVersionUID = 172026451127444506L;
	/**
	 * 1胶卷对应人名币的数量
	 */
	@Column(name="one_coupon_to_rmb")
	private String oneCouponToRmb;
	public String getOneCouponToRmb() {
		return oneCouponToRmb;
	}
	public void setOneCouponToRmb(String oneCouponToRmb) {
		this.oneCouponToRmb = oneCouponToRmb;
	}

}
