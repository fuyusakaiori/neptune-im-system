package com.fuyusakaiori.nep.im.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class NepRedisClientConfig {

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory connectionFactory){
        // 1. 创建 redis template
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 2. 设置连接工厂
        redisTemplate.setConnectionFactory(connectionFactory);
        // 3. 创建序列化工具
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        // 4. 设置 key 的序列化器
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // 5. 设置 value 的序列化器
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        // 6. 返回 redis template
        return redisTemplate;
    }

}
