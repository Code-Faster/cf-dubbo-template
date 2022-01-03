/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.provider.base.dubbo.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.alibaba.dubbo.config.annotation.Service;
import com.createTemplate.api.base.dao.AbstractBaseRedisDao;
import com.createTemplate.api.base.dubbo.service.CommonRedisDao;

/**
 * 通用redis服务实现类
 *
 * @version V1.0
 * @author:
 * @date: 2018年5月15日 下午2:11:10
 */
@Service(version = "1.0.0", retries = -1, timeout = 6000)
public class CommonRedisDaoImpl extends AbstractBaseRedisDao implements CommonRedisDao {
    @Override
    public void setDB(int db) {
        RedisConnectionFactory factory = this.redisTemplate.getConnectionFactory();
        factory.getConnection().select(db);
        this.redisTemplate.setConnectionFactory(factory);
    }

    @Override
    public void delete(String key) {
        this.redisTemplate.delete(key);
    }

    @Override
    public void delete(List<String> keys) {
        this.redisTemplate.delete(keys);
    }

    @Override
    public void delete(Set<String> keys) {
        this.redisTemplate.delete(keys);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void set(final String key, final String cacheObject) {
        this.redisTemplate.execute(new RedisCallback<Object>() {

            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                @SuppressWarnings("rawtypes")
                RedisSerializer valueRedisSerializer = redisTemplate.getValueSerializer();
                connection.set(redisTemplate.getStringSerializer().serialize(key), valueRedisSerializer.serialize(
                        cacheObject));
                return null;
            }
        });
    }

    @Override
    public void set(final String key, final String cacheObject, final int minutes) {

        this.redisTemplate.execute(new RedisCallback<Object>() {

            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer valueRedisSerializer = redisTemplate.getValueSerializer();
                connection.set(redisTemplate.getStringSerializer().serialize(key), valueRedisSerializer.serialize(
                        cacheObject));
                connection.expire(redisTemplate.getStringSerializer().serialize(key), minutes * 60);
                return null;
            }
        });

    }

    @Override
    public void setBySecond(final String key, final String cacheObject, final int second) {
        this.redisTemplate.execute(new RedisCallback<Object>() {

            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer valueRedisSerializer = redisTemplate.getValueSerializer();
                connection.set(redisTemplate.getStringSerializer().serialize(key), valueRedisSerializer.serialize(
                        cacheObject));
                connection.expire(redisTemplate.getStringSerializer().serialize(key), second);
                return null;
            }
        });
    }

    @Override
    public String get(final String key) {
        Object obj = this.redisTemplate.execute(new RedisCallback<Object>() {

            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] redisKey = redisTemplate.getStringSerializer().serialize(key);
                if (connection.exists(redisKey).booleanValue()) {
                    byte[] value = connection.get(redisKey);
                    Serializable valueSerial = (Serializable) redisTemplate.getValueSerializer().deserialize(value);
                    return valueSerial;
                }
                return null;
            }
        });

        if (obj == null) {
            return null;
        }

        return obj.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getAndExpire(final String key, final int minutes) {

        Object obj = this.redisTemplate.execute(new RedisCallback() {
            @Override
            public Serializable doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] redisKey = redisTemplate.getStringSerializer().serialize(key);
                if (connection.exists(redisKey).booleanValue()) {
                    byte[] value = connection.get(redisKey);
                    Serializable valueSerial = (Serializable) redisTemplate.getValueSerializer().deserialize(value);
                    connection.expire(redisKey, minutes * 60);
                    return valueSerial;
                }
                return null;
            }

        });
        if (obj == null) {
            return null;
        }

        return obj.toString();
    }

    @Override
    public void flushDB() {
        this.redisTemplate.execute(new RedisCallback() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return Boolean.valueOf(true);
            }
        });
    }

    @Override
    public void deleteLike(String pattern) {
        Set set = this.redisTemplate.keys(pattern);
        if (set.size() > 0) {
            delete(set);
        }
    }
}
