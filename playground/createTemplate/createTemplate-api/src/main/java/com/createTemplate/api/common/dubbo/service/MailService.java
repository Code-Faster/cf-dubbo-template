/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.api.common.dubbo.service;


import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.mail.SimpleMailMessage;


/**
 * 功能：邮件service接口
 * @author Administrator
 *
 */

public interface MailService  {

	/**
	 * 功能：发送邮件
	 */
	public void send(SimpleMailMessage msg);

	/**
	 * 功能：发送邮件
	 * 		from 发送者邮箱
	 *      to 接受者邮箱
	 *      subject主题
	 *      text邮件内容
	 * 返回：是否成功
	 */
	public void sendTextMail(String to, String subject, String text) throws Exception;
	
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
    public void sendMimeMessage(final String to, final String subject, final String text,final String []filePaths) throws Exception;
    
	/**
	  * @Description：发送绑定用户的邮件  from 发送者邮箱    to 接受者邮箱     subject主题   text邮件内容
	  * @author aijian
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	  * @Version: V1.00 
	  * @Create Date: 2014-2-24
	  * @Parameters：filePathsMap   对应图片资源
	 */
	public void sendMimeMessage(final String to, final String subject, final String text,
			final Map<String, String> filePathsMap) throws MessagingException, UnsupportedEncodingException;

}
