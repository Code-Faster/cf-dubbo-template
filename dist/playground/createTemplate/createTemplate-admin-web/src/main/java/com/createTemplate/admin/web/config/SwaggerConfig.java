/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.admin.web.config;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import com.alibaba.druid.sql.visitor.functions.Concat;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.createTemplate.model.resultvo.ResultInfo;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * SwaggerConfig
 */
@Configuration
public class SwaggerConfig {


    private String initContextInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE-AI 开发者辅助工作台");
        return sb.toString();
    }

    @Bean
    public Docket restfulApi() {
        System.out.println("http://localhost:8082" 
//                + pathMapping 
                + "/swagger-ui.html");
        return new Docket(DocumentationType.SWAGGER_2).groupName("RestfulApi")
                .apiInfo(apiInfo())
                // .genericModelSubstitutes(DeferredResult.class)
                .genericModelSubstitutes(ResultInfo.class)
                .useDefaultResponseMessages(true)
                .forCodeGeneration(false)
//                .pathMapping(pathMapping) // base，最终调用接口后会和paths拼接在一起
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.createTemplate"))
                .paths(PathSelectors.any())
                .build();
        // .select().paths(Predicates.not(PathSelectors.regex("/error.*"))).build().apiInfo(initApiInfo());

    }
    
  
    
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CREATE-AI 开发者辅助工作台")
                .description( initContextInfo())
                .termsOfServiceUrl("lian")
                .contact(new Contact("libiqi", "https://github.com/274793580", "libq@haiyungroup.com"))
                .version("1.0")
                .build();
    }
    
    /**
     * 设置过滤规则 这里的过滤规则支持正则匹配 //若有静态方法在此之前加载就会报集合相关的错误.
     * 
     * @return
     */
    private Predicate<String> doFilteringRules() {
        // return Predicates.not(PathSelectors.regex("/error.*"));
        // return or(regex("/hello.*"),
        // regex("/rest/adxSspFinanceManagement.*"));//success
        return or(regex("/book.*"),regex("/hello.*"), regex("/rest.*"));
    }
}
