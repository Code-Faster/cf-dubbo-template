package com.createTemplate.admin.web.aop;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.createTemplate.api.base.dubbo.service.UsersService;
import com.createTemplate.model.admin.system.vo.UsersVo;
import com.createTemplate.model.auth.AuthToken;
import com.createTemplate.model.exception.BusinessException;

/**
 * 登录Aop
 *
 * @author libiqi
 */
@Aspect
@Component
public class AuthTokenAOPInterceptor {

    private static Log logger = LogFactory.getLog(WebLogAspect.class);

    @Reference(version = "1.0.0", check = false)
    UsersService personService;

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
        UsersVo user = personService.getCurrentUser(authTokenStr);
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
