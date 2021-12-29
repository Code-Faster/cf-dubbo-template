package com.createTemplate.api.base.dubbo.service;

import java.util.List;
import java.util.Set;

/**
 * 通用redis操作类
 * @author:  
 * @date: 2018年5月31日 下午5:10:24 
 * @version V1.0
 */
public abstract interface CommonRedisDao
{
  /**
   * 选择db
   * @param db
   */
  public abstract void setDB(int db);
  
  /**
   * 删除key对应的值
   * @param key
   */
  public abstract void delete(String key);
  
  /**
   * 删除keys中所有key对应的值
   * @param keys
   */
  public abstract void delete(List<String> keys);
  /**
   * 删除keys中所有key对应的值
   * @param keys
   */
  public abstract void delete(Set<String> keys);
  
  /**
   * 以key为健永久存储cacheObject值
   * @param key
   * @param cacheObject
   */
  public abstract void set(final String key, final String cacheObject);
  /**
   * 获取健为key对应的值
   * @param key
   * @return
   */
  public abstract String get(final String key);
  /**
   * 清空当前db
   */
  public abstract void flushDB();
  /**
   * 以key为健存储minutes分钟cacheObject值
   * @param key
   * @param cacheObject
   * @param minutes
   */
  public abstract void set(final String key, final String cacheObject, final int minutes);
  /**
   * 以key为健存储second秒钟cacheObject值
   * @param key
   * @param cacheObject
   * @param minutes
   */
  public abstract void setBySecond(final String key, final String cacheObject, final int second);
  
  /**
   * 取出值，并且将原先的键值对删除
   * @param key
   * @param minutes
   * @return
   */
  public abstract String getAndExpire(final String key, final int minutes);
  
  /**
   * 删除key可以匹配的所有的键值对
   * @param pattern
   */
  public abstract void deleteLike(String pattern);
}