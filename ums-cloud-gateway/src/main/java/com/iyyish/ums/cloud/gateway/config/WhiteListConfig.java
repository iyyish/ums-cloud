package com.iyyish.ums.cloud.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @desc: 认证白名单配置类
 * @date: 2022年12月16日
 */
@Data
@Component
@ConfigurationProperties(prefix = "oauth2.whitelist")
public class WhiteListConfig {
    private List<String> urls;
}
