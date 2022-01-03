package com.createTemplate.api.util;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

public class PropertyUtils {
    public static final char UNDERLINE_CHAR = '_';

    public static String getPropertyNames(Class clazz) {
        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(clazz);
        StringBuffer sb = new StringBuffer();
        if (descriptors.length > 0) {
            for (int i = 0; i < descriptors.length; ++i) {
                if (!("class".equalsIgnoreCase(descriptors[i].getName()))) {
                    sb.append(camel2Underline(descriptors[i].getName())).append(",");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String getPropertyNames(Class clazz, String alias) {
        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(clazz);
        StringBuffer sb = new StringBuffer();
        if (descriptors.length > 0) {
            for (int i = 0; i < descriptors.length; ++i) {
                if (!("class".equalsIgnoreCase(descriptors[i].getName()))) {
                    sb.append(alias).append(".").append(camel2Underline(descriptors[i].getName())).append(",");
                }
            }

            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String getPropertyNames(Class clazz, String alias, String... columnArray) {
        Map map = new HashMap();
        for (String tempStr : columnArray) {
            map.put(tempStr, tempStr);
        }

        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(clazz);
        StringBuffer sb = new StringBuffer();
        if (descriptors.length > 0) {
            for (int k = 0; k < descriptors.length; ++k) {
                if (map.containsKey(descriptors[k].getName())) {
                    continue;
                }

                if (!("class".equalsIgnoreCase(descriptors[k].getName()))) {
                    sb.append(alias).append(".").append(descriptors[k].getName()).append(",");
                }
            }

            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 驼峰转下划线
     *
     * @param camelStr
     * @return
     */
    public static String camel2Underline(String camelStr) {

        if (StringUtils.isEmpty(camelStr)) {
            return StringUtils.EMPTY;
        }

        int len = camelStr.length();
        StringBuilder strb = new StringBuilder(len + len >> 1);
        for (int i = 0; i < len; i++) {

            char c = camelStr.charAt(i);
            if (Character.isUpperCase(c)) {
                strb.append(UNDERLINE_CHAR);
                strb.append(Character.toLowerCase(c));
            } else {
                strb.append(c);
            }
        }
        return strb.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param underlineStr
     * @return
     */
    public static String underline2Camel(String underlineStr) {

        if (StringUtils.isEmpty(underlineStr)) {
            return StringUtils.EMPTY;
        }

        int len = underlineStr.length();
        StringBuilder strb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = underlineStr.charAt(i);
            if (c == UNDERLINE_CHAR && (++i) < len) {

                c = underlineStr.charAt(i);
                strb.append(Character.toUpperCase(c));
            } else {
                strb.append(c);
            }
        }
        return strb.toString();
    }
}