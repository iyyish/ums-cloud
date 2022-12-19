package com.iyyish.ums.cloud.user.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @desc: ums-user
 * @date: 2022年12月19日
 */
@Data
public class SysUser {
    /** 用户id */
    private Long id;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 数据创建时间 */
    private LocalDateTime createTime;
    /** 数据更新时间 */
    private LocalDateTime updateTime;
}
