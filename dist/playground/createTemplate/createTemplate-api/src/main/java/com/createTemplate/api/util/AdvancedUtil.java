package com.createTemplate.api.util;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AdvancedUtil
 * @Author libiqi
 * @Description TODO
 * @Date 2019/11/6 3:36 下午
 * @Version 1.0
 */
public class AdvancedUtil {
    public static String oauth2Url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    public static String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    /**
     * 根据appid 、 appSecret 、 code获取用户信息
     *
     * @param appId
     * @param appSecret
     * @param code
     * @return
     */
    public static WeiXinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
        WeiXinOauth2Token wat = new WeiXinOauth2Token();
        String requestUrl = oauth2Url.replace("APPID", appId).replace("SECRET", appSecret).replace("CODE", code);
        JSONObject jsonObject = CommmonUtil.httpsRequest(requestUrl, "GET", null);
        System.out.println("==1==" + jsonObject);
        if (null != jsonObject) {
            try {
                wat = new WeiXinOauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(jsonObject.getInteger("expires_in"));
                wat.setRefeshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));
            } catch (Exception e) {
                wat = null;
                String errorCode = jsonObject.getString("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                // log.error("获取网页授权凭证失败 errcode{},errMsg", errorCode, errorMsg);
            }

        }
        return wat;
    }

    /**
     * 根据appid 、 appSecret 、获取AccessToken
     *
     * @param appId
     * @param appSecret
     * @return
     */
    public static Map<String, Object> getAccessToken(String appId, String appSecret) {
        Map<String, Object> map = new HashMap<String, Object>();
        String requestUrl = tokenUrl.replace("APPID", appId).replace("APPSECRET", appSecret);
        JSONObject jsonObject = CommmonUtil.httpsRequest(requestUrl, "GET", null);
        System.out.println("==1==" + jsonObject);
        if (null != jsonObject) {
            try {
                map.put("access_token", jsonObject.getString("access_token"));
            } catch (Exception e) {
                String errorCode = jsonObject.getString("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                // log.error("获取网页授权凭证失败 errcode{},errMsg", errorCode, errorMsg);
            }

        }
        return map;
    }
}
