package com.createTemplate.api.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.createTemplate.model.exception.BusinessException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HttpClientUtils
 * @Author libiqi
 * @Description TODO
 * @Date 2019/11/6 3:38 下午
 * @Version 1.0
 */
public class HttpClientUtils {
    /**
     * HTTPAUTH BASIC 认证 post访问
     *
     * @param url      接口地址
     * @param param    接口参数
     * @param userName 用户名
     * @param password 密码
     * @return
     * @auther xf
     * @date 2015-9-16 下午2:21:02
     */
    public static String authPostToServer(String url, Map<String, Object> param, String userName, String password) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));
            List<NameValuePair> postparams = new ArrayList<NameValuePair>();

            for (String key : param.keySet()) {
                postparams.add(new BasicNameValuePair(key, param.get(key).toString()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postparams, "UTF-8");
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity();
                String createResult = EntityUtils.toString(httpEntity, "UTF-8");
                return createResult;
            } else {
                HttpEntity httpEntity = httpResponse.getEntity();
                String createResult = EntityUtils.toString(httpEntity, "UTF-8");
                return createResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
    }


    /**
     * post访问
     *
     * @param param 接口参数
     * @return
     * @auther xf
     * @date 2015-9-16 下午2:21:02
     * 接口地址
     */
    public static String postToServer(String url, JSONObject param) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            if (param == null) {
                param = new JSONObject();
            }
            StringEntity s = new StringEntity(param.toString(), Charset.forName("UTF-8"));
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");// 发送json数据需要设置contentType
            httpPost.setEntity(s);
            httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity();
                String createResult = EntityUtils.toString(httpEntity, "UTF-8");
                return createResult;
            } else {
                HttpEntity httpEntity = httpResponse.getEntity();
                String createResult = EntityUtils.toString(httpEntity, "UTF-8");
                return createResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * @param url
     * @param param
     * @return
     */
    public static ByteArrayInputStream GetwxacodeUnlimit(String url, JSONObject param) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            if (param == null) {
                param = new JSONObject();
            }
            StringEntity s = new StringEntity(param.toString(), Charset.forName("UTF-8"));
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");// 发送json数据需要设置contentType
            httpPost.setEntity(s);
            httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            final HttpEntity httpEntity = httpResponse.getEntity();
            if (statusCode == HttpStatus.SC_OK) {
                if (httpEntity.isStreaming()) {
                    byte[] bs = EntityUtils.toByteArray(httpEntity);
                    return new ByteArrayInputStream(bs);
                } else {
                    String str = EntityUtils.toString(httpEntity, "utf-8");
                    throw new BusinessException("异常代码：" + JSON.parseObject(str).get("errcode").toString());
                }
            } else {
                String str = EntityUtils.toString(httpEntity, "utf-8");
                throw new BusinessException("异常代码：" + JSON.parseObject(str).get("errcode").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
    }
}
