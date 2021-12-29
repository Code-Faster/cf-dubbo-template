/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.personal.web.aop;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.createTemplate.api.core.doubbo.service.PersonService;
import com.createTemplate.api.util.JSONParamUtils;
import com.createTemplate.model.auth.AuthToken;
import com.createTemplate.model.core.vo.TPersonVO;
import com.createTemplate.model.exception.BusinessException;

/**
 * 登录Aop
 *
 * @author libiqi
 */
@Aspect
@Component
public class AuthTokenAOPInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Reference(version = "1.0.0", check = false)
    PersonService personService;

    private static final String authFieldName = "authToken";

    @Pointcut("@annotation( com.createTemplate.model.auth.AuthToken)")
    public void controllerAspect() {
    }

    @Before(value = "controllerAspect() && @annotation(authToken)", argNames = "authToken")
    public void before(final JoinPoint joinPoint, AuthToken authToken) throws Throwable {
        logger.info("=====前置通知开始=====");
        boolean isFound = false;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String authTokenStr = request.getParameter(authFieldName);
        if (StringUtils.isBlank(authTokenStr)) {
            throw new BusinessException("用户token令牌失效！", 2);
        }
        TPersonVO user = personService.getCurrentUser(authTokenStr);
        if (user == null) {
            throw new BusinessException("用户token令牌失效！", 2);
        }
        if (StringUtils.isNotBlank(authTokenStr))
            isFound = true;
        if (!isFound) {
            throw new BusinessException("用户token令牌失效！", 2);
        }
        logger.info("=====前置通知结束=====");
    }

}
