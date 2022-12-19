package com.iyyish.ums.cloud.common.web.exception;

/**
 * @desc: 自定义系统业务异常
 * @date: 2022年12月19日
 */
public class BizException extends RuntimeException {

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }
}
