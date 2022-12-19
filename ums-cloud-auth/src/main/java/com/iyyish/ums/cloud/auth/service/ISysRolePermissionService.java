package com.iyyish.ums.cloud.auth.service;

import com.iyyish.ums.cloud.auth.model.vo.SysRolePermissionVo;

import java.util.List;

/**
 * @desc: 角色-权限
 * @date: 2022年12月18日
 */
public interface ISysRolePermissionService {
    /**
     * 查询权限-角色关系, permission-roles
     * @return
     */
    List<SysRolePermissionVo> queryPermissionRoles();
}
