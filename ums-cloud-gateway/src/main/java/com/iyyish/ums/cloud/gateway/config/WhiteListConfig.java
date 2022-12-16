package com.iyyish.ums.cloud.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @desc: 认证白名单配置类
 * @date: 2022年12月16日
 */
@Component
@ConfigurationProperties(prefix = "oauth2.whitelist")
public class WhiteListConfig {
    private List<String> urls;

    public List<String> getUrls() {
        return urls;
    }

    /** 返回array */
    public String[] getArrayUrls() {
        if (urls == null || urls.size() == 0) {
            return new String[]{};
        }
        return urls.toArray(new String[0]);
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }


}
