package com.createTemplate.api.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.createTemplate.api.base.dubbo.service.CommonRedisDao;
import com.createTemplate.model.constant.RedisConstant;

/**
 * @author : libiq
 * @Description : 操作微信接口
 * @Create Date: 2015-1-7下午3:57:23
 */
public class WeixinUtil {
	/** 缓存1小时50分钟 */
	private static final long cashTime = 1000 * 60 * 55 * 2;

	/***
	 * ------------------------------------------- API地址
	 * ------------------------------------------------------------
	 */
	/**
	 * @Description: 获取access_token
	 * @Parameters:@param Appid(require) Appid
	 * @Parameters:@param Secret(require) 管理组的凭证密钥 Https请求方式: GET
	 */
	private static String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=Appid&secret=Secret";

	/**
	 * @Description: 获取jsapi_ticket
	 * @Parameters:@param ACCESS_TOKEN(require) ACCESS_TOKEN
	 */
	private static String jsapiTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

	/** 微信获取临时素材 */
	private static final String mediaGet = "http://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";

	/***
	 * ------------------------------------------- 公共方法
	 * ------------------------------------------------------------
	 */
	/**
	 * @Description: 建立链接获取access_token
	 * @Author: libiq
	 * @Version: V1.00
	 * @Create Date: 2015-1-7下午1:17:01
	 * @Parameters:@return
	 */
	public static String GetAccessTokenNoCache(CommonRedisDao commonRedisDao, String appid, String appSecret) {
		long curTime = System.currentTimeMillis();
		String key = RedisConstant.accessToken + appid;
		String accessTokenStr = commonRedisDao.get(key);
		String accessToken = null;
		if (StringUtils.isNotBlank(accessTokenStr)) {
			JSONObject accessTokenObj = JSONObject.parseObject(accessTokenStr);
			if (curTime - accessTokenObj.getLongValue("begin_time") >= cashTime) {
				return updateWeixinAccessToken(commonRedisDao, appid, appSecret);
			} else {
				accessToken = accessTokenObj.getString("access_token");
			}
		} else {
			return updateWeixinAccessToken(commonRedisDao, appid, appSecret);
		}
		return accessToken;
	}

	/**
	 * @Description:更新微信AccessToken
	 * @Author: libiq
	 * @Version: V1.00
	 * @Create Date: 2015-1-16下午3:56:38
	 * @Parameters:
	 */
	public static String updateWeixinAccessToken(CommonRedisDao commonRedisDao, String appid, String appSecret) {
		long curTime = System.currentTimeMillis();
		System.out.println("更新缓存,curTime=" + curTime);
		String key = RedisConstant.accessToken + appid;
		String accessToken = JSON
				.parseObject(
						HttpSendRequest.sendHttpsGet(tokenUrl.replace("Appid", appid).replace("Secret", appSecret)))
				.getString("access_token");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("access_token", accessToken);
		jsonObject.put("curTime", curTime);
		commonRedisDao.set(key, jsonObject.toJSONString());
		return accessToken;
	}

	/**
	 * @Description: 建立链接获取JsapiTicket
	 * @Author: libiq
	 * @Parameters:@return
	 */
	public static String GetJsapiTicketNoCache(CommonRedisDao commonRedisDao, String appid, String appSecret) {
		long curTime = System.currentTimeMillis();
		String key = RedisConstant.jsapi_Ticket + appid;
		String jsapi_TicketStr = commonRedisDao.get(key);
		String jsapi_Ticket = null;
		if (StringUtils.isNotBlank(jsapi_TicketStr)) {
			JSONObject jsapi_TicketObj = JSONObject.parseObject(jsapi_TicketStr);
			if (curTime - jsapi_TicketObj.getLongValue("begin_time") >= cashTime) {
				return updateJsapiTicket(commonRedisDao, appid, appSecret);
			} else {
				jsapi_Ticket = jsapi_TicketObj.getString("jsapi_Ticket");
			}
		} else {
			return updateJsapiTicket(commonRedisDao, appid, appSecret);
		}
		return jsapi_Ticket;
	}

	/**
	 * 获取微信JSApi_ticket
	 */
	public static String updateJsapiTicket(CommonRedisDao commonRedisDao, String appid, String appSecret) {
		long curTime = System.currentTimeMillis();
		String key = RedisConstant.jsapi_Ticket + appid;
		System.out.println("更新ticket缓存,curTime=" + curTime);
		String jsapi_Ticket = JSON.parseObject(HttpSendRequest.sendHttpsGet(
				jsapiTicketUrl.replace("ACCESS_TOKEN", GetAccessTokenNoCache(commonRedisDao, appid, appSecret))))
				.getString("ticket");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jsapi_Ticket", jsapi_Ticket);
		jsonObject.put("curTime", curTime);
		commonRedisDao.set(key, jsonObject.toJSONString());
		return jsapi_Ticket;
	}

	public static Map<String, String> sign(String jsapi_ticket, String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String string1;
		String signature = "";
		// 注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);
		return ret;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
