package com.iyyish.ums.cloud.auth.config;

import com.iyyish.ums.cloud.auth.error.AuthAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.sql.DataSource;

/**
 * @desc: 配置支持类, 拆出来结构更清晰
 * @date: 2022年12月12日
 */
@Configuration
public class OAuth2ConfigSupport {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private TokenStore jwtTokenStore;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 密码加密
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * JDBC客户端信息存储, table:oauth_client_details
     * @return
     */
    @Bean
    public ClientDetailsService clientDetailsService() {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder());
        return clientDetailsService;
    }

    /**
     * JDBC授权码存储, table:oauth_code
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * token令牌存储, JWT
     * @return
     */
    @Bean
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        // 客户端配置策略
        tokenServices.setClientDetailsService(clientDetailsService());
        // 用户认证管理器
        tokenServices.setAuthenticationManager(authenticationManager);
        // 支持令牌刷新
        tokenServices.setSupportRefreshToken(true);
        // access_token 过期时间
        tokenServices.setAccessTokenValiditySeconds(3 * 60 * 60);
        // refresh_token 过期时间
        tokenServices.setRefreshTokenValiditySeconds(3 * 24 * 60 * 60);
        // 令牌存储服务
        tokenServices.setTokenStore(jwtTokenStore);
        // 设置令牌增强, 使用jwtAccessTokenConverter进行转换
        tokenServices.setTokenEnhancer(jwtAccessTokenConverter);
        return tokenServices;
    }

    /**
     * 自定义请求异常处理类
     * @return
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthAuthenticationEntryPoint();
    }
}
