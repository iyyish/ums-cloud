package com.iyyish.ums.cloud.auth.error;


import com.iyyish.ums.cloud.common.core.result.ApiResponse;
import com.iyyish.ums.cloud.common.core.result.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * @desc: 自定义异常翻译类, 处理用户名密码错误异常或授权类型不支持异常, 默认实现类 DefaultWebResponseExceptionTranslator
 * @date: 2022年12月14日
 */
@Slf4j
public class AuthWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity<?> translate(Exception e) throws Exception {
        log.error("用户认证失败,{}", e.getMessage());
        ResponseCode responseCode = ResponseCode.HttpStatus.INTERNAL_SERVER_ERROR;
        if (e instanceof UnsupportedGrantTypeException) {
            responseCode = ResponseCode.AuthStatus.UNSUPPORTED_GRANT_TYPE;
        } else if (e instanceof InvalidGrantException) {
            responseCode = ResponseCode.AuthStatus.BAD_CREDENTIALS;
        }
        ApiResponse<Object> apiResponse = ApiResponse.build(responseCode);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
