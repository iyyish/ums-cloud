package com.iyyish.ums.cloud.auth.config;

import com.iyyish.ums.cloud.common.core.constant.Constants;
import com.iyyish.ums.cloud.common.core.domain.SecurityUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @desc: token令牌配置类
 * @date: 2022年12月13日
 */
@Configuration
public class AccessTokenConfig {

    /**
     * 令牌增强
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new OAuth2JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(Constants.JWT_SIGN_KEY);
        return accessTokenConverter;
    }

    /**
     * JWT方式存储AccessToken
     * @return
     */
    @Bean
    public JwtTokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    private static class OAuth2JwtAccessTokenConverter extends JwtAccessTokenConverter {
        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
            //获取认证用户信息
            Object principal = authentication.getPrincipal();
            if (principal instanceof SecurityUser) {
                SecurityUser user = (SecurityUser) principal;
                // 将额外的用户信息放入AccessToken
                Map<String, Object> additionalInformation = new LinkedHashMap<>();
                additionalInformation.put(Constants.JWT_USER_ID, user.getId());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
            }
            return super.enhance(accessToken, authentication);
        }
    }
}
