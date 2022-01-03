/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.provider.common.dubbo.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.alibaba.dubbo.config.annotation.Service;
import com.createTemplate.api.common.dubbo.service.MailService;
import com.createTemplate.provider.config.MailConfig;



/**
 * 功能：邮件service实现类
 * @author:  
 * @date: 2018年5月31日 下午6:30:54 
 * @version V1.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service(version = "1.0.0", retries = -1, timeout = 6000)
public class MailServiceImpl implements MailService {

	protected final Log logger = LogFactory.getLog(MailServiceImpl.class);
	
	@Autowired
	protected JavaMailSender mailSender;
	
	@Autowired
    protected MailConfig mailConfig;
	
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	/**
	 * 功能：发送邮件
	 */
	@Override
	public void send(SimpleMailMessage msg){
		try{
			mailSender.send(msg);
		} catch (MailException e) {
			this.logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 功能：发送邮件
	 * 		from 发送者邮箱
	 *      to 接受者邮箱
	 *      subject主题
	 *      text邮件内容
	 * 返回：是否成功
	 */
	@Override
	public void sendTextMail(String to, String subject, String text) throws Exception  {
    	
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(mailConfig.getFrom());
	    msg.setTo(to);
	    msg.setSubject(subject);
	    msg.setText(text);
	    send(msg);
	}

	/**
	  * @Description：发送绑定用户的邮件  from 发送者邮箱    to 接受者邮箱     subject主题   text邮件内容
	  * @author aijian
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	  * @Version: V1.00 
	  * @Create Date: 2014-2-24
	  * @Parameters：filePathsMap   对应图片资源
	 */
	@Override
	public void sendMimeMessage(final String to, final String subject, final String text,
			final Map<String, String> filePathsMap) throws MessagingException, UnsupportedEncodingException{
		       MimeMessage mimeMessage = mailSender.createMimeMessage();
	        /**
	           * new MimeMessageHelper(mimeMessage,true)之true个人见解：
	           * 关于true参数,官方文档是这样解释的：
	           * use the true flag to indicate you need a multipart message
	           * 翻译过来就是：使用true,以表明你需要多个消息
	           * 再去翻一下MimeMessageHelper的API,找到这样一句话：
	           * supporting alternative texts, inline elements and attachments
	           * 也就是说,如果要支持内联元素和附件就必须给定第二个参数为true
	           * 否则抛出 java.lang.IllegalStateException 异常
	           */
	        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
	        InternetAddress fromAddress = new InternetAddress(mailConfig.getFrom(), "互生求职招聘","UTF-8"); //设置发件人Email
	        messageHelper.setFrom(fromAddress);
	        messageHelper.setSubject(subject); //设置邮件主题
	        //messageHelper.setText(text);   //设置邮件主题内容
	        messageHelper.setTo(to);          //设定收件人Email
	        messageHelper.setCc(mailConfig.getUsername());
	        messageHelper.setText(text,true); 
	        if(filePathsMap != null && filePathsMap.size() > 0){
		        for(Entry<String, String> entry : filePathsMap.entrySet()){
		        	FileSystemResource img = new FileSystemResource(new File(entry.getValue())); 
		    	    messageHelper.addInline(entry.getKey(),img); 
		        }
	       }
	       mailSender.send(mimeMessage);   
	}
	
	/**
	 * 功能：发送邮件
	 * 		from 发送者邮箱
	 *      to 接受者邮箱
	 *      subject主题
	 *      text邮件内容
	 *      
	 *      filePaths附件路径  src/SpringMail.java
	 * 返回：是否成功
	 */
    @Override
	public void sendMimeMessage(final String to, final String subject, final String text,final String []filePaths) throws Exception {
        
    	//附件文件集合
    	final List files = new ArrayList();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        /**
           * new MimeMessageHelper(mimeMessage,true)之true个人见解：
           * 关于true参数,官方文档是这样解释的：
           * use the true flag to indicate you need a multipart message
           * 翻译过来就是：使用true,以表明你需要多个消息
           * 再去翻一下MimeMessageHelper的API,找到这样一句话：
           * supporting alternative texts, inline elements and attachments
           * 也就是说,如果要支持内联元素和附件就必须给定第二个参数为true
           * 否则抛出 java.lang.IllegalStateException 异常
           */
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
        messageHelper.setFrom(new InternetAddress(mailConfig.getFrom(), "互生求职招聘","UTF-8")); //设置发件人Email
        messageHelper.setSubject(subject); //设置邮件主题
        messageHelper.setText(text);   //设置邮件主题内容
        messageHelper.setTo(to);          //设定收件人Email
        /**
           * ClassPathResource：很明显就是类路径资源,我这里的附件是在项目里的,所以需要用ClassPathResource
           * 如果是系统文件资源就不能用ClassPathResource,而要用FileSystemResource,例：
           * FileSystemResource file = new FileSystemResource(new File("D:/Readme.txt"));
           */
        
        /**
          * MimeMessageHelper的addAttachment方法：
          * addAttachment(String attachmentFilename, InputStreamSource inputStreamSource)
          * InputStreamSource是一个接口,ClassPathResource和FileSystemResource都实现了这个接口
          */
        for(String filePath : filePaths){
        	ClassPathResource file = new ClassPathResource(filePath);
        	messageHelper.addAttachment(file.getFilename(), file); //添加附件
        }
        
        files.clear();
      //发送附件邮件
        mailSender.send(mimeMessage);    
        System.out.println("成功发送带附件邮件!");
    }
	
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
