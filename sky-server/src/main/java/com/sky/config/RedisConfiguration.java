package com.sky.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {

    /**
     * 手动 RedisTemplate
     * <p>
     * 被 @Configuration 注解的配置类中，标记了 @Bean 的方法，会被 Spring 框架自动调用
     * 将该方法的返回值对象注册为 Spring 容器中的一个 Bean
     * @param redisConnectionFactory 负责创建 Redis 连接（类似于数据库连接池）
     * @return Spring Data Redis 提供的核心操作类，用于执行 Redis 命令
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建 redis 模板对象...");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // 设置redis的连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置redis key的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // Redis 的 key 序列化器，将字符串 key 直接以 UTF-8 字节存储

        // 配置支持 LocalDateTime 的 Jackson2JsonRedisSerializer
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer
                = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper objectMapper = redisObjectMapper();

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置 value 的序列化器为 Jackson2JsonRedisSerializer
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        // 设置 hash 结构的序列化器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        return redisTemplate;
    }

    /**
     * Spring Cache 专用的 CacheManager
     * @param redisConnectionFactory 负责创建 Redis 连接（类似于数据库连接池）
     * @return Spring Cache 用来管理 Redis 缓存的管理器，它决定了 @Cacheable 等注解如何存取数据
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建RedisCacheManager(支持JSON序列化)...");
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        serializer.setObjectMapper(redisObjectMapper());

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(serializer));

        // RedisCacheManager只会操作普通的 String-Value 结构，不会使用 Hash, 不需要设置

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(configuration)
                .build();
    }

    /**
     * 创建一个共享的 ObjectMapper Bean，避免重复配置
     * @return ObjectMapper, Jackson库的核心类，主要作用是在 Java 对象 和 JSON 字符串 之间进行转换
     */
    @Bean
    private static ObjectMapper redisObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 注册 Java 8 时间模块 - Jackson 的扩展模块，注册后可以正确序列化/反序列化 LocalDateTime、LocalDate 等时间类型
        objectMapper.registerModule(new JavaTimeModule());
        // 默认情况下，Jackson 会把时间类型写成时间戳数组（如 [2025,4,8,10,30,0]）。
        // 禁用后，会写成 ISO-8601 字符串（如 "2025-04-08T10:30:00"），更易读。
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 启用默认类型, 保持类型信息 - 在 JSON 中添加一个 @class 字段，记录对象的真实类名
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        return objectMapper;
    }
}
