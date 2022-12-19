package com.iyyish.ums.cloud.gateway.manager;

import com.iyyish.ums.cloud.common.core.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @desc: 自定义授权管理器
 * 1.角色-权限的对应关系是n:n
 * 2.权限表中代码格式为: method:/url/url
 * 3.系统启动时加载url-roles对应关系到redis
 * @date: 2022年12月15日
 */
@Component
public class JwtReactiveAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        //1.获取请求URL信息
        URI uri = context.getExchange().getRequest().getURI();
        String method = context.getExchange().getRequest().getMethodValue();
        //POST:/user/query or *:/user/query
        String restUrl = method + Constants.METHOD_SUFFIX + uri.getPath();
        //2.从redis读取url-roles对应关系
        HashOperations<String, String, List<String>> operations = redisTemplate.opsForHash();
        Map<String, List<String>> urlRoles = operations.entries(Constants.OAUTH_URLS);
        //3.筛选有权访问此次请求URL的所有角色
        List<String> canAccessRoles = new ArrayList<>();
        final AntPathMatcher antPathMatcher = new AntPathMatcher();
        urlRoles.forEach((url, roles) -> {
            if (antPathMatcher.match(url, restUrl)) {
                canAccessRoles.addAll(roles);
            }
        });
        return authentication
                //判断是否认证成功
                .filter(Authentication::isAuthenticated)
                //获取认证后的全部权限
                .flatMapIterable(Authentication::getAuthorities).map(GrantedAuthority::getAuthority).any(authority -> {
                    //管理员有所有权限
                    if (Constants.ROOT_ROLE_CODE.equals(authority)) {
                        return true;
                    }
                    return canAccessRoles.size() > 0 && canAccessRoles.contains(authority);
                }).map(AuthorizationDecision::new).defaultIfEmpty(new AuthorizationDecision(false));
    }
}
