package com.createTemplate.api.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName CommmonUtil
 * @Author libiqi
 * @Description TODO
 * @Date 2019/11/6 3:38 下午
 * @Version 1.0
 */
public class CommmonUtil {
    /**
     * 发送https请求
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        try {
            String str = HttpClientUtils.postToServer(requestUrl, new JSONObject().parseObject(outputStr));

            System.out.println(str);
            jsonObject = JSON.parseObject(str);
            System.out.println("---------------" + jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
//                    log.error("https请求异常：{}", e);
        }
        return jsonObject;
    }
}
