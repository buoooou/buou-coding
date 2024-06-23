package com.github.buoooou.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author YiHui
 * @date 2022/7/6
 */
@Configuration
@ComponentScan("com.github.buoooou.service")
@MapperScan(basePackages = {
        "com.github.buoooou.service.article.repository.mapper",
        "com.github.buoooou.service.user.repository.mapper",
        "com.github.buoooou.service.comment.repository.mapper",
        "com.github.buoooou.service.config.repository.mapper",
        "com.github.buoooou.service.statistics.repository.mapper",
        "com.github.buoooou.service.notify.repository.mapper",})
public class ServiceAutoConfig {
}
