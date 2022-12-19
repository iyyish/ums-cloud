package com.iyyish.ums.cloud.common.web.exception;

import com.iyyish.ums.cloud.common.core.result.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @desc: 全局异常处理器
 * @date: 2022年12月19日
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = {BizException.class})
    public ApiResponse<String> handleBizException(BizException e) {
        log.error("业务异常, {}", e.getMessage(), e);
        return ApiResponse.failed(e.getMessage());
    }

    /**
     * 未知异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public ApiResponse<String> handleException(Exception e) {
        log.error("系统异常, {}", e.getMessage(), e);
        return ApiResponse.failed();
    }
}
