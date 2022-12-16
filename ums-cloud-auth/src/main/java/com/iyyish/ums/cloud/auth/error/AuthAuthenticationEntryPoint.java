package com.iyyish.ums.cloud.auth.error;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.iyyish.ums.cloud.common.core.result.ApiResponse;
import com.iyyish.ums.cloud.common.core.result.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @desc: 客户端认证信息错误
 * @date: 2022年12月14日
 */
@Slf4j
public class AuthAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.error("客户端信息认证失败,{}", e.getMessage());
        ApiResponse<?> apiResponse = ApiResponse.build(ResponseCode.AuthStatus.BAD_CLIENT_CREDENTIALS);
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(JSON.toJSONString(apiResponse, SerializerFeature.WriteMapNullValue));
        writer.flush();
    }
}
