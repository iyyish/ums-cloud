package com.iyyish.ums.cloud.common.core.result;

/**
 * @desc: 响应信息常量
 * @date: 2022年12月14日
 */
public interface ResponseCode {
    String getCode();

    String getMsg();

    /** http请求响应码 */
    enum HttpStatus implements ResponseCode {
        OK("200", "请求成功"),
        METHOD_NOT_ALLOWED("405", "请求方式不允许"),
        INTERNAL_SERVER_ERROR("500", "服务器异常");

        private final String code;
        private final String msg;

        HttpStatus(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        @Override
        public String getCode() {
            return this.code;
        }

        @Override
        public String getMsg() {
            return this.msg;
        }
    }

    /** oauth错误码 */
    enum AuthStatus implements ResponseCode {
        BAD_CLIENT_CREDENTIALS("4001", "客户端信息错误"),
        BAD_CREDENTIALS("4002", "用户名或密码错误"),
        UNSUPPORTED_GRANT_TYPE("4003", "授权类型不支持"),
        INVALID_TOKEN("4004", "未认证或令牌失效"),
        EXPIRED_TOKEN("4005", "令牌过期"),
        ACCESS_DENIED("4006", "权限不足");
        private final String code;
        private final String msg;

        AuthStatus(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        @Override
        public String getCode() {
            return this.code;
        }

        @Override
        public String getMsg() {
            return this.msg;
        }
    }
}
