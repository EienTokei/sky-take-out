package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 总的来说就是 OSS 配置的“搬运工”：
 * 从 yml 文件中读取 sky.alioss 下的所有配置，封装成一个对象，方便其他组件（如 OssConfiguration）使用
 */
@Component
// 让 Spring 自动扫描并创建这个类的一个实例（Bean）
// 也可以不写 @Component，而是在配置类上用 @EnableConfigurationProperties(AliOssProperties.class) 显式注册
@ConfigurationProperties(prefix = "sky.alioss")
// 自动将配置文件中 sky.alioss 下的属性值绑定到字段上
// 告诉 Spring：去配置文件中找所有以 sky.alioss 开头的 key，然后尝试将值赋给这个类中同名的字段
// （通过 setter 方法，所以需要 @Data 或显式 setter）
// 至于配置文件中 access-key-id 带中划线，而 Java 字段是 accessKeyId。
// Spring Boot 会自动做 宽松绑定（Relaxed Binding），所以能匹配上。这是 @ConfigurationProperties 的一个便捷特性
@Data
public class AliOssProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

}
