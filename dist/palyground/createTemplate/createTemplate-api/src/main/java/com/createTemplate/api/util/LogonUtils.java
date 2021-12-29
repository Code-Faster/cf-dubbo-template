package com.createTemplate.api.util;

import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;



public class LogonUtils {

	/**判断验证码输入是否正确*/
	public static boolean checkNumber(String checkNumber,HttpServletRequest request) {
		//获取页面的验证码
		if(StringUtils.isBlank(checkNumber)){
			return false;
		}
		//获取从Session中生成的验证码
		String check_number_key = (String) request.getSession().getAttribute("CHECK_NUMBER_KEY");
		if(StringUtils.isBlank(check_number_key)){
			return false;
		}
		//从页面中获取输入的验证码和Session中生成的验证码进行比对
		return checkNumber.equalsIgnoreCase(check_number_key);
	}

	/**添加记住我的功能*/
	public static void remeberMe(String name,String password,String remeberMe,HttpServletRequest request,
			HttpServletResponse response) {
		//创建2个Cookie，一个存放用户名，一个存放密码
		//如果name中存在中文，需要进行转码
		try {
			name = URLEncoder.encode(name, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Cookie nameCookie = new Cookie("fbUserName", name);
		Cookie passwordCookie = new Cookie("fbUserPassword", password);
		//设置Cookie的有效路径，项目的根路径
		nameCookie.setPath(request.getContextPath()+"/");
		passwordCookie.setPath(request.getContextPath()+"/");
		/**
		 * 获取页面记住我对应的复选框remeberMe的值
		      * 如果值为yes：设置Cookie的有效时间（1周）
		      * 如果值为null：清空Cookie的有效时间
		 */
		if(remeberMe!=null && remeberMe.equals("on")){
			//设置Cookie的有效时间（2周）
			nameCookie.setMaxAge(10*60*60*24*7);
			passwordCookie.setMaxAge(10*60*60*24*7);
		}
		else{
			nameCookie.setMaxAge(0);
			passwordCookie.setMaxAge(0);
		}
		//将Cookie放置到Response对象中
		response.addCookie(nameCookie);
		response.addCookie(passwordCookie);
		
	}
	
	public static String getOpenId(HttpServletRequest request){
		String openId = null;
		HttpSession session = request.getSession();
		System.out.println(session.getAttribute("openId"));
		if(session.getAttribute("openId") != null){
			return session.getAttribute("openId").toString();
		}else{
			Cookie [] cookies = request.getCookies();
			if(cookies!=null && cookies.length>0){
				for(int i=0;i<cookies.length;i++){
					Cookie cookie = cookies[i];
					if(cookie!=null && cookie.getName().equals("openId")){
						openId = cookie.getValue();
					}
				}
			}
			session.setAttribute("openId", openId);
			return openId;
		}
		
	}
	
}
