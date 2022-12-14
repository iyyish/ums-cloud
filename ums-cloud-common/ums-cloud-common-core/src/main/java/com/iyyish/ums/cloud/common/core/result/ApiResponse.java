package com.iyyish.ums.cloud.common.core.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @desc: 接口响应对象
 * @date: 2022年12月14日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String msg;
    private T data;

    public static <T> ApiResponse<T> build(String code, String msg, T data) {
        return new ApiResponse<>(code, msg, data);
    }

    public static <T> ApiResponse<T> build(ResponseCode responseCode, T data) {
        return build(responseCode.getCode(), responseCode.getMsg(), data);
    }

    public static <T> ApiResponse<T> build(ResponseCode responseCode) {
        return build(responseCode, null);
    }

    public static <T> ApiResponse<T> ok() {
        return build(ResponseCode.HttpStatus.OK);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return build(ResponseCode.HttpStatus.OK, data);
    }

    public static <T> ApiResponse<T> failed() {
        return build(ResponseCode.HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
