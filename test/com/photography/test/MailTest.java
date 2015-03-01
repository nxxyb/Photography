package com.photography.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.photography.service.IMailService;

/**
 * 
 * @author 徐雁斌
 * @since 2015-3-1
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
public class MailTest extends BaseTest {

	@Autowired
	private IMailService mailService;

	@Test
	public void test() {
		StringBuffer content = new StringBuffer();
		content.append("欢迎您加入摄影帮网站！");
		content.append("\r\n");
		content.append("您的账号已经成功创建，请点击此链接激活账号：");
		content.append("\r\n");
		content.append("http://www.iteye.com/users/1609152/active?key=19c7ac0c-74a4-36c9-9584-d3738cc4c9ea");
		content.append("\r\n");
		content.append("摄影帮和您一同成长，感谢您的注册！");

		mailService.sendMail("25213908@qq.com","新用户确认通知信",content.toString());
	}

}
