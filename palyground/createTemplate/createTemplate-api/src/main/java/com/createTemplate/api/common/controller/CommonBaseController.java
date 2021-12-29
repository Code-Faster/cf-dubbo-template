/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.api.common.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.createTemplate.api.core.doubbo.service.PersonService;
import com.createTemplate.model.core.vo.TPersonVO;
import com.createTemplate.model.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonBaseController extends BaseController {
    @Reference(version = "${app.service.version}", check = false)
    PersonService personService;

    /**
     * 校验当前页和每页的条数
     *
     * @param page
     * @param rows
     */
    public void checkPageRows(Integer page, Integer rows) {
        if (page == null || rows == null) {
            throw new BusinessException("page、rows不能为空");
        }
        if (rows > 500) {
            throw new BusinessException("rows 不能大于500");
        }
    }

    /**
     * @return java.lang.String
     * @Author libiqi
     * @Description 获取用户IP信息
     * @Date 09:19 2019-09-03
     * @Param []
     **/
    public String getIpAddr() {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        log.info("用户访问来源" + ip);
        return ip;
    }

    /**
     * 获取当前用户数据
     *
     * @return
     */
    public TPersonVO getWechatSessionPerson() {
        String authToken = request.getParameter("authToken");
        TPersonVO currentUser = personService.getCurrentUser(authToken);
        if (null == currentUser) {
            throw new BusinessException("用户信息获取失败！");
        }
        return currentUser;
    }
}
