package com.iyyish.ums.cloud.auth.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iyyish.ums.cloud.common.core.constant.Constants;
import com.iyyish.ums.cloud.common.core.result.ApiResponse;
import com.iyyish.ums.cloud.common.core.result.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @desc: 登出, JWT失效
 * @date: 2022年12月23日
 */
@Slf4j
@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 通过黑名单方式登出
     * @return
     */
    @PostMapping("/logout")
    public ApiResponse<?> logout(HttpServletRequest request) {
        //获取请求头中的加密的用户信息
        String token = request.getHeader(Constants.JWT_REDIRECT_ATTRIBUTE);
        if (StringUtils.isNotBlank(token)) {
            //解密
            String json = new String(Base64.decodeBase64(token), StandardCharsets.UTF_8);
            JSONObject jsonObject = JSON.parseObject(json);
            //获取用户身份信息,token信息
            String principal = jsonObject.getString(Constants.JWT_PRINCIPAL_NAME);
            String userId = jsonObject.getString(Constants.JWT_USER_ID);
            String jti = jsonObject.getString(Constants.JWT_JTI);
            Long exprIn = jsonObject.getLong(Constants.JWT_EXPR);
            log.info("注销成功, username:{},userId:{},jti:{},expr:{}", principal, userId, jti, exprIn);
            // 将JTI作为key存入Redis
            redisTemplate.opsForValue().set(Constants.JWT_JTI_BLACK_PREFIX + jti, "", exprIn, TimeUnit.SECONDS);
            return ApiResponse.ok();
        } else {
            return ApiResponse.build(ResponseCode.AuthStatus.INVALID_TOKEN);
        }
    }
}
