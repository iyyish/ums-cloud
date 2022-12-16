package com.iyyish.ums.cloud.auth.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @desc: 角色-权限
 * @date: 2022年12月16日
 */
@Data
@TableName("ums_role_permission")
public class SysRolePermission {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long roleId;
    private Long permissionId;
    /** 数据创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /** 数据更新时间 */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
