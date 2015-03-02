package com.photography.service;

/**
 * 邮件发送
 * @author 徐雁斌
 * @since 2015-3-1
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
public interface IMailService {
	
	/**
	 * 发送邮件
	 * @param sendTo  地址
	 * @param subject 标题 
	 * @param content 内容
	 * @author 徐雁斌
	 */
	public void sendMail(String sendTo,String subject,String content);
	
	/**
	 * 发送注册确认邮件
	 * @param sendTo  地址
	 * @param key     链接主键
	 * @author 徐雁斌
	 */
	public void sendConfirmMail(String sendTo,String key);

}
