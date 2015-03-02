package com.photography.service;

import javax.annotation.Resource;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.photography.utils.CustomizedPropertyPlaceholderConfigurer;

/**
 * 
 * @author 徐雁斌
 * @since 2015-3-1
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Service
public class MailServiceImpl implements IMailService{

	@Resource(name="javaMailSender")
	private MailSender javaMailSender;
	
	@Resource(name="simpleMailMessage")
	private SimpleMailMessage simpleMailMessage;
	
	public void setJavaMailSender(MailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessage = simpleMailMessage;
	}

	@Override
	public void sendMail(String sendTo, String subject, String content) {
		simpleMailMessage.setTo(sendTo);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(content);
		javaMailSender.send(simpleMailMessage);
	}

	@Override
	public void sendConfirmMail(String sendTo,String key) {
		String subject = (String) CustomizedPropertyPlaceholderConfigurer.getContextProperty("mail.confirm.subject");
        
		StringBuffer content = new StringBuffer();
        content.append(CustomizedPropertyPlaceholderConfigurer.getContextProperty("mail.confirm.content1"));
        String content2 = (String) CustomizedPropertyPlaceholderConfigurer.getContextProperty("mail.confirm.content2");
        content.append(content2.replace("{key}", key));
        content.append(CustomizedPropertyPlaceholderConfigurer.getContextProperty("mail.confirm.content3"));
        sendMail(sendTo,subject,content.toString());
	}

}
