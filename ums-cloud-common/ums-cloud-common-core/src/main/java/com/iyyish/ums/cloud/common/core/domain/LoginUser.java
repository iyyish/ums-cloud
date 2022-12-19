package com.iyyish.ums.cloud.common.core.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @desc: 保存登录的用户信息
 * @date: 2022年12月19日
 */
@Data
@Builder
public class LoginUser {
    private String userId;
    private String username;
    private List<String> authorities;
    /** jwt token唯一ID */
    private String jti;
    /** 距离 token 过期还剩多少秒 */
    private Long expire;
}
