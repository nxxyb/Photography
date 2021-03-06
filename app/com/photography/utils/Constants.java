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
	//未审核
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
	//用户
	public final static String SESSION_USER_KEY = "sessionuser";
	
	//链接跳转url
	public final static String SESSION_LOGIN_REDIRECTURL = "sessionLoginRedirectURL";
	
	//链接跳转url参数
	public final static String SESSION_LOGIN_PARAMETERMAP = "sessionLoginParametermap";
	
	//查询参数
	public final static String SESSION_SEARCHSTRING = "sessionSearchstring";
	
	//template 
	public final static String ERROR_MESSAGE = "errorMessage";
	public final static String SUCCESS_MESSAGE = "successMessage";
	

	/**
	 * 活动类型
	 */
	//棚拍
	public final static String PROJECT_TYPE_PP = "0";
	public final static String PROJECT_TYPE_PP_NAME = "棚拍";
	
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
	 * 审核状态
	 */
	//未审核
	public final static String VERIFY_STATUS_WSH = "0";
	
	//已审核
	public final static String VERIFY_STATUS_YSH = "1";
	
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
	
	//支付失败
	public final static String USER_ORDER_STATUS_ZFSB= "4";
	
	
	/**
	 * 分页
	 */
	public final static int PAGER_DEFALUT_SIZE = 5;
	
	//用户中心-发布活动每页显示条数
	public final static int PAGER_PROJECT_FB = 4;
	
	//用户中心-活动订单每页显示条数
	public final static int PAGER_PROJECT_ORDER = 4;
	
	public final static int PAGER_SIZE_6 = 6;
	public final static int PAGER_SIZE_7 = 7;
	public final static int PAGER_SIZE_8 = 8;
	public final static int PAGER_SIZE_9 = 9;
	public final static int PAGER_SIZE_10 = 10;
	
	/**
	 * 胶卷类别
	 */
	//收入
	public final static String COUPON_CLASS_INCOME = "1";
	
	//支出
	public final static String COUPON_CLASS_SPEND = "2";
	
	/**
	 * 胶卷类型
	 */
	
	//用户注册
	public final static String COUPON_TYPE_GIVE_REGISTRATION = "1";
	
	//登录签到
	public final static String COUPON_TYPE_GIVE_SIGN = "2";
	
	//评论
	public final static String COUPON_TYPE_GIVE_COMMENT = "3";
	
	//分享
	public final static String COUPON_TYPE_GIVE_SHARE = "4";
	
	//预定活动
	public final static String COUPON_TYPE_CONSUME_ORDERPROJECT = "5";
	
	//参加活动--支付胶卷
	public final static String COUPON_TYPE_GIVE_JOINPROJECT = "6";
}
