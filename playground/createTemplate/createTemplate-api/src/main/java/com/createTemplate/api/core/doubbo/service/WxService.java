package com.createTemplate.api.core.doubbo.service;

import com.createTemplate.api.base.dao.MybatisDao;
import com.createTemplate.model.base.vo.TWechatEwmVO;
import com.createTemplate.model.exception.BusinessException;

import java.util.Map;

public interface WxService extends MybatisDao {
    /**
     * 支付结果通知
     *
     * @param notifyData
     * @return
     */
    public String payBack(String notifyData) throws BusinessException;

    /**
     * 微信公众号/小程序支付
     *
     * @param attach           附加数据 ,主要用来回调判断订单类型 CommonConstant.PayFrom
     * @param out_trade_no     订单号
     * @param total_fee        支付金额
     * @param spbill_create_ip APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
     * @param tradeType        1：JSAPI 其他 APP
     * @param body             订单备注 慢慢早教-课包购买；慢慢早教-慢慢币充值；慢慢早教-会员购买；
     * @return
     * @throws Exception
     */
    public String weChatPay(String total_fee, String attach, String out_trade_no, String spbill_create_ip, Integer tradeType, String body) throws BusinessException;

    /**
     * 微信发送通知模板
     *
     * @param open_id     通知人
     * @param templete_id 模板id
     * @param first       消息头
     * @param remark      消息备注
     * @param offical_url 跳转管理地址 如果没有 不需要填
     * @param data        消息内容 例如 keyword1 keyword2等
     * @throws BusinessException
     */
    public void send(String open_id, String templete_id, String first, String remark, String offical_url,
                     Map<String, Object> data) throws BusinessException;

    /**
     * 提现
     *
     * @param partner_trade_no //商户订单号
     * @param openid           //微信用户 openid
     * @param amount           //提现金额
     * @param re_user_name     //名字
     * @return
     * @throws BusinessException
     */
    public int withdrawDeposit(String partner_trade_no, String openid, Integer amount, String re_user_name,
                               Long person_id, int service_charge) throws BusinessException;

    /**
     * 获取小程序二维码
     *
     * @return
     */
    public Map getUnlimited(TWechatEwmVO tWechatEwmVO) throws BusinessException;

}