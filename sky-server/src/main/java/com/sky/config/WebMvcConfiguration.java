package com.sky.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    /**
     * 注册自定义拦截器
     * <p>
     * 将 JwtTokenAdminInterceptor 拦截器添加到 Spring MVC 的拦截器链中。
     * 拦截所有 /admin/** 的请求，但排除登录接口 /admin/employee/login，
     * 因为登录时还没有 token，需要放行。
     * @param registry 拦截器注册器
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")   // 拦截所有 admin 下的请求
                .excludePathPatterns("/admin/employee/login");  // 放行登录接口
    }

    /**
     * 通过knife4j生成接口文档
     * @return Docket 对象，用于配置 Swagger 2 接口文档（管理端）
     */
    @Bean
    public Docket docketAdmin() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("苍穹外卖项目接口文档")
                .version("2.0")
                .description("苍穹外卖项目接口文档")
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .groupName("admin") // 设置组名
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.admin"))
                .paths(PathSelectors.any())
                .build();
    }
    /**
     * 通过knife4j生成接口文档
     * @return Docket 对象，用于配置 Swagger 2 接口文档（用户端）
     */
    @Bean
    public Docket docketUser() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("苍穹外卖项目接口文档")
                .version("2.0")
                .description("苍穹外卖项目接口文档")
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .groupName("user")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.user"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 设置静态资源映射
     * <p>
     * 使 knife4j 生成的接口文档页面（doc.html）和相关的 webjars 资源可以被访问。
     * @param registry 资源处理器注册器
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射 /doc.html 到 classpath:/META-INF/resources/ 下的静态文件
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        // 映射 /webjars/** 到 classpath:/META-INF/resources/webjars/，供 Swagger UI 使用
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 扩展Spring MVC框架的消息转换器
     * <p>
     * 在Spring MVC默认的消息转换器列表基础上，添加自定义的Jackson消息转换器，
     * 用于统一处理JSON序列化/反序列化（尤其是日期时间格式）。
     * 通过将自定义转换器置于列表首位，确保其优先于默认转换器生效。
     *
     * @param converters Spring MVC已有的消息转换器列表
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // 实例不被 Spring 管理，无法注入，可能多份实例，维护困难
        //converter.setObjectMapper(new JacksonObjectMapper());
        converter.setObjectMapper(objectMapper());
        converters.add(0, converter);
    }

    /**
     * 创建并配置Jackson的ObjectMapper实例
     * <p>
     * 使用@Bean注解将返回的JacksonObjectMapper实例交给Spring容器管理，
     * 使其成为单例Bean，可在整个应用中共享同一份配置，并支持依赖注入。
     *
     * @return 配置好的ObjectMapper实例（实际类型为JacksonObjectMapper）
     */
    // 选择 @Bean 方法是为了让 Spring 接管这个对象的创建和管理，使整个应用能共享同一个配置，并符合依赖注入的编程范式
    @Bean
    public ObjectMapper objectMapper() {
        return new JacksonObjectMapper();
    }
}
