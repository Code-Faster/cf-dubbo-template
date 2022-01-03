/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.model.mybatis.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value ="分页类")
public class PageParameter implements Serializable
{
  private static final long serialVersionUID = 1L;
  public static final int DEFAULT_PAGE_SIZE = 10;
  @ApiModelProperty(hidden=true)
  private int pageSize;
  @ApiModelProperty(hidden=true)
  private int currentPage;
  @ApiModelProperty(hidden=true)
  private int prePage;
  @ApiModelProperty(hidden=true)
  private int nextPage;
  @ApiModelProperty(hidden=true)
  private int totalPage;
  @ApiModelProperty(hidden=true)
  private int totalCount;

  public PageParameter()
  {
    this.currentPage = 1;
    this.pageSize = 10;
  }

  public PageParameter(int currentPage, int pageSize)
  {
    if (currentPage == 0) {
        currentPage = 1;
    }
    this.currentPage = currentPage;
    this.pageSize = pageSize;
  }

  public int getCurrentPage() {
    return this.currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public int getPageSize() {
    return this.pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getPrePage() {
    return this.prePage;
  }

  public void setPrePage(int prePage) {
    this.prePage = prePage;
  }

  public int getNextPage() {
    return this.nextPage;
  }

  public void setNextPage(int nextPage) {
    this.nextPage = nextPage;
  }

  public int getTotalPage() {
    return this.totalPage;
  }

  public void setTotalPage(int totalPage) {
    this.totalPage = totalPage;
  }

  public int getTotalCount() {
    return this.totalCount;
  }

  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }
}
