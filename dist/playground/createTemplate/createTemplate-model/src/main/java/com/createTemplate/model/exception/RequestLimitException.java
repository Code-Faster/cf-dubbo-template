/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.model.exception;

import java.io.Serializable;

public class RequestLimitException extends BaseException{  
    private static final long serialVersionUID = 1364225358754654702L;  
  
    public RequestLimitException() {  
        super("HTTP请求超出设定的限制");  
    }  
  
    public RequestLimitException(String message) {  
        super(message);  
    }  
  
}