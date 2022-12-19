package com.iyyish.ums.cloud.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.iyyish.ums.cloud.auth.mapper.SysPermissionMapper;
import com.iyyish.ums.cloud.auth.mapper.SysRoleMapper;
import com.iyyish.ums.cloud.auth.mapper.SysRolePermissionMapper;
import com.iyyish.ums.cloud.auth.model.entity.SysPermission;
import com.iyyish.ums.cloud.auth.model.entity.SysRole;
import com.iyyish.ums.cloud.auth.model.entity.SysRolePermission;
import com.iyyish.ums.cloud.auth.model.vo.SysRolePermissionVo;
import com.iyyish.ums.cloud.auth.service.ISysRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @desc: 角色-权限
 * @date: 2022年12月18日
 */
@Service
public class SysRolePermissionServiceImpl implements ISysRolePermissionService {
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysPermissionMapper permissionMapper;
    @Autowired
    private SysRolePermissionMapper rolePermissionMapper;


    @Override
    public List<SysRolePermissionVo> queryPermissionRoles() {
        //查询所有权限
        List<SysPermission> permissionList = permissionMapper.selectList(null);
        return permissionList.stream().map(permission -> {
            //查询角色权限关联关系
            LambdaQueryWrapper<SysRolePermission> qwRolePermission = new LambdaQueryWrapper<>();
            qwRolePermission.eq(SysRolePermission::getPermissionId, permission.getId());
            List<SysRolePermission> rolePermissionList = rolePermissionMapper.selectList(qwRolePermission);
            List<Long> roleIdList = rolePermissionList.stream().map(SysRolePermission::getRoleId).collect(Collectors.toList());
            List<SysRole> roles = roleIdList.size() == 0 ? new ArrayList<>() : roleMapper.selectBatchIds(roleIdList);
            //封装结果
            return SysRolePermissionVo.builder()
                    .permissionId(permission.getId())
                    .permissionName(permission.getName())
                    .permissionUrl(permission.getUrl())
                    .roles(roles)
                    .build();
        }).collect(Collectors.toList());
    }
}
