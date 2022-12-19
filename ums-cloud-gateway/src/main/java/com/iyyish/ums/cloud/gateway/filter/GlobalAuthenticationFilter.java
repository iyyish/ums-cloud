package com.iyyish.ums.cloud.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iyyish.ums.cloud.common.core.constant.Constants;
import com.iyyish.ums.cloud.common.core.result.ApiResponse;
import com.iyyish.ums.cloud.common.core.result.ResponseCode;
import com.iyyish.ums.cloud.gateway.config.WhiteListConfig;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @desc: 网关过滤器
 * @date: 2022年12月15日
 */
@Component
public class GlobalAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private WhiteListConfig whiteList;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestUrl = exchange.getRequest().getPath().value();
        //1、白名单放行，比如授权服务、静态资源.....
        if (checkUrls(whiteList, requestUrl)) {
            return chain.filter(exchange);
        }

        //2、 检查token是否存在
        String token = getToken(exchange);
        if (StringUtils.isBlank(token)) {
            return invalidTokenMono(exchange);
        }

        //3 判断是否是有效的token
        OAuth2AccessToken oAuth2AccessToken;
        try {
            //解析token，使用tokenStore
            oAuth2AccessToken = tokenStore.readAccessToken(token);
            Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
            //去除jti并在黑名单中查找,如果包含则说明已注销
            String jti = additionalInformation.get(Constants.JWT_JTI).toString();
            Boolean isExists = redisTemplate.hasKey(Constants.JWT_JTI_BLACK_PREFIX + jti);
            if (isExists != null && isExists) {
                return invalidTokenMono(exchange);
            }
            //取出用户身份信息
            String user_name = additionalInformation.get(Constants.JWT_USER_NAME).toString();
            String userId = additionalInformation.get(Constants.JWT_USER_ID).toString();
            //获取用户权限
            List<String> authorities = (List<String>) additionalInformation.get(Constants.JWT_AUTHORITIES_NAME);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.JWT_PRINCIPAL_NAME, user_name);
            jsonObject.put(Constants.JWT_USER_ID, userId);
            jsonObject.put(Constants.JWT_AUTHORITIES_NAME, authorities);
            jsonObject.put(Constants.JWT_JTI, jti);
            jsonObject.put(Constants.JWT_EXPR, oAuth2AccessToken.getExpiresIn());
            //将解析后的token加密放入请求头中，方便下游微服务解析获取用户信息
            String base64 = Base64.encodeBase64String(jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));
            //放入请求头中,后面的微服务中可以取到
            ServerHttpRequest tokenRequest = exchange.getRequest().mutate().header(Constants.JWT_REDIRECT_ATTRIBUTE, base64).build();
            ServerWebExchange build = exchange.mutate().request(tokenRequest).build();
            return chain.filter(build);
        } catch (InvalidTokenException e) {
            //解析token异常，直接返回token无效
            return invalidTokenMono(exchange);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private String getToken(ServerWebExchange exchange) {
        String tokenStr = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StringUtils.isBlank(tokenStr)) {
            return null;
        }
        String token = tokenStr.split(" ")[1];
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return token;
    }

    private boolean checkUrls(WhiteListConfig whiteList, String path) {
        List<String> urls = whiteList.getUrls();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String url : urls) {
            if (pathMatcher.match(url, path))
                return true;
        }
        return false;
    }

    private Mono<Void> invalidTokenMono(ServerWebExchange exchange) {
        return buildReturnMono(ApiResponse.build(ResponseCode.AuthStatus.INVALID_TOKEN), exchange);
    }

    private Mono<Void> buildReturnMono(ApiResponse<?> data, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        byte[] bits = JSON.toJSONString(data).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=utf-8");
        return response.writeWith(Mono.just(buffer));
    }
}
