package com.iyyish.ums.cloud.auth.manager;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.iyyish.ums.cloud.auth.mapper.SysRoleMapper;
import com.iyyish.ums.cloud.auth.mapper.SysUserMapper;
import com.iyyish.ums.cloud.auth.mapper.SysUserRoleMapper;
import com.iyyish.ums.cloud.auth.model.entity.SysRole;
import com.iyyish.ums.cloud.auth.model.entity.SysUser;
import com.iyyish.ums.cloud.auth.model.entity.SysUserRole;
import com.iyyish.ums.cloud.common.core.constant.Constants;
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
public class UserDetailsServiceManager implements UserDetailsService {
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
        List<SysRole> roleList = roleMapper.selectBatchIds(roleIdList);
        //3.拼接角色代码,与redis缓存中的格式一致
        //缓存角色信息格式:ROLE_XXX
        List<String> authorities = roleList.stream().map(sysRole -> Constants.ROLE_PREFIX + sysRole.getCode()).collect(Collectors.toList());

        //3.封装UserDetails对象
        return SecurityUser.builder()
                .id(sysUser.getId())
                .username(sysUser.getUsername())
                .password(sysUser.getPassword())
                .authorities(AuthorityUtils.createAuthorityList(ArrayUtil.toArray(authorities, String.class)))
                .build();
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("user"));
    }
}
