package com.iyyish.ums.cloud.common.core.constant;

/**
 * @desc: 公共常量
 * @date: 2022年12月12日
 */
public interface Constants {
    //region token令牌静态常量
    String JWT_SIGN_KEY = "ums-cloud";
    String JWT_USER_ID = "user_id";
    String JWT_USER_NAME = "user_name";
    String JWT_TOKEN_NAME = "jwt-token";
    String JWT_PRINCIPAL_NAME = "principal";
    String JWT_AUTHORITIES_NAME = "authorities";
    String JWT_JTI = "jti";
    String JWT_EXPR = "expr";
    //endregion

    String JWT_JTI_BLACK_PREFIX = "oauth2:black:";
    
    String METHOD_SUFFIX = ":";
    String ROOT_ROLE_CODE = "ROLE_ROOT";
    String ROLE_PREFIX = "ROLE_";
    String OAUTH_URLS = "oauth2:oauth_urls";
}
