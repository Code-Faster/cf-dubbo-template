/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.api.common.dubbo.service;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.exceptions.ClientException;

/**
 * 消息服务
 *
 * @author: libiqi
 * @date: 2018年5月29日 下午1:50:01
 * @version V1.0
 */
public interface MessageService {

    /**
     * 发送短信信息
     * 
     * @param phone
     *            发送手机号
     * @param msg
     *            发送消息
     * @return
     * @throws Exception
     */
    void sendMessage(Long phone, String msg) throws Exception;

    /**
     * 查询短信详情
     * 
     * @param bizId
     * @param phone
     * @return
     * @throws ClientException
     */
    QuerySendDetailsResponse querySendDetails(String bizId, String phone) throws ClientException;

    /**
     * 检验验证码是否正确
     * 
     * @param phone
     * @param captcha
     * @return
     */
    Boolean checkCaptchaToken(String phone, String captcha);

    /**
     * 检验1分钟内是否已经发送短信
     * 
     * @param phone
     * @return
     */
    Boolean checkCanCaptcha(Long phone);
}
