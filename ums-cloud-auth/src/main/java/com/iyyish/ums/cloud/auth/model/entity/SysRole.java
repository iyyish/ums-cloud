package com.iyyish.ums.cloud.auth.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @desc: 角色
 * @date: 2022年12月16日
 */
@Data
@TableName("ums_role")
public class SysRole {
    /** 角色id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 角色名称 */
    private String name;
    /** 角色代码 */
    private String code;
    /** 数据创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /** 数据更新时间 */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
