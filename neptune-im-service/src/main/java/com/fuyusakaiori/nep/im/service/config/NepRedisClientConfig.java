package com.fuyusakaiori.nep.im.service.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class NepRedisClientConfig {

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory connectionFactory){
        // 1. 创建 redis template
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 2. 设置连接工厂
        redisTemplate.setConnectionFactory(connectionFactory);
        // 3. 创建序列化工具: netty 只能使用 redisson 之类的客户端, springboot 可以使用 redisTemplate 作为 "客户端", 这两者在序列化的时候有区别
        // 注: 如果使用 redisson 存放, 那么就是直接存放的字符串, 不会标记任何类型信息, 但是 redisTemplate 好几个序列化器都是需要类型信息才可以反序列化的, 所以选了 Jackson 这个
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // 4. 设置反序列化器
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        // 5. 设置 key 的序列化器
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // 6. 设置 value 的序列化器
        redisTemplate.setValueSerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(serializer);
        // 7. 返回 redis template
        return redisTemplate;
    }

}
