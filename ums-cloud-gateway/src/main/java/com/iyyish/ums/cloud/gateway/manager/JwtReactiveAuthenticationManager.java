package com.iyyish.ums.cloud.gateway.manager;

import com.iyyish.ums.cloud.common.core.result.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @desc: 自定义认证管理器
 * @date: 2022年12月15日
 */
@Slf4j
@Component
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    @Autowired
    private TokenStore tokenStore;

    /**
     * justOrEmpty:基于对象创建Mono元素序列
     * filter:过滤元素
     * cast:转换成指定类型
     * map:遍历元素,返回流对象
     * @param auth the {@link Authentication} to test
     * @return
     */
    @Override
    public Mono<Authentication> authenticate(Authentication a) {
        return Mono
                //创建元素序列
                .justOrEmpty(a)
                //过滤
                .filter(authentication -> authentication instanceof BearerTokenAuthentication)
                //类型转换
                .cast(BearerTokenAuthenticationToken.class)
                //遍历获取流对象
                .map(authentication -> authentication.getToken())
                //处理对象,通过Header:Authorization:token解析成AccessToken对象
                .flatMap(token -> {
                    log.info("请求Token: {}", token);
                    //1.解析accessToken
                    OAuth2AccessToken accessToken = this.tokenStore.readAccessToken(token);
                    if (accessToken == null) {
                        return Mono.error(new InvalidTokenException(ResponseCode.AuthStatus.INVALID_TOKEN.getMsg()));
                    } else if (accessToken.isExpired()) {
                        return Mono.error(new InvalidTokenException(ResponseCode.AuthStatus.EXPIRED_TOKEN.getMsg()));
                    }
                    //2.解析Authentication
                    OAuth2Authentication authentication = this.tokenStore.readAuthentication(accessToken);
                    if (authentication == null) {
                        return Mono.error(new InvalidTokenException(ResponseCode.AuthStatus.INVALID_TOKEN.getMsg()));
                    }
                    return Mono.just(authentication);
                }).cast(Authentication.class);
    }
}
