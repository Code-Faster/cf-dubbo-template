package com.createTemplate.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Spring Boot 应用启动类
 * 
 * @author: Administrator
 * @date: 2018年5月15日 下午2:02:02
 * @version V1.0
 */
@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication
@EnableSwagger2
public class Application {

    public static void main(String[] args) {
        // 程序启动入口
        // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
        SpringApplication.run(Application.class, args);
    }
}
