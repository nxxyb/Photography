package com.photography.controller.view;

import com.photography.utils.Constants;

public class ProjectOrderCoupon {
	
	/**
	 * 可以使用的胶卷数量
	 */
	private String enableCouponNum;
	
	/**
	 * 胶卷抵扣的钱数
	 */
	private String useCouponRmb;
	
	/**
	 * 是否能使用胶卷
	 */
	private String isUseCoupon = Constants.NO;

	public String getEnableCouponNum() {
		return enableCouponNum;
	}

	public void setEnableCouponNum(String enableCouponNum) {
		this.enableCouponNum = enableCouponNum;
	}

	public String getUseCouponRmb() {
		return useCouponRmb;
	}

	public void setUseCouponRmb(String useCouponRmb) {
		this.useCouponRmb = useCouponRmb;
	}

	public String getIsUseCoupon() {
		return isUseCoupon;
	}

	public void setIsUseCoupon(String isUseCoupon) {
		this.isUseCoupon = isUseCoupon;
	}

	
	
}
