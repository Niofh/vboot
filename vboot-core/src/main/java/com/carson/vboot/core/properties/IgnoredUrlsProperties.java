package com.carson.vboot.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Nicofh on 2020-03-14
 * 忽略url
 */
@Configuration
@ConfigurationProperties(prefix = "ignored")
@Data
public class IgnoredUrlsProperties {

    /**
     * 无需登录认证的请求
     */
    private List<String> urls = new ArrayList<>();

    /**
     * 限流及黑名单不拦截的路径
     */
    private List<String> limitUrls = new ArrayList<>();
}
