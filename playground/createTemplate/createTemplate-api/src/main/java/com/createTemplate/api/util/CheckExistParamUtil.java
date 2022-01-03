package com.createTemplate.api.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.createTemplate.model.exception.BusinessException;

public class CheckExistParamUtil {

    private Map<String,Map<String, Object>> checkMap = null;
    private static final String ERRORMSG="errorMsg";
    private static final String VALUE="value";
    private CheckExistParamUtil() {
        checkMap = new HashMap<String,Map<String, Object>>();
    }

    public static CheckExistParamUtil getInstance() {
        return new CheckExistParamUtil();
    }

    public CheckExistParamUtil addCheckParam(HttpServletRequest request, String param, String errorMsg) {
        if(request==null){
            throw new BusinessException("请求不能为空");
        }
        if(StringUtil.checkNull(param)){
            throw new BusinessException("请添加待检测的参数");
        }
        
        if(StringUtil.checkNull(errorMsg)){
            throw new BusinessException("请添加参数为空时的提示信息");
        }
        String paramValue = request.getParameter(param);
        Map<String, Object>  valueMap = new HashMap<String, Object>();
        valueMap.put(VALUE, paramValue);
        valueMap.put(ERRORMSG, errorMsg);
        checkMap.put(param, valueMap);
        return this;
    }
    
    public CheckExistParamUtil addCheckParam(String fieldName,Object value, String errorMsg) {
        if(StringUtil.checkNull(fieldName)){
            throw new BusinessException("请添加待检测的参数");
        }
        
        if(StringUtil.checkNull(errorMsg)){
            throw new BusinessException("请添加参数为空时的提示信息");
        }
        if(!errorMsg.contains("不能为空")){
            errorMsg = errorMsg+"不能为空!";
        }
        Map<String, Object>  valueMap = new HashMap<String, Object>();
        valueMap.put(VALUE, value);
        valueMap.put(ERRORMSG, errorMsg);
        checkMap.put(fieldName, valueMap);
        return this;
    }
    
    public CheckExistParamUtil checkParam(HttpServletRequest request, String param, String errorMsg) {
        if(request==null){
            throw new BusinessException("请求不能为空");
        }
        if(StringUtil.checkNull(param)){
            throw new BusinessException("请添加待检测的参数");
        }
        if(StringUtil.checkNull(errorMsg)){
            throw new BusinessException("请添加参数为空时的提示信息");
        }
        if(!errorMsg.contains("不能为空")){
            errorMsg = errorMsg+"不能为空!";
        }
        String paramValue = request.getParameter(param);
        if( StringUtil.checkNull(paramValue)) {
            throw new BusinessException(Objects.toString(errorMsg,param+" can't be null !"));
        }
        return this;
    }
    
    
    public void check() {
        if (checkMap.keySet() != null && checkMap.keySet().size() > 0) {
            for (String param : checkMap.keySet()) {
                Map<String, Object>  valueMap =  checkMap.get(param);
                if(valueMap==null){
                    continue;
                }
                Object paramValue = valueMap.get(VALUE);
                if( StringUtil.checkNull(paramValue)) {
                    throw new BusinessException(Objects.toString(valueMap.get(ERRORMSG),param+" can't be null !"));
                }
            }
        }
    }
    

}
