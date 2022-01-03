/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.provider.base.dubbo.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.createTemplate.api.base.dubbo.service.CommonRedisDao;
import com.createTemplate.api.common.dubbo.service.MessageService;
import com.createTemplate.model.constant.RedisConstant;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.provider.base.dao.impl.MybatisDaoImpl;
import com.createTemplate.provider.config.SMSAutoConfiguration;

@Service(version = "1.0.0", retries = -1, timeout = 6000)
public class MessageServiceImpl extends MybatisDaoImpl implements MessageService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // 产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";

    // 产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    @Autowired
    private SMSAutoConfiguration smsConfig;

    @Autowired
    private CommonRedisDao commonRedisDao;

    /**
     * 发送验证码
     *
     * @param phone
     * @param captcha
     */
    @Override
    public void sendMessage(Long phone, String captcha) throws Exception {
        Boolean checkCanCaptcha = checkCanCaptcha(phone);
        if (!checkCanCaptcha) {
            throw new BusinessException("请勿频繁操作!");
        }
        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsConfig.getAppKey(), smsConfig
                .getAppSecret());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        // 必填:待发送手机号
        request.setPhoneNumbers(phone.toString());
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName("我要金蛋网");
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_136090014");
        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\"" + captcha + "\"}");

        // 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        // request.setSmsUpExtendCode("90997");

        // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId(RedisConstant.captcha + phone);

        // hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        //
        if (!sendSmsResponse.getCode().equals("OK")) {
            logger.error(sendSmsResponse.getCode());
            throw new BusinessException("短信发送失败，我们将尽快处理");
        }
        // 将验证码存入redis 并保留5分钟
        commonRedisDao.set(RedisConstant.captcha + phone, captcha, 5);
        // 设置访问限制一分钟
        commonRedisDao.set(RedisConstant.captchaToken + phone, captcha, 1);
        // return sendSmsResponse;
    }

    /**
     * 检验验证码是否正确
     *
     * @param phone
     * @param captcha
     * @return
     */
    @Override
    public Boolean checkCaptchaToken(String phone, String captcha) {
        String redisCaptcha = commonRedisDao.get(RedisConstant.captcha + phone);
        return captcha.equals(redisCaptcha);
    }

    /**
     * 检验一分钟内是否已经发送短信
     *
     * @param phone
     * @return
     */
    @Override
    public Boolean checkCanCaptcha(Long phone) {
        String redisCaptcha = commonRedisDao.get(RedisConstant.captchaToken + phone);
        return StringUtils.isBlank(redisCaptcha);
    }

    /**
     * 查询发送短信详情
     *
     * @param bizId
     * @param phone
     * @return
     * @throws ClientException
     */
    @Override
    public QuerySendDetailsResponse querySendDetails(String bizId, String phone) throws ClientException {

        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsConfig.getAppKey(), smsConfig
                .getAppSecret());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        // 必填-号码
        request.setPhoneNumber(phone);
        // 可选-流水号
        request.setBizId(bizId);
        // 必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        // 必填-页大小
        request.setPageSize(10L);
        // 必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        // hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }

}
