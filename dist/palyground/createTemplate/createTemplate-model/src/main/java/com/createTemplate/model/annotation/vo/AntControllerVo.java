/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.model.annotation.vo;

import java.util.List;


public class AntControllerVo
{
  private String moduleName;
  private String className;
  private String author;
  private String desc;
  private String name;
  private String path;
  private List<AntMethodVo> antMethodVoList;

  public String getPath()
  {
    return this.path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getClassName() {
    return this.className;
  }

  public String getModuleName() {
    return this.moduleName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getAuthor() {
    return this.author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getDesc() {
    return this.desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<AntMethodVo> getAntMethodVoList() {
    return this.antMethodVoList;
  }

  public void setAntMethodVoList(List<AntMethodVo> antMethodVoList) {
    this.antMethodVoList = antMethodVoList;
  }
}