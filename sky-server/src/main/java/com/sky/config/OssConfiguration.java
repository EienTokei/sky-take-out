package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类, 创建 AliOssUtil 的 Bean 实例，供项目其他地方（如 CommonController）依赖注入使用<br>
 * 是连接配置属性（AliOssProperties）和上传工具（AliOssUtil）的桥梁，负责以可扩展、可测试的方式生产 AliOssUtil 的 Bean
 */
@Configuration  // Spring 配置类，Spring 会处理其内部的 @Bean 方法
@Slf4j
public class OssConfiguration {
    @Bean
    // 放在方法上，表示该方法的返回值会被注册为 Spring 容器中的一个 Bean，默认方法名（aliOssUtil）就是 Bean 的名字
    @ConditionalOnMissingBean   // 只有当容器中不存在 AliOssUtil 类型的 Bean 时，才会执行这个方法创建 Bean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        // Spring 会自动从容器中查找 AliOssProperties 的 Bean 并注入进来（因为 AliOssProperties 标注了 @Component，已经被扫描注册）
        log.info("开始创建阿里云文件上传工具类");
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}
