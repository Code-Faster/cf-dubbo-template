/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.model.mq.message;

import java.io.Serializable;

/**
 * 消息
 * @author 
 *
 */
public class DefaultMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 0  面试短信*/
	private String messageType;
	
	
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
}
