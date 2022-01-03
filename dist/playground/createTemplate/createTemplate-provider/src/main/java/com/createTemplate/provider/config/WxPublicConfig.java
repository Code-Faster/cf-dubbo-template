package com.createTemplate.provider.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName WxPublicConfig
 * @Author libiqi
 * @Description 微信公共配置
 * @Date 2019/11/6 3:33 下午
 * @Version 1.0
 */
@Configuration
@Data
public class WxPublicConfig {
    @Value("${spring.wx.applet.appId}")
    @ApiModelProperty(value = "小程序Appid")
    private String appId;

    @Value("${spring.wx.applet.appSecret}")
    @ApiModelProperty(value = "小程序AppSecret")
    private String appSecret;

    @Value("${spring.wx.public.appId}")
    @ApiModelProperty(value = "公众号Appid")
    private String publicAppId;

    @Value("${spring.wx.public.appSecret}")
    @ApiModelProperty(value = "公众号AppSecret")
    private String publicAppSecret;
}
