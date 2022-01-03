/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.model.exception;

import java.io.Serializable;

import com.createTemplate.model.resultvo.ResultInfo;

public class BusinessException extends BaseException implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 返回码 0 正常 -1 业务异常 -99 系统异常
     */
    private Integer code;

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

    public BusinessException() {
        this.code = ResultEnum.BUSSINESS_EXCEPTION.getCode();
    }

    public BusinessException(String messageKey) {
        super(messageKey);
        ExceptionCause cause = new ExceptionCause(messageKey);
        addCause(cause);
        this.code = ResultEnum.BUSSINESS_EXCEPTION.getCode();
    }

    public BusinessException(String messageKey, Integer code) {
        super(messageKey);
        ExceptionCause cause = new ExceptionCause(messageKey);
        addCause(cause);
        this.code = code;
    }

    public BusinessException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        ExceptionCause cause = new ExceptionCause(resultEnum.getMsg());
        addCause(cause);
        this.code = resultEnum.getCode();
    }
}
