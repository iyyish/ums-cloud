package com.iyyish.ums.cloud.auth.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @desc: 重写客户端信息认证实现类, 即处理/oauth/token的登录认证请求
 * 如果配置allowFormAuthenticationForClients()且请求参数中包含client_id和client_secret则会走ClientCredentialsTokenEndpointFilter
 * 如果未配置allowFormAuthenticationForClients()或请求参数中不包含client_id和client_secret则会走BasicAuthenticationFilter
 * @date: 2022年12月14日
 */
@Slf4j
public class AuthClientCredentialsTokenEndpointFilter extends ClientCredentialsTokenEndpointFilter {
    private final AuthorizationServerSecurityConfigurer configurer;
    /** 覆盖父类属性 */
    private AuthenticationEntryPoint authenticationEntryPoint;

    public AuthClientCredentialsTokenEndpointFilter(AuthorizationServerSecurityConfigurer configurer) {
        this.configurer = configurer;
    }

    @Override
    protected AuthenticationManager getAuthenticationManager() {
        return configurer.and().getSharedObject(AuthenticationManager.class);
    }

    @Override
    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        super.setAuthenticationEntryPoint(null);
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    public void afterPropertiesSet() {
        //重写失败回调方法
        setAuthenticationFailureHandler(((httpServletRequest, httpServletResponse, e) -> {
            if (e instanceof BadCredentialsException) {
                e = new BadCredentialsException(e.getMessage(), new BadClientCredentialsException());
            }
            this.authenticationEntryPoint.commence(httpServletRequest, httpServletResponse, e);
        }));
        setAuthenticationSuccessHandler(((httpServletRequest, httpServletResponse, authentication) -> {
        }));
    }
}
