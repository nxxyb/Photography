package com.photography.controller.view;

import java.util.List;

import com.photography.mapping.UserCouponSetting;
import com.photography.mapping.UserCouponSettingRmb;

public class UserCouponSettingList {
	
	private UserCouponSettingRmb userCouponSettingRmb;
	
	private List<UserCouponSetting> userCouponSettings;

	public List<UserCouponSetting> getUserCouponSettings() {
		return userCouponSettings;
	}

	public void setUserCouponSettings(List<UserCouponSetting> userCouponSettings) {
		this.userCouponSettings = userCouponSettings;
	}

	public UserCouponSettingRmb getUserCouponSettingRmb() {
		return userCouponSettingRmb;
	}

	public void setUserCouponSettingRmb(UserCouponSettingRmb userCouponSettingRmb) {
		this.userCouponSettingRmb = userCouponSettingRmb;
	}
	

}
