/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.model.exception;

import java.io.Serializable;

public class ExceptionCause implements Serializable
{
  private String messageKey;
  private Object[] messageArgs;
  private boolean resource;
  private int index;

  public ExceptionCause()
  {
    this.messageKey = null;

    this.messageArgs = null;

    this.resource = false;

    this.index = -1;
  }

  public ExceptionCause(String messageKey)
  {
    this.messageKey = null;

    this.messageArgs = null;

    this.resource = false;

    this.index = -1;

    this.messageKey = messageKey;
  }

  public ExceptionCause(String messageKey, Object messageArgs) {
    this(messageKey, new Object[] { messageArgs });
  }

  public ExceptionCause(String messageKey, Object[] messageArgs)
  {
    this.messageKey = null;

    this.messageArgs = null;

    this.resource = false;

    this.index = -1;

    this.messageKey = messageKey;
    this.messageArgs = messageArgs;
    this.resource = true;
  }

  public Object[] getMessageArgs() {
    return this.messageArgs;
  }

  public void setMessageArgs(Object[] messageArgs) {
    this.messageArgs = messageArgs;
  }

  public String getMessageKey() {
    return this.messageKey;
  }

  public void setMessageKey(String messageKey) {
    this.messageKey = messageKey;
  }

  public boolean isResource() {
    return this.resource;
  }

  public void setResource(boolean resource) {
    this.resource = resource;
  }

  public int getIndex() {
    return this.index;
  }

  public void setIndex(int index) {
    this.index = index;
  }
}