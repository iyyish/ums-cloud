package com.iyyish.ums.cloud.auth.model.vo;

import com.iyyish.ums.cloud.auth.model.entity.SysRole;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @desc: url-roles
 * @date: 2022年12月18日
 */
@Data
@Builder
public class SysRolePermissionVo {
    private Long permissionId;
    private String permissionName;
    private String permissionUrl;
    private List<SysRole> roles;
}
