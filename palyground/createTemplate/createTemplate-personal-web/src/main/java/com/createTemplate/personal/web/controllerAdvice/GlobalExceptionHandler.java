/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.personal.web.controllerAdvice;

import javax.servlet.http.HttpServletRequest;

import com.createTemplate.model.exception.ResultEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.resultvo.ResultInfo;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResultInfo<String> jsonErrorHandler(HttpServletRequest req, BusinessException e) throws Exception {
        ResultInfo<String> r = new ResultInfo<String>();
        r.setCode(e.getCode() == null ? ResultEnum.BUSSINESS_EXCEPTION.getCode() : e.getCode());
        r.setMessage(StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : ResultEnum.BUSSINESS_EXCEPTION.getMsg());
        return r;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultInfo<String> jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        e.printStackTrace();
        ResultInfo<String> r = new ResultInfo<>();
        r.setCode(ResultEnum.UNKNOWN_ERROR.getCode());
        r.setMessage(ResultEnum.UNKNOWN_ERROR.getMsg());
        r.setUrl(req.getRequestURL().toString());
        logger.error(r.getMessage(), e);
        return r;
    }

}