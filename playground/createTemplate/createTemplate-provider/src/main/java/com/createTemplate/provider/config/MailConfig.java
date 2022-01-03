/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.provider.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 邮件配置文件
 * @author:  
 * @date: 2018年8月3日 上午11:00:05 
 * @version V1.0
 */
@Component
public class MailConfig {
    @Value("${spring.mail.username}")
	private String from;
    @Value("${spring.mail.host}")
	private String host;
    @Value("${spring.mail.username}")
	private String username;
    @Value("${spring.mail.password}")
	private String password;
    @Value("${spring.mail.properties.mail.smtp.auth}")
	private String auth;
    @Value("${spring.mail.properties.mail.smtp.timeout}")
	private String timeout;
    @Value("${spring.mail.port}")
	private String port;
    
    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }
    
    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * @return the auth
     */
    public String getAuth() {
        return auth;
    }
    
    /**
     * @return the timeout
     */
    public String getTimeout() {
        return timeout;
    }
    
    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }
	
	
}
