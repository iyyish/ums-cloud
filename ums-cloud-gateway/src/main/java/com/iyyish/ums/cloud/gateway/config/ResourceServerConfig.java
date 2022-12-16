package com.iyyish.ums.cloud.gateway.config;

import com.iyyish.ums.cloud.gateway.error.RequestAccessDeniedHandler;
import com.iyyish.ums.cloud.gateway.error.RequestAuthenticationEntryPoint;
import com.iyyish.ums.cloud.gateway.manager.JwtReactiveAuthenticationManager;
import com.iyyish.ums.cloud.gateway.manager.JwtReactiveAuthorizationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

/**
 * @desc: 资源服务器配置类, 可以把网关和其他微服务看做是一个整体, 由网关负责统一鉴权
 * @date: 2022年12月15日
 */
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {
    @Autowired
    private WhiteListConfig whiteList;
    @Autowired
    private JwtReactiveAuthenticationManager authenticationManager;
    @Autowired
    private JwtReactiveAuthorizationManager authorizationManager;
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
        AuthenticationWebFilter webFilter = new AuthenticationWebFilter(authenticationManager);
        webFilter.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());//Bearer Token解析
        http
                .authorizeExchange()
                //配置认证白名单
                .pathMatchers(whiteList.getArrayUrls()).permitAll()
                //自定义鉴权管理器
                .anyExchange().access(authorizationManager)
                .and()
                //配置自定义异常处理类
                .exceptionHandling()
                //认证失败
                .authenticationEntryPoint(authenticationEntryPoint)
                //鉴权失败
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                //自定义认证管理器
                .addFilterAt(webFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf().disable()
                .httpBasic().disable();
        return http.build();
    }
}
