package com.iyyish.ums.cloud.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @desc: 资源服务器配置类, 可以把网关和其他微服务看做是一个整体, 由网关负责统一鉴权
 * @date: 2022年12月15日
 */
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {
    //private final AuthorizationManager authorizationManager;
    //private final IgnoreUrlsConfig ignoreUrlsConfig;
    //private final RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    //private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.build();
    }
}
