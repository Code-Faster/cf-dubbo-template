package com.createTemplate.api.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 *Cookies工具类
 * @author:  
 * @date: 2018年6月21日 下午4:50:53 
 * @version V1.0
 */
public class CookiesUtils {
    
    /**
     * 
     * @param response
     * @param name
     * @param value
     * @param maxAge
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge > 0) {
			cookie.setMaxAge(maxAge);
		} else if (maxAge == 0) {
			cookie.setMaxAge(0);
		}
        response.addCookie(cookie);
    }

    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie;
        }
        return null;
    }

    private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map cookieMap = new HashMap();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}

        return cookieMap;
    }

    public static String getCookieValueByName(HttpServletRequest request, String name) {
        Map cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie.getValue();
        }
        return null;
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setPath("");
            response.addCookie(cookie);
        }
    }

}
