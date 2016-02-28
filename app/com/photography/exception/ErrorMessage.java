package com.photography.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常错误消息
 *
 * @since 2011-11-9
 * @author Zhao Zhihong
 *
 * @Copyright (C) 2012 天大求实电力新技术股份有限公司 版权所有
 */
public class ErrorMessage {
	private static Map<Integer, String> msg = new HashMap<Integer, String>();

	static {
		/*
		 * 公共异常错误码
		 */
		msg.put(ErrorCode.UNKNOWN_ERROR, "数据库异常！");
		msg.put(ErrorCode.DATABASE_ERROR, "数据库异常！");
		msg.put(ErrorCode.SESSION_TIMEOUT, "用户未登录或会话已超时，请重新登录！");

		
		/*
		 * 用户
		 */
		msg.put(ErrorCode.USER_NOT_EXIST, "用户名错误！");
		msg.put(ErrorCode.USER_PWD_NOT_MATCH, "密码错误！");
		msg.put(ErrorCode.USER_NOT_VERIFY, "用户未审核通过！");
		msg.put(ErrorCode.USER_NOT_ENABLE, "用户未激活！");
		msg.put(ErrorCode.USER_OLD_PWD_NOT_MATCH, "原始密码错误！");
		
		/*
		 * 活动
		 */
		msg.put(ErrorCode.PROJECT_NOT_EXIST, "该活动不存在或已经被删除！");
		msg.put(ErrorCode.PROJECT_PHOTO_NO_UPLOAD, "请上传轮播图片！");
		
		/*
		 * 作品
		 */
		msg.put(ErrorCode.WORK_NOT_EXIST, "该作品不存在或已经被删除！");
		msg.put(ErrorCode.WORK_PHOTO_NO_UPLOAD, "请上传作品图片！");
		
		/*
		 * 胶卷
		 */
		msg.put(ErrorCode.COUPON_ADD_EXCEPTION, "添加胶卷记录失败！");
		
		/*
		 * 支付
		 */
		msg.put(ErrorCode.ALIPAY_SAVE_ORDER_EXCEPTION, "保存订单失败！");
	}

	public static String get(int errorCode){
		return msg.get(errorCode);
	}
	
	public static void put(Map<Integer, String> message){
		msg.putAll(message);
	}
	
	public static String getErrorMessage(Exception e){
		String message = null;
		if(e instanceof ServiceException){
			ServiceException se = (ServiceException) e;
			message = se.getErrorMessage();
		}else{
			message = ErrorMessage.get(ErrorCode.UNKNOWN_ERROR);
		}
		return message;
	}
}
