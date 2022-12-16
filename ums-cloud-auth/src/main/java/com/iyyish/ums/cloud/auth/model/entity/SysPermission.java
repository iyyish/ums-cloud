package com.iyyish.ums.cloud.auth.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @desc: 权限
 * @date: 2022年12月16日
 */
@Data
@TableName("ums_permission")
public class SysPermission {
    /** 权限id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 权限名称 */
    private String name;
    /** 权限路径 */
    private String url;
    /** 数据创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /** 数据更新时间 */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
