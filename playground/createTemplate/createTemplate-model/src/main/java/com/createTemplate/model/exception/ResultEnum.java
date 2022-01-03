package com.createTemplate.model.exception;

import java.io.Serializable;

/**
 * @ClassName ResultEnum
 * @Author libiqi
 * @Description TODO
 * @Date 2019-09-04 11:16
 * @Version 1.0
 */
public enum ResultEnum implements Serializable {

    UNKNOWN_ERROR(-99, "o(╥﹏╥)o~~系统出异常啦!,请联系管理员!!!"),

    SUCCESS(0, "成功"),

    USER_NOT_EXIT(-2, "用户不存在!"),

    USER_EXIT(-3, "用户已存在!"),

    UNAUTHORIZED(-4, "用户未登录!"),

    FORBIDDEN(-6, "无权限访问!"),

    TOKEN_EXPIRED(-27, "token过期!"),

    BUSSINESS_EXCEPTION(-1, "业务异常!"),

    USER_IS_PROHIBITED(-33, "该账户已禁用，请联系客服！");

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}