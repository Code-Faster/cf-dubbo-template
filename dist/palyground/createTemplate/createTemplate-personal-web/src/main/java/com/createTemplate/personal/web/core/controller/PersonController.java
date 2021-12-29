/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.personal.web.core.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.createTemplate.api.common.controller.CommonBaseController;
import com.createTemplate.api.core.doubbo.service.WxService;
import com.createTemplate.api.util.CheckExistParamUtil;
import com.createTemplate.api.util.WXAppletUserInfo;
import com.createTemplate.model.base.vo.TWechatEwmVO;
import com.createTemplate.model.constant.CommonConstant;
import com.createTemplate.model.exception.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.createTemplate.api.base.dubbo.service.CommonRedisDao;
import com.createTemplate.api.common.dubbo.service.MessageService;
import com.createTemplate.api.core.doubbo.service.PersonService;
import com.createTemplate.api.util.StringUtil;
import com.createTemplate.model.auth.AuthToken;
import com.createTemplate.model.core.pojo.TPerson;
import com.createTemplate.model.core.vo.TPersonVO;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.resultvo.ResultInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.Map;

/*
 * 用户控制层
 *
 */
@RestController
@Scope(value = "prototype")
@RequestMapping(value = "/service/person")
@Api(value = "用户controller", tags = {"用户操作接口"})
@Slf4j
public class PersonController extends CommonBaseController {
    @Reference(version = "1.0.0", check = false)
    PersonService personService;

    @Reference(version = "1.0.0", check = false)
    private CommonRedisDao commonRedisDao;

    @Resource
    private HttpServletRequest request;

    @Reference(version = "1.0.0", check = false)
    private MessageService messageService;

    @Reference(version = "1.0.0")
    WxService wxService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "code", required = true, dataType = "String", paramType = "query"),})
    @ApiOperation(value = "获取微信小程序用户信息", notes = "获取微信小程序用户信息")
    @RequestMapping(value = "/getAppletUserInfo", method = RequestMethod.POST)
    public ResultInfo<Object> getAppletUserInfo(@RequestBody(required = false) TPersonVO personVo) {
        /** 获取code */
        String code = personVo.getCode();
        Map result = new HashMap();
        if (!"authdeny".equals(code)) {
            /** 根据code获取openId */
            JSONObject appletSession = personService.getSessionKeyOropenid(code);
            log.error("-------------getAppletUserInfo.appletSession =" + appletSession == null ? "null"
                    : appletSession.toJSONString());
            if (appletSession == null || StringUtil.checkNull(appletSession.getString("openid"))) {
                log.error("-------------getAppletUserInfo.appletSession =" + appletSession == null ? "null"
                        : appletSession.toJSONString());
                throw new BusinessException("openId为空，获取信息失败。");
            }
            result.put("appletSession", appletSession);
            TPersonVO sessionPersonVo = personService.saveOrloginByAppletOpenId(appletSession.getString("openid"),
                    appletSession);
            result.put("userInfo", sessionPersonVo);
            result.put("openId", appletSession.get("openid"));
        }
        return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), "成功", result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "authToken", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "nickName", value = "微信姓名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "avatarUrl", value = "头像地址", required = false, dataType = "String", paramType = "query"),})
    @ApiOperation(value = "更新微信小程序用户信息", notes = "更新微信小程序用户信息")
    @RequestMapping(value = "/updateAppletUserInfo", method = RequestMethod.POST)
    @AuthToken(type = CommonConstant.SESSION_TYPE_WECHAT)
    public ResultInfo<Object> updateAppletUserInfo(@RequestBody(required = false) TPersonVO personVo) {
        TPersonVO wechatSessionPerson = super.getWechatSessionPerson();
        personVo.setId(wechatSessionPerson.getId());
        personService.updatePersonById(personVo);
        return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), "成功", super.getWechatSessionPerson());
    }

    /**
     * 获取当前登录用户信息
     *
     * @param person
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "authToken", required = true, dataType = "String", paramType = "query"),})
    @ApiOperation(value = "获取当前登录用户信息", notes = "获取当前登录用户信息")
    @RequestMapping(value = "/getCurrentUser", method = RequestMethod.POST)
    @AuthToken(type = CommonConstant.SESSION_TYPE_WECHAT)
    public ResultInfo<TPersonVO> getCurrentUser(@RequestBody(required = false) TPersonVO person) {
        return new ResultInfo<TPersonVO>(ResultEnum.SUCCESS.getCode(), "成功", super.getWechatSessionPerson());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "authToken", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "recharge_money", value = "充值金额  以分为单位", required = true, dataType = "Integer", paramType = "query"),})
    @ApiOperation(value = "用户充值", notes = "用户充值")
    @RequestMapping(value = "/accountsRecharge", method = RequestMethod.POST)
    @AuthToken(type = CommonConstant.SESSION_TYPE_WECHAT)
    public ResultInfo<Object> accountsRecharge(@RequestBody(required = false) TPersonVO tPersonVO) {
        if (tPersonVO.getRechargeMoney() == null) {
            throw new BusinessException("请输入充值金额");
        }
        TPersonVO sessionPerson = super.getWechatSessionPerson();
        tPersonVO.setId(sessionPerson.getId());
        tPersonVO.setSpbillCreateIp("127.0.0.1");
        return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), "成功", personService.accountsRecharge(tPersonVO));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "authToken", required = true, dataType = "String", paramType = "query"),})
    @ApiOperation(value = "获取账户余额", notes = "获取账户余额")
    @RequestMapping(value = "/getAccountBalance", method = RequestMethod.POST)
    @AuthToken(type = CommonConstant.SESSION_TYPE_WECHAT)
    public ResultInfo<Object> getAccountBalance() {
        TPersonVO sessionPerson = super.getWechatSessionPerson();
        return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), "获取成功",
                personService.queryPersonSurplusMoeny(sessionPerson.getId()));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "authToken", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "advanceMoney", value = "提现金额  以分为单位", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "advanceChannel", value = "提现渠道 0 提现到微信余额；1提现到银行卡", required = true, dataType = "Integer", paramType = "query"),
    })
    @ApiOperation(value = "提现", notes = "提现")
    @RequestMapping(value = "/withdrawDeposit", method = RequestMethod.POST)
    @AuthToken(type = CommonConstant.SESSION_TYPE_WECHAT)
    public ResultInfo<Object> withdrawDeposit(@RequestBody(required = false) TPersonVO tPersonVO) {
        String authToken = request.getParameter("authToken");
        CheckExistParamUtil.getInstance()
                .addCheckParam("authToken", authToken, "authToken")
                .addCheckParam("advanceMoney", tPersonVO.getAdvanceMoney(), "提现金额")
                .addCheckParam("advanceChannel", tPersonVO.getAdvanceChannel(), "提现渠道")
                .check();
        TPersonVO sessionPerson = personService.getCurrentUser(authToken);
        tPersonVO.setId(sessionPerson.getId());
        personService.withdrawDeposit(tPersonVO);
        return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), "提现成功", null);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "scene", value = "scene", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "page", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "width", value = "width", required = true, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "auto_color", value = "auto_color", required = true, dataType = "Boolean", paramType = "query"),
            @ApiImplicitParam(name = "is_hyaline", value = "is_hyaline", required = true, dataType = "Boolean", paramType = "query"),})
    @ApiOperation(value = "获取小程序二维码", notes = "获取小程序二维码")
    @RequestMapping(value = "/getUnlimited", method = RequestMethod.POST)
    @AuthToken(type = CommonConstant.SESSION_TYPE_WECHAT)
    public ResultInfo<Object> getUnlimited(@RequestBody(required = false) TWechatEwmVO tWechatEwmVO) {
        CheckExistParamUtil.getInstance().addCheckParam("scene", tWechatEwmVO.getScene(), "scene")
                .addCheckParam("page", tWechatEwmVO.getPage(), "page")
                .addCheckParam("width", tWechatEwmVO.getWidth(), "width").check();
        return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), "获取成功", wxService.getUnlimited(tWechatEwmVO));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "encryptedData", value = "包括敏感数据在内的完整用户信息的加密数据", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "iv", value = "加密算法的初始向量", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sessionKey", value = "数据进行加密签名的密钥", required = true, dataType = "String", paramType = "query")})
    @ApiOperation(value = "获取小程序手机号", notes = "获取小程序手机号")
    @RequestMapping(value = "/getAppletPhone", method = RequestMethod.POST)
    @AuthToken(type = CommonConstant.SESSION_TYPE_WECHAT)
    public ResultInfo<Object> getAppletPhone(@RequestBody(required = false) TPersonVO tPersonVO) {
        CheckExistParamUtil.getInstance().addCheckParam("iv", tPersonVO.getIv(), "加密算法的初始向量")
                .addCheckParam("encryptedData", tPersonVO.getEncryptedData(), "包括敏感数据在内的完整用户信息的加密数据")
                .addCheckParam("sessionKey", tPersonVO.getSessionKey(), "数据进行加密签名的密钥").check();
        JSONObject object = WXAppletUserInfo.getUserInfo(tPersonVO.getEncryptedData(), tPersonVO.getSessionKey(),
                tPersonVO.getIv());
        if (object != null && StringUtil.checkNotNull(object.getString("purePhoneNumber"))) {
            return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), "获取成功", object.getString("purePhoneNumber"));
        } else {
            throw new BusinessException("微信获取的手机号码错误!");
        }
    }

}
