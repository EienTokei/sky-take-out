package com.sky.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {

    // 被 @Configuration 注解的配置类中，标记了 @Bean 的方法，会被 Spring 框架自动调用
    // 并将该方法的返回值对象注册为 Spring 容器中的一个 Bean
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建 redis 模板对象...");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // 设置redis的连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置redis key的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 配置支持 LocalDateTime 的 Jackson2JsonRedisSerializer
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer
                = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();

        // 注册 Java 8 时间模块
        objectMapper.registerModule(new JavaTimeModule());
        // 将日期序列化为 ISO-8601 字符串，而不是时间戳数组
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 启用默认类型, 保持类型信息
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置 value 的序列化器为 Jackson2JsonRedisSerializer
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        // 设置 hash 结构的序列化器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        return redisTemplate;
    }
}
