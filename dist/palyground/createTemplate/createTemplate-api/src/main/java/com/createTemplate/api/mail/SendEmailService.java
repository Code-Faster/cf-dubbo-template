/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.api.mail;

import com.createTemplate.api.base.dao.MybatisDao;

/**
 *  发送邮件service
 * @author:  
 * @date: 2018年8月3日 上午10:55:57 
 * @version V1.0
 */
public interface SendEmailService extends MybatisDao{
	
	/**
	 * 发送查看了联系方式邮件
	 * @param email
	 * @param dbCompany
	 */
//	public void sendCommunicateEmail(String recievePersonId,SendPosition dbSendPosition);
	
}
