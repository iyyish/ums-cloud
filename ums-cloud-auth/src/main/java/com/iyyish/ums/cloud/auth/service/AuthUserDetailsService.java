package com.iyyish.ums.cloud.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.iyyish.ums.cloud.auth.mapper.SysRoleMapper;
import com.iyyish.ums.cloud.auth.mapper.SysUserMapper;
import com.iyyish.ums.cloud.auth.mapper.SysUserRoleMapper;
import com.iyyish.ums.cloud.auth.model.entity.SysRole;
import com.iyyish.ums.cloud.auth.model.entity.SysUser;
import com.iyyish.ums.cloud.auth.model.entity.SysUserRole;
import com.iyyish.ums.cloud.common.core.domain.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @desc: 用户查询接口实现类
 * @date: 2022年12月12日
 */
@Component
public class AuthUserDetailsService implements UserDetailsService {
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private SysRoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.通过username查询用户信息
        LambdaQueryWrapper<SysUser> lqwUser = new LambdaQueryWrapper<>();
        lqwUser.eq(SysUser::getUsername, username);
        SysUser sysUser = userMapper.selectOne(lqwUser);
        if (Objects.isNull(sysUser)) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        //2.获取角色信息
        LambdaQueryWrapper<SysUserRole> lqwUserRole = new LambdaQueryWrapper<>();
        lqwUserRole.eq(SysUserRole::getUserId, sysUser.getId());
        List<SysUserRole> userRoleList = userRoleMapper.selectList(lqwUserRole);
        List<Long> roleIdList = userRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        LambdaQueryWrapper<SysRole> lqwRole = new LambdaQueryWrapper<>();
        lqwRole.in(SysRole::getId, roleIdList);
        List<SysRole> roleList = roleIdList.size() == 0 ? new ArrayList<>() : roleMapper.selectList(lqwRole);

        //3.封装UserDetails对象
        return SecurityUser.builder()
                .id(sysUser.getId())
                .username(sysUser.getUsername())
                .password(sysUser.getPassword())
                .authorities(AuthorityUtils.createAuthorityList(roleList.stream().map(SysRole::getCode).toArray(String[]::new)))
                .build();
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("admin"));
    }
}
