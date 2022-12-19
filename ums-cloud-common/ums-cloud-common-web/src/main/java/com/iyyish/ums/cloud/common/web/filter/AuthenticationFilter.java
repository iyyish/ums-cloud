package com.iyyish.ums.cloud.common.web.filter;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iyyish.ums.cloud.common.core.constant.Constants;
import com.iyyish.ums.cloud.common.core.domain.LoginUser;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @desc: 微服务端的认证过滤器
 * 网关服务做完认证和授权之后,通过request传递认证信息到之后的微服务
 * @date: 2022年12月19日
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(Constants.JWT_REDIRECT_ATTRIBUTE);
        if (StringUtils.isNoneBlank(token)) {
            String text = new String(Base64.decodeBase64(token), StandardCharsets.UTF_8);
            JSONObject jsonObject = JSON.parseObject(text);
            //获取用户身份信息、token信息、权限信息
            String principal = jsonObject.getString(Constants.JWT_PRINCIPAL_NAME);
            String userId = jsonObject.getString(Constants.JWT_USER_ID);
            String jti = jsonObject.getString(Constants.JWT_JTI);
            Long exprIn = jsonObject.getLong(Constants.JWT_EXPR);
            JSONArray jsonArray = jsonObject.getJSONArray(Constants.JWT_AUTHORITIES_NAME);
            //保存登录用户信息
            LoginUser loginUser = LoginUser.builder()
                    .userId(userId)
                    .username(principal)
                    .authorities(jsonArray.toJavaList(String.class))
                    .jti(jti)
                    .expire(exprIn)
                    .build();
            request.setAttribute(Constants.LOGIN_USER_ATTRIBUTE, loginUser);
        }
        filterChain.doFilter(request, response);
    }
}
