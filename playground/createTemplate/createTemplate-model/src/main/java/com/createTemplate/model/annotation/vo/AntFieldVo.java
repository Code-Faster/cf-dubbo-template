/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.model.annotation.vo;

import java.lang.reflect.Field;

public class AntFieldVo
{
  private String type;
  private String fieldName;
  private String name;
  private String desc;
  private boolean required;

  public String getName()
  {
    return this.name; }

  public void setName(String name) {
    this.name = name; }

  public String getDesc() {
    return this.desc; }

  public void setDesc(String desc) {
    this.desc = desc; }

  public boolean isRequired() {
    return this.required; }

  public void setRequired(boolean required) {
    this.required = required; }

  public String getFieldName() {
    return this.fieldName; }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getType() {
    return this.type; }

  public void setType(String type) {
    this.type = type; }

  public static String setType(Field field) {
    if (field.getGenericType().toString().equals(
      "class java.lang.Integer")){
    	return "int";
    }
    if (field.getGenericType().toString().equals(
      "class java.lang.String")){
    	return "string";
    }
    if (field.getGenericType().toString().equals(
      "class java.lang.Boolean")){
    	return "boolean";
    }
    if (field.getGenericType().toString().equals(
      "class java.lang.Long"))
      {
    	return "long";
      }
    if (field.getGenericType().toString().equals(
      "class java.lang.Float"))
    {
    	return "float";
    }
    if (field.getGenericType().toString().equals(
      "class java.lang.Double")){
    	{
    		return "double";
    	}
    }
    if (field.getGenericType().toString().equals(
      "class java.lang.Date"))
      {
    	return "date";
      }
    if (field.getGenericType().toString().equals(
      "class java.lang.Object")){
    	return "object";
    }
    if (field.getGenericType().toString().equals(
      "class java.util.Date")){
      return "date";
    }
    if (field.getGenericType().toString().equals(
    	      "class java.io.File")){
    	return "file";
    }
    if (field.getGenericType().toString().equals("interface java.util.List")
    		|| field.getGenericType().toString().equals("class java.util.ArrayList")
    		|| field.getGenericType().toString().equals("interface java.util.Set")
    		|| field.getGenericType().toString().equals("class java.util.HashSet")){
    	return "array";
    }
    return "";
  }
}