/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.model.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseException extends RuntimeException implements Serializable
{
  private static final long serialVersionUID = 1L;
  private List<ExceptionCause> causeList = new ArrayList();

  public BaseException()
  {
  }

  public BaseException(String message)
  {
    super(message);
  }

  public void addCause(ExceptionCause exceptionCause) {
    this.causeList.add(exceptionCause);
  }

  public List<ExceptionCause> getCauseList() {
    return this.causeList;
  }
}