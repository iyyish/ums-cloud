package com.iyyish.ums.cloud.gateway.config;

import com.iyyish.ums.cloud.gateway.error.RequestAccessDeniedHandler;
import com.iyyish.ums.cloud.gateway.error.RequestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authorization.AuthorizationContext;

/**
 * @desc: 资源服务器配置类, 可以把网关和其他微服务看做是一个整体, 由网关负责统一鉴权
 * @date: 2022年12月15日
 */
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {
    @Autowired
    private ReactiveAuthenticationManager reactiveAuthenticationManager;
    @Autowired
    private ReactiveAuthorizationManager<AuthorizationContext> reactiveAuthorizationManager;
    @Autowired
    private RequestAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private RequestAccessDeniedHandler accessDeniedHandler;
    /**
     * authorization:授权
     * authentication:认证
     * @param http
     * @return
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter webFilter = new AuthenticationWebFilter(reactiveAuthenticationManager);
        webFilter.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());//Bearer Token解析
        http
                .authorizeExchange()
                // 所有请求都走自定义鉴权管理器
                .anyExchange().access(reactiveAuthorizationManager)
                .and()
                // 注册自定义认证处理器
                .addFilterAt(webFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                // 配置自定义异常处理类
                .exceptionHandling()
                // 认证失败
                .authenticationEntryPoint(authenticationEntryPoint)
                // 鉴权失败
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .csrf().disable()
                .httpBasic().disable();
        return http.build();
    }
}
