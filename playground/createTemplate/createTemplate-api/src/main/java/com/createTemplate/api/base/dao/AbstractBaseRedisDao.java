package com.createTemplate.api.base.dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author:  
 * @date: 2018年6月13日 上午9:31:00 
 * @version V1.0
 */
@SuppressWarnings({"rawtypes","unchecked"})
public abstract class AbstractBaseRedisDao
{

   protected RedisTemplate redisTemplate;
    
   /**
    * 
    * @param redisTemplate
    */
    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setDefaultSerializer(getRedisSerializer());
        this.redisTemplate.setKeySerializer(getRedisSerializer());
        this.redisTemplate.setHashKeySerializer(getRedisSerializer());
        
        // 设置value 序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(  
                Object.class);  
        ObjectMapper om = new ObjectMapper();  
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);  
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);  
        jackson2JsonRedisSerializer.setObjectMapper(om);  
        this.redisTemplate.setValueSerializer(jackson2JsonRedisSerializer); 
        this.redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    }
    
    /**
     * 
     * @return
     */
    protected RedisSerializer<String> getRedisSerializer() {
        return this.redisTemplate.getStringSerializer();
    }
}