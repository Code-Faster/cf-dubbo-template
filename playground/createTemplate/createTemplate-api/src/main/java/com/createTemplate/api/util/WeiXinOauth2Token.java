package com.createTemplate.api.util;

import lombok.Data;

/**
 * @ClassName WeiXinOauth2Token
 * @Author libiqi
 * @Description TODO
 * @Date 2019/11/6 3:37 下午
 * @Version 1.0
 */
@Data
public class WeiXinOauth2Token {
    private String accessToken;

    private Integer expiresIn;

    private String refeshToken;

    private String openId;

    private String scope;
}
