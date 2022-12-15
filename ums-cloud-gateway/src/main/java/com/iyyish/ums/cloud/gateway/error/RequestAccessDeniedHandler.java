package com.iyyish.ums.cloud.gateway.error;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.iyyish.ums.cloud.common.core.result.ApiResponse;
import com.iyyish.ums.cloud.common.core.result.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @desc: 权限不足
 * @date: 2022年12月15日
 */
@Slf4j
@Component
public class RequestAccessDeniedHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, AccessDeniedException e) {
        log.error("权限不足, {}", e.getMessage(), e);
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.getHeaders().add("content-type", "application/json;charset=utf-8");
        response.setStatusCode(HttpStatus.OK);
        ApiResponse<?> data = ApiResponse.build(ResponseCode.AuthStatus.ACCESS_DENIED);
        String json = JSON.toJSONString(data, SerializerFeature.WriteNullStringAsEmpty);
        DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
