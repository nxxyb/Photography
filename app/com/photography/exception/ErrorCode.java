package com.photography.exception;

/**
 * 异常错误码<BR/>
 * 
 * @Copyright (C) 2012 天大求实电力新技术股份有限公司 版权所有
 */
public class ErrorCode {
	/*
	 * 公共异常错误码
	 * 系统异常：10000-10099
	 */
	public static final int UNKNOWN_ERROR = 10000;
	public static final int DATABASE_ERROR = 10001;
	public static final int SESSION_TIMEOUT = 10002;
	public static final int PARSE_QUERY_EXP_ERROR = 10003;
	

	/*
	 * 用户
	 * 系统异常：10100-10199
	 */
	public static final int USER_NOT_EXIST = 10100;
	public static final int USER_PWD_NOT_MATCH = 10101;
	public static final int USER_NOT_VERIFY = 10102;
	public static final int USER_NOT_ENABLE = 10103;
	public static final int USER_OLD_PWD_NOT_MATCH = 10104;
	
	/*
	 * 活动
	 * 系统异常：10200-10299
	 */
	public static final int PROJECT_NOT_EXIST = 10200;
	
	public static final int PROJECT_PHOTO_NO_UPLOAD = 10201;
	
	/*
	 * 作品
	 * 系统异常：10300-10399
	 */
	public static final int WORK_NOT_EXIST = 10300;
	
	public static final int WORK_PHOTO_NO_UPLOAD = 10301;
	
	/*
	 * 缓存
	 * 系统异常：10400-10499
	 */
	public static final int CACHE_EXCEPTION = 10400;
	
	/*
	 * 胶卷
	 * 系统异常：10500-10599
	 */
	public static final int COUPON_ADD_EXCEPTION = 10500;
	
	/*
	 * 支付
	 * 系统异常：10600-10699 
	 */
	public static final int ALIPAY_SAVE_ORDER_EXCEPTION = 10600;
}
