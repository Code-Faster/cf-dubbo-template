/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.personal.web.core.controller;

import com.createTemplate.model.exception.ResultEnum;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.createTemplate.api.common.controller.CommonBaseController;
import com.createTemplate.api.common.dubbo.service.MessageService;
import com.createTemplate.api.core.doubbo.service.PersonService;
import com.createTemplate.api.util.StringUtil;
import com.createTemplate.model.auth.AuthToken;
import com.createTemplate.model.core.vo.TPersonVO;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.resultvo.ResultInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping(value = "/service/message")
@Scope(value = "prototype")
@RestController
@Api(value = "短信controller", tags = {"短信操作接口"})
public class MessageController extends CommonBaseController {

    @Reference(version = "1.0.0",check = false)
    private MessageService messageService;

    @Reference(version = "1.0.0",check = false)
    PersonService personService;

    @ApiOperation(value = "注册发送验证码短信", notes = "注册发送验证码短信")
    @PostMapping("sendMessageByRegister")
    public ResultInfo<Object> sendMessageByRegister(@RequestParam("phone") Long phone) {
        // 发送验证码
        String captcha = StringUtil.generateNumberCaptcha(6);
        // 判断是否已经注册 已经注册抛出错误
//        if (personService.checkPersonIsRepeat(phone)) {
//            throw new BusinessException("此手机号已经注册!");
//        }
        try {
            messageService.sendMessage(phone, captcha);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "发送成功", null);
    }

    @ApiOperation(value = "忘记密码发送验证码短信", notes = "忘记密码发送验证码短信")
    @PostMapping("sendMessageByForget")
    public ResultInfo<Object> sendMessageByForget(@RequestParam("phone") Long phone) {
        // 发送验证码
        String captcha = StringUtil.generateNumberCaptcha(6);
        // 判断是否已经注册 未注册抛出错误
//        if (!personService.checkPersonIsRepeat(phone)) {
//            throw new BusinessException("此手机号未注册!");
//        }
        try {
            messageService.sendMessage(phone, captcha);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "发送成功", null);
    }

    @ApiOperation(value = "登录后发送验证码短信", notes = "登录后发送验证码短信")
    @PostMapping("sendMessageByLogin")
    @AuthToken(type = "osc")
    public ResultInfo<Object> sendMessageByLogin(@RequestParam("phone") Long phone, @RequestParam("authToken") String authToken) {
        TPersonVO currentUser = personService.getCurrentUser(authToken);
        boolean isMe = currentUser.getPhoneNum().equals(phone);
        if (!isMe) {
            throw new BusinessException("非本人操作!");
        }
        // 发送验证码
        String captcha = StringUtil.generateNumberCaptcha(6);
        try {
            messageService.sendMessage(phone, captcha);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "发送成功", null);
    }
}
