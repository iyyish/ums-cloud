package com.iyyish.ums.cloud.auth.service;

import com.iyyish.ums.cloud.common.core.domain.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @desc: 用户查询接口实现类
 * @date: 2022年12月12日
 */
@Component
public class AuthUserDetailsService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser user = SecurityUser.builder()
                .id(1001L)
                .username(username)
                .password(passwordEncoder.encode(username))
                .build();
        return user;
    }
}
