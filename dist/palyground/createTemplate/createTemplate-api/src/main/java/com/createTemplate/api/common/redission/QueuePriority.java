/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.api.common.redission;

import java.util.ArrayList;
import java.util.List;

/**
 * 队列优先级
 * @author sjl
 * 创建时间:2015-7-27 下午2:28:08
 */
public class QueuePriority {
	public static List<String> queueNameList = new ArrayList<String>();
	static{
		queueNameList.add("queue_priority_0");
		queueNameList.add("queue_priority_1");
		queueNameList.add("queue_priority_2");
		queueNameList.add("queue_priority_3");
	}
	
	/**
	 * 根据考生人数得到队列名称（通过优先级判断）
	 * @author sjl
	 * 创建时间:2015-7-27 下午2:30:46
	 * @param examineeCount
	 * @return
	 */
	public static String getQueueNameByPriority(Integer examineeCount){
		//默认最低优先级
		String queueName = queueNameList.get(3);
		if(examineeCount!=null){
			if(examineeCount>=0 & examineeCount <= 5000){
				queueName = queueNameList.get(0);
			}else if(examineeCount>5000 & examineeCount <= 10000){
				queueName = queueNameList.get(1);
			}else if(examineeCount>10000 & examineeCount <= 15000){
				queueName = queueNameList.get(2);
			}else{
				queueName = queueNameList.get(3);
			}
		}
		return queueName;
	}
}
