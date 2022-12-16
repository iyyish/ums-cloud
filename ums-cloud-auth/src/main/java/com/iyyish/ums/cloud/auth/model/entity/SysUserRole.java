package com.iyyish.ums.cloud.auth.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @desc: 用户-角色
 * @date: 2022年12月16日
 */
@Data
@TableName("ums_user_role")
public class SysUserRole {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long roleId;
    /** 数据创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /** 数据更新时间 */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
