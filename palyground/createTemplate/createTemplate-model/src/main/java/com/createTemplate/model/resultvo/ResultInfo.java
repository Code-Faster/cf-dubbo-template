/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.model.resultvo;

import java.io.Serializable;

import com.createTemplate.model.exception.ResultEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "返回响应数据")
public class ResultInfo<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 返回码 1 正常  0 业务异常 -1 系统异常
     */
    @ApiModelProperty(value = "返回码")
    private Integer code;
    /**
     * 返回消息
     */
    @ApiModelProperty(value = "返回消息")
    private String message;
    /**
     * url请求路径
     */
    @ApiModelProperty(value = "url请求路径")
    private String url;
    /**
     * 返回消息体
     */
    @ApiModelProperty(value = "返回数据对象")
    private T result;


    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public ResultInfo() {
        super();
    }

    public ResultInfo(Integer code, String message, T data) {
        super();
        this.code = code;
        this.message = message;
        this.result = data;
    }

    /**
     * @return
     * @Author libiqi
     * @Description 错误信息处理
     * @Date 10:27 2019-09-04
     * @Param [resultEnum]
     **/
    public ResultInfo(ResultEnum resultEnum) {
        super();
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMsg();
    }

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }


    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}