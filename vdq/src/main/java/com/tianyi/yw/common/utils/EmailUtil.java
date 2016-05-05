/**
 * 
 */
package com.tianyi.yw.common.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 邮件发�?工具�?
 * @author Leo
 */
public class EmailUtil {
	
	/**
	 * 是否�?��身份验证 
	 */
    private static String validate = "true";   	
	/**
	 * 邮件发�?服务器地�?
	 */
	private String mailServerHost;	
	/**
	 * 登陆邮件发�?服务器的用户名和密码   
	 */
    private String userName;   
    private String password;
    /**
	 * 邮件发�?者的地址     
	 */
    private String fromAddress;   
    
    /**
     * 邮件发�?工具类构造函�?
     * @param mailServerHost 邮件发�?服务器地�?
     * @param fromAddress 邮件发�?者的地址     
     * @param userName 登陆邮件发�?服务器的用户�?
     * @param password 登陆邮件发�?服务器的密码   
     */
    public EmailUtil(String mailServerHost,String fromAddress,String userName,String password) {
    	this.mailServerHost = mailServerHost;
    	this.fromAddress = fromAddress;
    	this.userName = userName;
    	this.password = password;
    }
    
	/**
	 * 以文本格式发送邮�? 
	 * @param subject 邮件主题
	 * @param fromName 发�?方名�?
	 * @param to 接收方的email地址
	 * @param text 文本邮件内容
	 * @return 
	 */
	public boolean sendMail(String subject, String fromName, String to, String text) {
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", mailServerHost);
			props.put("mail.smtp.auth", validate);
			Session session = Session.getDefaultInstance(props,
					new Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(userName,
									password);
						}
					});
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress,fromName));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setSentDate(new Date());
			message.setText(text);
			Transport.send(message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 以HTML格式发�?邮件 
	 * 
	 * @param subject 邮件主题
	 * @param fromName 发�?方名�?
	 * @param to 目标邮件地址
	 * @param text 带html标签的邮件内�?
	 * @return
	 */
	public boolean sendHtmlMail(String subject, String fromName, String to, String text) {
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", mailServerHost);
			props.put("mail.smtp.auth", validate);
			Session session = Session.getDefaultInstance(props,
					new Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(userName,
									password);
						}
					});
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress,fromName));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setSentDate(new Date());
			message.setContent(text, "text/html;charset=gb2312");
			Transport.send(message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
