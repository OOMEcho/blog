package com.blog.config.knife4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Knife4j配置类
 *
 * @author xuesong.lei
 * @since 2025/9/7 14:14
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        // 网站标题
                        .title("blog")
                        // 描述 可以穿插md语法
                        .description("blog 个人博客系统，基于 Spring Boot + Vue 前后端分离架构")
                        // 设置作者 服务器url 邮箱
                        .contact(new Contact("xuesong.lei", "http://127.0.0.1:9090", "blog_system@163.com"))
                        // 版本
                        .version("1.0")
                        .build())
                .select()
                // 要扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.blog"))
                // 要扫描的url
                .paths(PathSelectors.any())
                .build();
    }
}
