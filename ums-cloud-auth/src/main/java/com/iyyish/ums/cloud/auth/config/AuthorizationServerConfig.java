package com.iyyish.ums.cloud.auth.config;

import com.iyyish.ums.cloud.auth.error.AuthClientCredentialsTokenEndpointFilter;
import com.iyyish.ums.cloud.auth.error.AuthWebResponseExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @desc: 认证授权服务配置类
 * @date: 2022年12月12日
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private ClientDetailsService clientDetailsService;//spring.main.allow-bean-definition-overriding=true
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 配置令牌端点访问权限
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        AuthClientCredentialsTokenEndpointFilter filter = new AuthClientCredentialsTokenEndpointFilter(security);
        filter.afterPropertiesSet();
        filter.setAuthenticationEntryPoint(authenticationEntryPoint);
        security
                //开启/oauth/token_key访问权限
                .tokenKeyAccess("permitAll()")
                //开启/oauth/check_token访问权限
                .checkTokenAccess("permitAll()")
                //自定义认证失败处理类
                .authenticationEntryPoint(authenticationEntryPoint)
                //添加filter
                .addTokenEndpointAuthenticationFilter(filter);
        //.allowFormAuthenticationForClients();//允许form表单提交client_id和client_secret进行登录认证.配置自定义filter后需要关闭
    }

    /**
     * 配置第三方接入客户端信息服务
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //授权码模式需要,授权码管理服务
                .authorizationCodeServices(authorizationCodeServices)
                //密码授权模式需要
                .authenticationManager(authenticationManager)
                //refresh_token需要,否则报错
                .userDetailsService(userDetailsService)
                //token令牌管理服务,任何授权模式都需要
                .tokenServices(authorizationServerTokenServices)
                //配置只允许POST方式提交令牌, uri: /oauth/token
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)
                //自定义用户名或密码错误、授权类型不支持的异常处理
                .exceptionTranslator(new AuthWebResponseExceptionTranslator());
    }
}
