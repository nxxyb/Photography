package com.photography.utils;

/**
 * 
 * @author 徐雁斌
 * @since 2015-2-5
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
public class Constants {
	
	/**
	 * 是否
	 */
	public final static String NO = "0";
	public final static String YES = "1";
	
	public final static String SEPARATOR = ",";
	
	/**
	 * 性别
	 */
	public final static String SEX_MAN = "0";
	public final static String SEX_WOMAN = "1";
	
	/**
	 * 审核状态
	 */
	//未提交审核
	public final static String VERIFY_NO = "0";
	//提交审核
	public final static String VERIFY_ING = "1";
	//审核不通过
	public final static String VERIFY_NOTPASS = "2";
	//审核通过
	public final static String VERIFY_PASS = "3";
	
	/**
	 * 用户类型
	 */
	//普通用户
	public final static String USER_TYPE_NORMAL = "2";
	
	//个人活动发布者
	public final static String USER_TYPE_PERSON_PUBLISH = "3";
	
	//单位活动发布者
	public final static String USER_TYPE_COMPANY_PUBLISH = "4";
	
	//管理员
	public final static String USER_TYPE_ADMIN = "1";
	
	//session
	public final static String SESSION_USER_KEY = "sessionuser";
	
	//template 
	public final static String ERROR_MESSAGE = "errorMessage";
	public final static String SUCCESS_MESSAGE = "successMessage";
	

	/**
	 * 活动类型
	 */
	//市内采风
	public final static String PROJECT_TYPE_CITYINSIDE = "1";
	public final static String PROJECT_TYPE_CITYINSIDE_NAME = "市内采风";
	
	//远郊采风
	public final static String PROJECT_TYPE_CITYOUTSIDE = "2";
	public final static String PROJECT_TYPE_CITYOUTSIDE_NAME = "远郊采风";
	
	//说拍就拍
	public final static String PROJECT_TYPE_SAIDPAT = "3";
	public final static String PROJECT_TYPE_SAIDPAT_NAME = "说拍就拍";
	
	//一对一
	public final static String PROJECT_TYPE_ONETOONE = "4";
	public final static String PROJECT_TYPE_ONETOONE_NAME = "一对一";
	
	/**
	 * 活动状态
	 */
	//未开始
	public final static String PROJECT_STATUS_WKS = "1";
	
	//已开始
	public final static String PROJECT_STATUS_YKS = "2";
	
	//已结束
	public final static String PROJECT_STATUS_YJS = "3";
	
	/**
	 * 订单状态
	 */
	//未支付
	public final static String USER_ORDER_STATUS_WZF = "1";
	
	//已取消
	public final static String USER_ORDER_STATUS_YQX = "2";
	
	//已支付
	public final static String USER_ORDER_STATUS_YZF = "3";
	
	
	/**
	 * 分页
	 */
	public final static int PAGER_DEFALUT_SIZE = 5;
	
	//用户中心-发布活动每页显示条数
	public final static int PAGER_PROJECT_FB = 4;
	
	//用户中心-活动订单每页显示条数
	public final static int PAGER_PROJECT_ORDER = 4;
	
}
