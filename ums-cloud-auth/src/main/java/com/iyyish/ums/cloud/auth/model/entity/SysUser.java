package com.iyyish.ums.cloud.auth.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @desc: 用户
 * @date: 2022年12月16日
 */
@Data
@TableName("ums_user")
public class SysUser {
    /** 用户id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 数据创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /** 数据更新时间 */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
