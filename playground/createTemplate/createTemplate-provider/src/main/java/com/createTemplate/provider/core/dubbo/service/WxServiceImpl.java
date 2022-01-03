package com.createTemplate.provider.core.dubbo.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.createTemplate.api.base.dubbo.service.CommonRedisDao;
import com.createTemplate.api.core.doubbo.service.PersonService;
import com.createTemplate.api.core.doubbo.service.TAccountDetailService;
import com.createTemplate.api.core.doubbo.service.WxService;
import com.createTemplate.api.util.*;
import com.createTemplate.api.util.wxpay.WXPay;
import com.createTemplate.api.util.wxpay.WXPayUtil;
import com.createTemplate.model.base.vo.TWechatEwmVO;
import com.createTemplate.model.constant.CommonConstant;
import com.createTemplate.model.core.pojo.TAccountDetail;
import com.createTemplate.model.core.pojo.TRechargeOrder;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.provider.base.dao.impl.MybatisDaoImpl;
import com.createTemplate.provider.config.OOSAutoConfig;
import com.createTemplate.provider.config.WxPublicConfig;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WxServiceImpl
 * @Author libiqi
 * @Description TODO
 * @Date 2019/11/6 3:22 下午
 * @Version 1.0
 */
@Service(version = "1.0.0", retries = -1, timeout = 12000)
@Slf4j
@AllArgsConstructor
public class WxServiceImpl extends MybatisDaoImpl implements WxService {

    @Autowired
    private OOSAutoConfig ossUtil;
    @Autowired
    private WxPublicConfig wxPublicConfig;
    @Autowired
    private WxPayService wxPayService;

    /**
     * 支付结果通知
     *
     * @param notifyData
     * @return
     */
    @Override
    public String payBack(String notifyData) {
        log.debug("-------------WxService.payBack start");
        // 回调类
        WxPayOrderNotifyResult notifyResult = null;
        // 订单号
        String outTradeNo = null;
        // 回调参数
        String attach = null;
        try {
            notifyResult = wxPayService.parseOrderNotifyResult(notifyData);
            log.info("解析结构：" + JSON.toJSONString(notifyResult));
        } catch (WxPayException e) {
            e.printStackTrace();
            return WxPayNotifyResponse.fail("解析结构错误");
        }
        // 支付成功
        if ("SUCCESS".equalsIgnoreCase(notifyResult.getResultCode())) {
            // 订单编号
            outTradeNo = notifyResult.getOutTradeNo();
            attach = notifyResult.getAttach();
            log.error("-------------------------手机支付回调通------out_trade_no={},attach={}", outTradeNo, attach);
        }
        // 返回数据结构正确，进行处理
        if (StringUtils.isNotBlank(attach) && StringUtils.isNotBlank(outTradeNo)) {
            Map<String, Object> map = JSON.parseObject(attach);
            int type = (int) map.get("type");
            try {
                WxPayOrderQueryResult payResult = wxPayService.queryOrder(null, notifyResult.getOutTradeNo());
                log.info("去微信二次核对返回：" + JSON.toJSONString(payResult));
                if ("SUCCESS".equalsIgnoreCase(payResult.getResultCode())) {
                    // 签名正确
                    // 进行处理。
                    // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                    String transaction_id = notifyResult.getTransactionId();// 微信支付订单号
                    Integer total_fee = notifyResult.getTotalFee();
                    // 处理订单逻辑
                    // type支付类型
                    log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>attach=" + attach + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                    log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>type=" + type + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

                    if (type == CommonConstant.PayFrom.UNDER_RECHARGE) {
                        log.error(">>>>>>>>>>>>>进充值订单操作<<<<<<<<<<<<<<<<<<<");
                        PersonService personService = this.applicationContext.getBean(PersonService.class);
                        personService.rechargeOrderCall(outTradeNo, transaction_id);
                        log.error(">>>>>>>>>>>>>出充值订单操作<<<<<<<<<<<<<<<<<<<");
                    } else {
                        log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>获取参数错误<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                    }
                    log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>支付成功<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                    log.error(">>>>>>>>>>>>>返回数据给微信<<<<<<<<<<<<<<<<<<<" + outTradeNo);
                    return WxPayNotifyResponse.success("成功");
                } else {
                    // 签名错误，如果数据里没有sign字段，也认为是签名错误
                    log.error("-------------------------二次核对错误--------------------------");
                    return WxPayNotifyResponse.fail("二次核对失败！");
                }
            } catch (WxPayException e) {
                e.printStackTrace();
                return WxPayNotifyResponse.fail("二次核对失败！");
            }
        } else {
            log.error("-------------------------手机支付回调通知回调参数错误--------------------------");
            return WxPayNotifyResponse.fail("回调参数错误！");
        }
    }

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
    @Override
    public String weChatPay(String total_fee, String attach, String out_trade_no, String spbill_create_ip, Integer tradeType, String body) {
        log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>进入微信支付申请<<<<<<<<<<<<<<<<<<<<<<<<<<<<total_fee<<" + total_fee
                + "<<<attach>>" + attach + "<<<out_trade_no>>>" + out_trade_no);
        WxPayUnifiedOrderRequest wxPayRequest = new WxPayUnifiedOrderRequest();
        wxPayRequest.setBody(body);
        wxPayRequest.setFeeType("CNY");
        wxPayRequest.setTotalFee(Integer.parseInt(total_fee));// 订单总金额，单位为分
        wxPayRequest.setOutTradeNo(out_trade_no); // 字段名：商户订单号32位
        wxPayRequest.setAttach(attach);// 附加数据
        wxPayRequest.setSpbillCreateIp(spbill_create_ip);// 用户端ip
        wxPayRequest.setNotifyUrl(wxPayService.getConfig().getNotifyUrl());
        // wxPayRequest.setNonceStr(nonceStr);
        wxPayRequest.setTradeType(tradeType == 1 ? "JSAPI" : "APP");
        log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>微信支付<<<<<<<<<<<<<<<<<<<<<<<<<<<<wxPayRequest<<" + JSON.toJSONString(wxPayRequest));
        try {
            WxPayAppOrderResult object = wxPayService.createOrder(wxPayRequest);
            return JSON.toJSONString(object);
        } catch (WxPayException e) {
            log.error("微信预支付订单请求异常:" + e.getMessage());
            e.printStackTrace();
            throw new BusinessException("微信支付参数获取异常");
        }
    }

    /**
     * @return void
     * @Author libiqi
     * @Description 公众号发送模版消息
     * @Date 2:52 下午 2019/11/1
     * @Param [open_id, templete_id, first, remark, offical_url, data]
     **/
    @Override
    public void send(String open_id, String templete_id, String first, String remark, String offical_url,
                     Map<String, Object> data) {
        Map<String, Object> accessTokenMap = AdvancedUtil.getAccessToken(wxPublicConfig.getAppId(),
                wxPublicConfig.getAppSecret());
        Map<String, Object> map = new HashMap<String, Object>();
        // 内容数据 keyword1 keywowrd2等
        Map<String, Object> dataMap = new HashMap<String, Object>();
        for (String key : data.keySet()) {
            Map<String, Object> valueMap = new HashMap<String, Object>();
            valueMap.put("value", data.get(key));
            dataMap.put(key, valueMap);
        }
        // 头数据first
        Map<String, Object> firstMap = new HashMap<String, Object>();
        firstMap.put("value", first);
        // 备注数据
        Map<String, Object> remarkMap = new HashMap<String, Object>();
        remarkMap.put("value", remark);

        dataMap.put("first", first);
        dataMap.put("remark", remark);

        map.put("appid", wxPublicConfig.getAppId());
        map.put("touser", open_id);
        map.put("template_id", templete_id);
        map.put("url", offical_url);
        map.put("data", dataMap);

        String send = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
        String requestUrl = send.replace("ACCESS_TOKEN", accessTokenMap.get("access_token").toString());
        String param = JSON.toJSONString(map);
        log.error("------------send 公众号  templete mgs------paramMap----------" + param);
        JSONObject result = CommmonUtil.httpsRequest(requestUrl, "POST", JSON.toJSONString(map));
        log.error("------------send 公众号  templete mgs------result----------" + result);
    }

    /**
     * @return int
     * @Author libiqi
     * @Description 提现
     * @Date 2:52 下午 2019/11/1
     * @Param [partner_trade_no, openid, amount, re_user_name, person_id, service_charge]
     **/
    @Override
    public int withdrawDeposit(String partner_trade_no, String openid, Integer amount, String re_user_name,
                               Long person_id, int service_charge) throws BusinessException {
        System.out.println("----------------------------进入提现-------------------------------------------------");
        int result = 0;
        // 1.0 拼凑企业支付需要的参数
        System.out.println("------wXPayConfig.getMchId------" + wxPayService.getConfig().getMchId());
        String appid = wxPayService.getConfig().getAppId(); // 微信公众号的appid

        String mch_id = wxPayService.getConfig().getMchId(); // 商户号
        String nonce_str = WXPayUtil.generateNonceStr(); // 生成随机数
        String check_name = "NO_CHECK"; // 是否验证真实姓名呢
        String desc = "提现"; // 企业付款操作说明信息。必填。
        String spbill_create_ip = "127.0.0.1"; //

        // 2.0 生成map集合
        Map<String, String> packageParams = new HashMap<String, String>();
        packageParams.put("mch_appid", appid); // 微信公众号的appid
        packageParams.put("mchid", mch_id); // 商务号
        packageParams.put("nonce_str", nonce_str); // 随机生成后数字，保证安全性

        packageParams.put("partner_trade_no", partner_trade_no); // 生成商户订单号
        packageParams.put("openid", openid); // 支付给用户openid
        packageParams.put("check_name", check_name); // 是否验证真实姓名呢
        packageParams.put("re_user_name", re_user_name);// 收款用户姓名
        packageParams.put("amount", amount + ""); // 企业付款金额，单位为分
        packageParams.put("desc", desc); // 企业付款操作说明信息。必填。
        packageParams.put("spbill_create_ip", spbill_create_ip); // 调用接口的机器Ip地址

        try {
            String key = wxPayService.getConfig().getMchKey();
            // 封装退款对象 生成自己的签名
            packageParams.put("sign", WXPayUtil.generateSignature(packageParams, key));
            System.out.println("------data------" + packageParams);
            // 获取需要发送的url地址
            String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";// 获取退款的api接口
            // 将当前的map结合转化成xml格式
            String reqBody = WXPayUtil.mapToXml(packageParams);
            String resultXml = ssl(url, reqBody, mch_id);
            System.out.println("=========" + resultXml);
            Map<String, String> resp = WXPayUtil.xmlToMap(resultXml);
            System.out.println("------------resp-------" + resp);
            String returnCode = resp.get("return_code"); // 获取返回码
            String result_code = resp.get("result_code");// SUCCESS/FAIL，注意：当状态为FAIL时，存在业务结果未明确的情况。如果如果状态为FAIL，请务必关注错误代码（err_code字段），通过查询查询接口确认此次付款的结果。
            String payment_no = resp.get("payment_no");
            if ("SUCCESS".equalsIgnoreCase(returnCode) && "SUCCESS".equalsIgnoreCase(result_code)) {
                // 添加账户明细数据
                TAccountDetail newTAccountDetailOne = new TAccountDetail();
                newTAccountDetailOne.setPersonId(person_id);
                newTAccountDetailOne.setAccount(amount);
                newTAccountDetailOne.setOrderNo(payment_no);
                newTAccountDetailOne.setStatus(TAccountDetail.STATUS_NORMAL);
                newTAccountDetailOne.setType(TAccountDetail.TYPE_WITHDRAW_DEPOSIT);
                newTAccountDetailOne.setRemark("提现到微信");
                TAccountDetailService accountDetailService = this.applicationContext
                        .getBean(TAccountDetailService.class);
                accountDetailService.saveTAccountDetail(newTAccountDetailOne);

                // 添加手续费账户明细数据
                TAccountDetail tAccountDetailOne = new TAccountDetail();
                tAccountDetailOne.setPersonId(person_id);
                tAccountDetailOne.setAccount(service_charge);
                tAccountDetailOne.setOrderNo(payment_no);
                tAccountDetailOne.setStatus(TAccountDetail.STATUS_NORMAL);
                tAccountDetailOne.setType(TAccountDetail.TYPE_WITHDRAW_DEPOSIT_CONSUME);
                tAccountDetailOne.setRemark("提现到微信手续费");
                accountDetailService.saveTAccountDetail(tAccountDetailOne);
            } else {
                // 9 表示退款失败
                // TODO 调用service的方法 ，存储失败提现的记录咯
                result = 1;
                log.error("------------提现失败-------------------------------------------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("--------错误Message-------------------" + e.getMessage());
        }
        return result;
    }

    @Override
    public Map getUnlimited(TWechatEwmVO tWechatEwmVO) throws BusinessException {
        String send = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=ACCESS_TOKEN";
        CommonRedisDao commonRedisDao = this.applicationContext.getBean(CommonRedisDao.class);
        String appId = wxPublicConfig.getAppId();
        String appSecret = wxPublicConfig.getAppSecret();
        String requestUrl = send.replace("ACCESS_TOKEN",
                WeixinUtil.GetAccessTokenNoCache(commonRedisDao, appId, appSecret));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("scene", tWechatEwmVO.getScene());
        map.put("page", tWechatEwmVO.getPage());
        map.put("width", tWechatEwmVO.getWidth());
        map.put("auto_color", tWechatEwmVO.getAuto_color());
        map.put("is_hyaline", tWechatEwmVO.getIs_hyaline());
        log.error("------------getUnlimited 获取小程序二维码 mgs------paramMap----------" + JSON.toJSONString(map));
        Map upload = ossUtil.upload(
                HttpClientUtils.GetwxacodeUnlimit(requestUrl, new JSONObject().parseObject(JSON.toJSONString(map))),
                ossUtil.getCommonBucket(), "jpg");
        log.error("------------getUnlimited 获取小程序二维码 mgs------result----------" + upload);
        return upload;
    }

    public static String ssl(String url, String data, String mchId) {
        StringBuffer message = new StringBuffer();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            Resource fileRource = new ClassPathResource("apiclient_cert.p12");
            InputStream instream = fileRource.getInputStream();
            keyStore.load(instream, mchId.toCharArray());
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"},
                    null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            HttpPost httpost = new HttpPost(url);

            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(entity.getContent(), "UTF-8"));
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        message.append(text);
                    }
                }
                EntityUtils.consume(entity);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return message.toString();
    }

    /**
     * 校验订单剩余的金额是否还能退款
     *
     * @param out_trade_no
     * @param refundFee
     * @param sum_fee
     * @param totle_fee
     * @author xiongfeng
     * @since 2019年9月24日 下午4:45:54
     */
    private void checkOddAmout(String out_trade_no, int refundFee, int sum_fee, int totle_fee) {
        if (totle_fee < sum_fee) {
            throw new BusinessException("订单号为:" + out_trade_no + "的订单最多能再退"
                    + RoundUtil.round((totle_fee - sum_fee + refundFee) / 100.0d, 2) + "元");
        }
    }

}

